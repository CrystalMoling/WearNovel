package com.moling.wearnovel.locAPI;

import static com.moling.wearnovel.catAPI.catAPI.GET_CHAPTER_CONTENT;
import static com.moling.wearnovel.catAPI.catAPI.GET_KET_BY_CHAPTER_ID;
import static com.moling.wearnovel.utils.filebuffer.fileBuffer.bufferSave;
import static com.moling.wearnovel.utils.unmarshal.unmarshal.UnmarshalChapterContent;
import static com.moling.wearnovel.utils.unmarshal.unmarshal.UnmarshalChapterKey;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.moling.wearnovel.MainAct;
import com.moling.wearnovel.locAPI.bookshelf.bookshelf;
import com.moling.wearnovel.locAPI.locbook.locBook;
import com.moling.wearnovel.utils.templates.bookshelfIndex.book_list;
import com.moling.wearnovel.utils.templates.bookshelfIndex.bookshelfIndex;
import com.moling.wearnovel.utils.templates.chapterContent;
import com.moling.wearnovel.utils.templates.chapterKey;
import com.moling.wearnovel.utils.templates.divisionList.chapter;
import com.moling.wearnovel.utils.templates.divisionList.division;
import com.moling.wearnovel.utils.templates.divisionList.divisionList;

import java.io.File;
import java.util.Objects;

import CatAESDecryptor.CatAESDecryptor;

public class locAPI {

    public static void InitBookFolder(book_list book) {
        locBook.InitBookFolder(book);
    }

    public static bookshelfIndex GetBooksOnBookshelf() {
        return bookshelf.GetBooksOnBookshelf();
    }

    public static void AddBookOnBookshelf(book_list book) {
        bookshelf.AddBookOnBookShelf(book);
    }

    public static divisionList GetBookIndex(String book_id) {
        return locBook.GetBookIndex(book_id);
    }

    public static void AddDivisionToIndex(String book_id, division division) {
        locBook.AddDivisionToIndex(book_id, division);
    }

    public static void AddChapterToDivision(String book_id, String division_id, chapter chapter) {
        locBook.AddChapterToDivision(book_id, division_id, chapter);
    }

    public static void DownloadChapterContent(String book_id, String chapter_id, boolean cover, boolean sync, Handler handler) {
        Log.d("[locAPI]", "Downloading Book:[" + book_id + "], Chapter:[" + chapter_id + "]");
        File chapter_file = new File(MainAct.documentsBaseDir + "/Bookshelf/" + book_id + "/chapters/" + chapter_id + ".json");
        if (!chapter_file.exists() || cover) {
            Log.d("[locAPI]", "Book:[" + book_id + "], Chapter:[" + chapter_id + "] is not Exists");
            Thread thread = new Thread(() -> {
                // 当前本地书籍文件
                File chapterFile = new File(MainAct.documentsBaseDir + "/Bookshelf/" + book_id + "/chapters/" + chapter_id + ".json");

                String chapter_key_resp = GET_KET_BY_CHAPTER_ID(chapter_id);
                Log.d("[locAPI]", "chapter_key_resp[" + chapter_key_resp + "]");
                if (Objects.equals(chapter_key_resp, null)) {
                    return;
                }
                chapterKey chapter_key = UnmarshalChapterKey(chapter_key_resp);
                // 章节 JSON
                String chapterContentJson= GET_CHAPTER_CONTENT(chapter_id, chapter_key.getCommand());

                JSONObject chapterContentJsonObj = JSON.parseObject(chapterContentJson);
                chapterContent chapter_content = UnmarshalChapterContent(chapterContentJson);

                // 解码章节内容
                String chapterContent = new String(CatAESDecryptor.decode(chapter_content.getTxt_content(), chapter_key.getCommand()));

                chapterContentJsonObj.getJSONObject("data").getJSONObject("chapter_info").put("txt_content", chapterContent);
                // 保存明文章节内容
                bufferSave(chapterFile, chapterContentJsonObj.toJSONString());

                // 同步章节内容
                if (sync) {
                    Message msg = Message.obtain();
                    msg.obj = chapterContent;
                    handler.sendMessage(msg);
                }
            });
            thread.setName("DownloadChapterContent");
            thread.start();
        }
    }
}
