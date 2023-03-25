package com.moling.wearnovel;

import static com.moling.wearnovel.locAPI.locAPI.*;
import static com.moling.wearnovel.utils.filebuffer.fileBuffer.*;
import static com.moling.wearnovel.utils.unmarshal.unmarshal.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.moling.wearnovel.utils.templates.*;
import com.moling.wearnovel.utils.templates.divisionList.chapter;
import com.moling.wearnovel.utils.templates.divisionList.divisionList;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ReaderAct extends Activity {
    public static ReaderAct ReadAct;
    static File readHistoryFile;
    static String externalBookId;
    static Handler syncContent = null;

    static JSONObject read_history_obj;

    static String externalChapterId;
    static int externalPage;

    static String chapterContent;

    TextView headerText;
    TextView contentText;
    static divisionList division_list;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader);
        ReadAct = this;

        // 当前书籍 ID
        externalBookId = MainAct.externalBookId;

        // 当前阅读历史文件
        readHistoryFile = new File(MainAct.documentsBaseDir + "/Bookshelf/" + externalBookId + "/ReadHistory.json");
        read_history_obj = JSON.parseObject(bufferRead(readHistoryFile));

        // 当前章节 Id
        externalChapterId = read_history_obj.getString("last_read_chapter_id");
        // 当前阅读页码
        externalPage = read_history_obj.getInteger("last_read_page");

        // 当前书籍章节列表
        division_list = GetBookIndex(externalBookId);

        // 界面对象
        headerText = (TextView) findViewById(R.id.text_header);
        contentText = (TextView) findViewById(R.id.text_content);

        //headerText.setText(new SimpleDateFormat("HH:mm").format(new Date()));

        // Header 点击事件
        headerText.setClickable(true);
        headerText.setOnClickListener(view -> {
            int external_chapter_index = division_list.findChapter(externalChapterId);
            List<chapter> chapter_list = division_list.toChapterList();
            Toast.makeText(ReaderAct.this, "当前章节:\n" + chapter_list.get(external_chapter_index).getChapter_title(), Toast.LENGTH_SHORT).show();
        });

        // 正文长按事件
        contentText.setOnLongClickListener(view -> {
            Intent readerIntent = new Intent(ReaderAct.this, ChaptersAct.class);
            startActivity(readerIntent);
            return true;
        });

        // 同步章节内容 Handler
        syncContent = new Handler(){
            @SuppressLint("SetTextI18n")
            public void handleMessage(Message msg){
                if (!Objects.equals(msg.obj, null)) {
                    chapterContent = (String) msg.obj;
                }
                // 分割章节正文
                int n = (chapterContent.length() + 72 - 1) / 72; //获取整个字符串可以被切割成字符子串的个数
                String[] split = new String[n];
                for (int i = 0; i < n; i++) {
                    if (i < (n - 1)) {
                        split[i] = chapterContent.substring(i * 72, (i + 1) * 72);
                    } else {
                        split[i] = chapterContent.substring(i * 72);
                    }
                }
                // 保存章节页数
                read_history_obj.put("total_pages", n);
                bufferSave(readHistoryFile, read_history_obj.toJSONString());

                // 显示章节正文
                int last_read_page = read_history_obj.getInteger("last_read_page");
                if (last_read_page < 0) { last_read_page = 0; }
                else if (last_read_page > split.length - 1) { last_read_page = split.length - 1; }
                contentText.setText(split[last_read_page]);

                // 设置 Header
                headerText.setText(new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date()) + "\n" + (last_read_page + 1) + "/" + n);
            }
        };

        // 获取章节正文
        GetChapterContent(externalBookId, externalChapterId);
    }

    private static void GetChapterContent(String book_id, String chapter_id) {
        try {
            // 当前本地章节文件
            File externalChapterFile = new File(MainAct.documentsBaseDir + "/Bookshelf/" + book_id + "/chapters/" + chapter_id + ".json");
            // 尝试从本地读取章节内容
            chapterContent chapter_content = UnmarshalChapterContent(bufferRead(externalChapterFile));
            String txt_content = chapter_content.getTxt_content();
            // 同步章节内容
            Message msg = Message.obtain();
            msg.obj = txt_content;
            syncContent.sendMessage(msg);
        } catch (Exception e) {
            int external_chapter_index = division_list.findChapter(chapter_id);
            List<chapter> chapter_list = division_list.toChapterList();
            // 下载本章
            DownloadChapterContent(externalBookId, externalChapterId, false, true, syncContent);
            // 下载上一章
            String previous_chapter_id = chapter_list.get(external_chapter_index - 1).getChapter_id();
            DownloadChapterContent(externalBookId, previous_chapter_id, false, false, null);
            // 下载下一章
            String following_chapter_id = chapter_list.get(external_chapter_index + 1).getChapter_id();
            DownloadChapterContent(externalBookId, following_chapter_id, false, false, null);
        }
    }

    public void PreviousButton_onClick(View view) {
        int external_chapter_index = division_list.findChapter(externalChapterId);
        if (external_chapter_index == 0) {
            Toast.makeText(this, "已经是最前一章", Toast.LENGTH_SHORT).show();
        } else {
            int external_page = read_history_obj.getInteger("last_read_page");
            if (external_page == 0) {
                String previous_chapter_id = division_list.toChapterList().get(external_chapter_index - 1).getChapter_id();
                // 上一章节本地文件
                File externalChapterFile = new File(MainAct.documentsBaseDir + "/Bookshelf/" + externalBookId + "/chapters/" + previous_chapter_id + ".json");
                chapterContent chapter_content = UnmarshalChapterContent(bufferRead(externalChapterFile));
                int previous_chapter_total_page = ((chapter_content.getTxt_content().length() + 72 - 1) / 72) - 1;

                GetChapterContent(externalBookId, previous_chapter_id);
                externalChapterId = previous_chapter_id;
                read_history_obj.put("last_read_chapter_id", previous_chapter_id);
                read_history_obj.put("last_read_page", previous_chapter_total_page);

                bufferSave(readHistoryFile, read_history_obj.toJSONString());
            } else {
                read_history_obj.put("last_read_page", external_page - 1);

                bufferSave(readHistoryFile, read_history_obj.toJSONString());
            }
        }

        Message msg = Message.obtain();
        syncContent.sendMessage(msg);
    }

    public void FollowingButton_onClick(View view) {
        int external_chapter_index = division_list.findChapter(externalChapterId);
        if (external_chapter_index == division_list.toChapterList().size() - 1) {
            Toast.makeText(this, "已经是最后一章", Toast.LENGTH_SHORT).show();
        } else {
            int external_page = read_history_obj.getInteger("last_read_page");
            if (external_page == read_history_obj.getInteger("total_pages") - 1) {
                GetChapterContent(externalBookId, division_list.toChapterList().get(external_chapter_index + 1).getChapter_id());
                externalChapterId = division_list.toChapterList().get(external_chapter_index + 1).getChapter_id();
                read_history_obj.put("last_read_chapter_id", division_list.toChapterList().get(external_chapter_index + 1).getChapter_id());
                read_history_obj.put("last_read_page", 0);

                bufferSave(readHistoryFile, read_history_obj.toJSONString());
            } else {
                read_history_obj.put("last_read_page", external_page + 1);

                bufferSave(readHistoryFile, read_history_obj.toJSONString());
            }
        }

        Message msg = Message.obtain();
        syncContent.sendMessage(msg);
    }
}
