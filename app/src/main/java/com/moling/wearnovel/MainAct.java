package com.moling.wearnovel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.moling.wearnovel.catAPI.catAPI;
import com.moling.wearnovel.locAPI.locAPI;
import com.moling.wearnovel.utils.templates.bookshelfIndex.book_list;
import com.moling.wearnovel.utils.templates.bookshelfIndex.bookshelfIndex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainAct extends Activity {

    private static bookshelfIndex localBooks; // 本地书架
    Handler syncBookshelf = null; // 同步书架 Handler
    public static Handler callToast = null; // Toast Handler
    public static MainAct MainAct;

    // region 系统变量
    public static SharedPreferences.Editor preferencesEditor;
    public static String documentsBaseDir;
    // endregion

    // region App 设置相关
    public static String catToken = "";
    public static String catAccount = "";
    public static String catAppVer = "";
    // endregion

    // region 菜单页面相关
    public static int page = 0;
    public static String menuTitle = "设置";
    public static String[] menuArgs = null;
    // endregion

    // region 章节页面相关
    public static int chapterPage = 0;
    public static book_list externalBookList = null;
    public static int externalChapterIndex = 0;
    // endregion

    // region 阅读页面相关
    public static String externalBookId;
    public static String[] externalChapterContent;
    public static int externalChapterPage;
    // endregion

    // region 页面控件
    ListView bookList;
    TextView tipText;
    // endregion

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main);
        MainAct = this;

        // 获取SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
        // 获取Editor对象的引用
        preferencesEditor = sharedPreferences.edit();
        // 阅读器 Intent

        // 获取书源设置
        catAccount = sharedPreferences.getString("Account", "");
        catToken = sharedPreferences.getString("Token", "");
        catAppVer = sharedPreferences.getString("AppVer", "");
        if (catAppVer == "") {
            preferencesEditor.putString("AppVer", "2.9.291");
            MainAct.preferencesEditor.commit();
        }
        Log.d("[MainAct]", "Cat Account:[" + catAccount + "]");
        Log.d("[MainAct]", "Cat Token:[" + catToken + "]");
        Log.d("[MainAct]", "Cat AppVer:[" + catAppVer + "]");

        // 界面对象
        bookList = (ListView) findViewById(R.id.list_book);
        tipText = (TextView) findViewById(R.id.text_tip);

        // 正文保存路径
        documentsBaseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/WearNovel";
        File files = new File(documentsBaseDir);
        if (!files.exists()) {
            files.mkdirs();
        }

        // 权限请求
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 222);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 222);

        // 同步书架 Handler
        syncBookshelf = new Handler(){
            public void handleMessage(Message msg){
                List<String> bookL = (List<String>)msg.obj;
                if ((long) bookL.size() != 0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainAct.this,android.R.layout.simple_list_item_1,bookL);
                    bookList.setAdapter(adapter);;
                    tipText.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainAct.this, "同步完成", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // 显示 Toast Handler
        callToast = (Handler) new Handler(){
            public void handleMessage(Message msg){
                String content = (String) msg.obj;
                Toast.makeText(MainAct.this, content, Toast.LENGTH_SHORT).show();
            }
        };

        // 尝试从本地读取书架
        try {
            localBooks = locAPI.GetBooksOnBookshelf();
            if ((long) localBooks.getData().size() != 0) {
                List<String> booksList = new ArrayList<>();
                for (int i = 0; i < (long) localBooks.getData().size(); i++) {
                    booksList.add(localBooks.getData().get(i).getBook_info().getBook_name());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,booksList);
                bookList.setAdapter(adapter);;
                tipText.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) { }

        if (bookList.getCount() == 0) {
            tipText.setVisibility(View.VISIBLE);
        } else {
            tipText.setVisibility(View.INVISIBLE);
        }

        // 书本列表点击事件
        bookList.setOnItemClickListener((parent, view, position, id) -> {
            externalBookList = localBooks.getData().get(position);
            File readHistory = new File(documentsBaseDir + "/Bookshelf/" + localBooks.getData().get(position).getBook_info().getBook_id() + "/ReadHistory.json");
            if (!readHistory.exists()) {
                Intent chapterIntent = new Intent(MainAct.this, ChaptersAct.class);
                startActivity(chapterIntent);
            } else {
                externalBookId = localBooks.getData().get(position).getBook_info().getBook_id();
                Intent readerIntent = new Intent(MainAct.this, ReaderAct.class);
                startActivity(readerIntent);
            }
        });
    }

    public void Button_Setting_onClick(View view) {
        page = 0;
        Intent settingIntent = new Intent(this, MenuAct.class);
        startActivity(settingIntent);
    }

    public void Button_Refresh_onClick(View view) {
        Toast.makeText(this, "正在同步书架", Toast.LENGTH_SHORT).show();
        Thread thread = new Thread(() -> {
            List<String> booksList = new ArrayList<>();
            bookshelfIndex localBooks_temp = catAPI.GetBooksOnBookshelf();
            if (Objects.equals(localBooks_temp, null)) {
                return;
            }
            for(int i = 0; i < (long) localBooks_temp.getData().size(); i++) {
                booksList.add(localBooks_temp.getData().get(i).getBook_info().getBook_name());
                locAPI.AddBookOnBookshelf(localBooks_temp.getData().get(i));
            }
            Message msg = Message.obtain();
            msg.obj = booksList;
            syncBookshelf.sendMessage(msg);
        });
        thread.setName("Button_Refresh_onClick");
        thread.start();
    }
}