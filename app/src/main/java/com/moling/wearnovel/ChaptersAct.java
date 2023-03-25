package com.moling.wearnovel;

import static com.moling.wearnovel.catAPI.catAPI.*;
import static com.moling.wearnovel.locAPI.locAPI.*;
import static com.moling.wearnovel.utils.constants.*;
import static com.moling.wearnovel.utils.filebuffer.fileBuffer.bufferSave;
import static com.moling.wearnovel.utils.unmarshal.unmarshal.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson2.JSONObject;
import com.moling.wearnovel.utils.adapters.chapterAdapter;
import com.moling.wearnovel.utils.adapters.models.chapter_adapter;
import com.moling.wearnovel.utils.templates.bookshelfIndex.book_list;
import com.moling.wearnovel.utils.templates.divisionList.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChaptersAct extends Activity {
    public static Activity ctx;
    List<String> id_list;
    book_list externalBookList;
    int externalChapterIndex;
    Handler addOption = null;
    Handler progress = null;
    ListView chapterList;
    Button backButton;
    LinearLayout progressLayout;
    TextView progressText;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapters);
        ctx = this;

        // 界面对象
        chapterList = (ListView) findViewById(R.id.list_chapter);
        progressLayout = (LinearLayout) findViewById(R.id.layout_progress);
        progressText = (TextView) findViewById(R.id.text_progress);

        progressLayout.setVisibility(View.GONE);

        // 向 ListView 添加 Header
        LayoutInflater infla = LayoutInflater.from(this);
        View headview = infla.inflate(R.layout.widget_title, null);
        backButton = (Button) headview.findViewById(R.id.button_back);
        chapterList.addHeaderView(backButton,null,true);

        // 设置标题文本
        backButton.setText("卷");

        // 返回按钮点击事件
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainAct.chapterPage == 1) {
                    MainAct.chapterPage = 0;
                    Intent intent = new Intent(ctx, ChaptersAct.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // 当前 BookList 对象
        externalBookList = MainAct.externalBookList;
        // 当前卷 Index
        externalChapterIndex = MainAct.externalChapterIndex;

        // 初始化书籍目录
        InitBookFolder(externalBookList);

        // 同步书架 Handler
        addOption = new Handler(){
            public void handleMessage(Message msg){
                if (msg.arg1 != 1) {
                    List<String> bookL = (List<String>) msg.obj;
                    if ((long) bookL.size() != 0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ChaptersAct.this,android.R.layout.simple_list_item_1,bookL);
                        chapterList.setAdapter(adapter);
                        if (msg.what == 1) {
                            backButton.setText("卷");
                        }
                    }
                } else {
                    List<chapter_adapter> chapterAdapterList = (List<chapter_adapter>) msg.obj;
                    chapterAdapter adapter = new chapterAdapter(getLayoutInflater(), chapterAdapterList, externalBookList.getBook_info().getBook_id());
                    chapterList.setAdapter(adapter);
                    if (msg.what == 1) {
                        backButton.setText("卷");
                    }
                }
            }
        };

        // 进度条 Handler
        progress = new Handler(){
            public void handleMessage(Message msg){
                if (msg.what == 1) {
                    progressLayout.setVisibility(View.VISIBLE);
                    progressText.setText((String) msg.obj);
                } else {
                    progressLayout.setVisibility(View.GONE);
                }
            }
        };

        // 尝试从本地读取章节
        try {
            List<String> divisionList = new ArrayList<>();
            id_list = new ArrayList<>();
            Message msg = Message.obtain();
            divisionList division_list = GetBookIndex(externalBookList.getBook_info().getBook_id());
            if (division_list.getData().size() != 0) {
                if(MainAct.chapterPage == 0){ // 卷视图
                    for (int i = 0; i < (long) division_list.getData().size(); i++) {
                        divisionList.add(division_list.getData().get(i).getDivision_name());
                        id_list.add(division_list.getData().get(i).getDivision_id());
                    }
                    msg.obj = divisionList;
                    addOption.sendMessage(msg);
                } else if (MainAct.chapterPage == 1) { // 章节视图
                    backButton.setText(division_list.getData().get(externalChapterIndex).getDivision_name());
                    List<chapter_adapter> chapterAdapterList = new ArrayList<>();
                    for (int i = 0; i < (long) division_list.getData().get(externalChapterIndex).getChapter_list().size(); i++) {
                        chapterAdapterList.add(new chapter_adapter(
                                division_list.getData().get(externalChapterIndex).getChapter_list().get(i).getChapter_id(),
                                division_list.getData().get(externalChapterIndex).getChapter_list().get(i).getChapter_title(),
                                division_list.getData().get(externalChapterIndex).getChapter_list().get(i).getWord_count(),
                                division_list.getData().get(externalChapterIndex).getChapter_list().get(i).getAuth_access()
                        ));
                        //divisionList.add(division_list.getData().get(externalChapterIndex).getChapter_list().get(i).getChapter_title());
                        id_list.add(division_list.getData().get(externalChapterIndex).getChapter_list().get(i).getChapter_id());
                    }
                    msg.arg1 = 1;
                    msg.obj = chapterAdapterList;
                    addOption.sendMessage(msg);
                }
            } else {
                Refresh_Division_list();
            }
        } catch (Exception e) {
            Message toastMsg = Message.obtain();
            toastMsg.obj = e.getMessage();
            MainAct.callToast.sendMessage(toastMsg);
        }

        // 章节列表点击事件
        chapterList.setOnItemClickListener((parent, view, position, id) -> {
            if (MainAct.chapterPage == CHAPTER_PAGE_DIVISION) {
                MainAct.chapterPage = CHAPTER_PAGE_CHAPTER; // 切换为章节视图
                MainAct.externalChapterIndex = position - 1;
                Intent intent = new Intent(this, ChaptersAct.class);
                startActivity(intent);
                finish();
            } else if (MainAct.chapterPage == CHAPTER_PAGE_CHAPTER) {
                File readHistory = new File(MainAct.documentsBaseDir + "/Bookshelf/" + externalBookList.getBook_info().getBook_id() + "/ReadHistory.json");
                divisionList division_list = GetBookIndex(externalBookList.getBook_info().getBook_id());
                JSONObject readHistoryJson = new JSONObject();

                readHistoryJson.put("last_read_chapter_id", division_list.getData().get(externalChapterIndex).getChapter_list().get(position - 1).getChapter_id());
                readHistoryJson.put("last_read_page", 0);

                bufferSave(readHistory, readHistoryJson.toJSONString());

                MainAct.externalBookId = externalBookList.getBook_info().getBook_id();

                try {
                    ReaderAct.ReadAct.finish();
                } catch (Exception e) { }

                Intent readerIntent = new Intent(this, ReaderAct.class);
                startActivity(readerIntent);
                finish();
            }
        });

        // 章节列表长按事件
        chapterList.setOnItemLongClickListener((adapterView, view, i, l) -> {
            MainAct.menuTitle = chapterList.getAdapter().getItem(i).toString();
            MainAct.menuArgs = new String[]{id_list.get(i - 1)};
            if (MainAct.chapterPage == CHAPTER_PAGE_DIVISION) {
                MainAct.page = MENU_PAGE_DIVISION;
                Intent menuIntent = new Intent(ctx, MenuAct.class);
                startActivity(menuIntent);
            } else if (MainAct.chapterPage == CHAPTER_PAGE_CHAPTER) {
                MainAct.page = MENU_PAGE_CHAPTER;
                Intent menuIntent = new Intent(ctx, MenuAct.class);
                startActivity(menuIntent);
            }
            return true;
        });
    }

    public void Button_Refresh_onClick(View view) {
        Refresh_Division_list();
    }

    private void Refresh_Division_list() {
        MainAct.catAccount = MainAct.sharedPreferences.getString("Account", "");
        MainAct.catToken = MainAct.sharedPreferences.getString("Token", "");
        MainAct.catAppVer = MainAct.sharedPreferences.getString("AppVer", "");
        Log.d("[ChaptersAct]", "Refreshing division list...");
        Thread thread = new Thread(() -> {
            try {
                String divisionListJson = GET_DIVISION_LIST_BY_BOOKID(externalBookList.getBook_info().getBook_id());
                Log.d("[ChaptersAct]", "divisionListJson[" + divisionListJson + "]");
                if (Objects.equals(divisionListJson, null)) {
                    return;
                }
                divisionList division_list = UnmarshalDivisionList(divisionListJson);

                Message toastMsg = Message.obtain();
                toastMsg.obj = "远程目录拉取完成,正在同步至本地书架";
                MainAct.callToast.sendMessage(toastMsg);

                int total_chapter_count = division_list.toChapterList().size();
                int processed_chapter_count = 0;

                for (int i = 0; i < (long) division_list.getData().size(); i++) {
                    AddDivisionToIndex(externalBookList.getBook_info().getBook_id(), division_list.getData().get(i));
                    for (int j = 0; j < (long) division_list.getData().get(i).getChapter_list().size(); j++) {
                        AddChapterToDivision(
                                externalBookList.getBook_info().getBook_id(),
                                division_list.getData().get(i).getDivision_id(),
                                division_list.getData().get(i).getChapter_list().get(j)
                        );
                        processed_chapter_count += 1;
                        // 显示进度条
                        Message progressMsg = Message.obtain();
                        progressMsg.what = 1;
                        progressMsg.obj = processed_chapter_count + "/" + total_chapter_count;
                        progress.sendMessage(progressMsg);
                    }
                }

                List<String> itemsList = new ArrayList<>();
                for (int i = 0; i < (long) division_list.getData().size(); i++) {
                    itemsList.add(division_list.getData().get(i).getDivision_name());
                }

                // 更新列表
                Message objMsg = Message.obtain();
                objMsg.what = 1;
                objMsg.obj = itemsList;
                addOption.sendMessage(objMsg);

                // Toast 消息
                toastMsg = Message.obtain();
                toastMsg.obj = "同步完成";
                MainAct.callToast.sendMessage(toastMsg);

                // 隐藏进度条
                Message progressMsg = Message.obtain();
                progressMsg.what = 0;
                progress.sendMessage(progressMsg);
            } catch (RuntimeException e) {
                Log.d("[ChaptersAct]", "Refresh failed: [" + e + "]");
                Message toastMsg = Message.obtain();
                toastMsg.obj = e.getMessage();
                MainAct.callToast.sendMessage(toastMsg);
            }
        });
        thread.setName("Refresh_Division_list");
        thread.start();
    }
}
