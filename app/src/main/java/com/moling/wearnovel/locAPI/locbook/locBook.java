package com.moling.wearnovel.locAPI.locbook;

import static com.moling.wearnovel.catAPI.catAPI.GET_CHAPTER_CONTENT;
import static com.moling.wearnovel.catAPI.catAPI.GET_KET_BY_CHAPTER_ID;
import static com.moling.wearnovel.utils.filebuffer.fileBuffer.*;
import static com.moling.wearnovel.utils.unmarshal.unmarshal.*;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.moling.wearnovel.MainAct;
import com.moling.wearnovel.utils.templates.bookshelfIndex.book_list;
import com.moling.wearnovel.utils.templates.chapterContent;
import com.moling.wearnovel.utils.templates.chapterKey;
import com.moling.wearnovel.utils.templates.divisionList.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import CatAESDecryptor.CatAESDecryptor;

public class locBook {
    public static void InitBookFolder(book_list book) {
        // 创建书籍文件夹
        File bookDir = new File(MainAct.documentsBaseDir + "/Bookshelf/" + book.getBook_info().getBook_id());
        if(!bookDir.exists()) {
            bookDir.mkdirs();
        }
        // 创建章节文件夹
        File bookChaptersDir = new File(MainAct.documentsBaseDir + "/Bookshelf/" + book.getBook_info().getBook_id() + "/chapters");
        if(!bookChaptersDir.exists()) {
            bookChaptersDir.mkdirs();
        }
        // 如果不存在 BookIndex.json 文件,则创建
        File bookIndexFile = new File(MainAct.documentsBaseDir + "/Bookshelf/" + book.getBook_info().getBook_id() + "/BookIndex.json");
        if(!bookIndexFile.exists()) {
            JSONObject data = new JSONObject();
            data.put("chapter_list", new JSONArray());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 100000);
            jsonObject.put("data", data);
            try {
                FileWriter fw = new FileWriter(bookIndexFile);
                fw.write(jsonObject.toJSONString());
                fw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static divisionList GetBookIndex(String book_id) {
        File bookIndexFile = new File(MainAct.documentsBaseDir + "/Bookshelf/" + book_id + "/BookIndex.json");
        String bookIndexJson = bufferRead(bookIndexFile);
        return UnmarshalDivisionList(bookIndexJson);
    }

    public static void AddDivisionToIndex(String book_id, division division) {
        File bookIndexFile = new File(MainAct.documentsBaseDir + "/Bookshelf/" + book_id + "/BookIndex.json");
        String bookIndexJson = bufferRead(bookIndexFile);
        JSONObject bookIndexJsonObj = JSON.parseObject(bookIndexJson);

        JSONArray bookDivisionList = bookIndexJsonObj.getJSONObject("data").getJSONArray("chapter_list");
        //Log.d("[locBook]|[bookDivisionList]", bookDivisionList.toJSONString());

        boolean divisionExists = false;
        for (int i = 0; i < (long) bookDivisionList.size(); i++) {
            //Log.d("[locBook]", bookDivisionList.getJSONObject(i).getString("division_id") + "><" + division.getDivision_id());
            if (Objects.equals(bookDivisionList.getJSONObject(i).getString("division_id"), division.getDivision_id())) {
                divisionExists = true;
                break;
            }
        }
        //Log.d("Result", division.getDivision_id() + " | " + divisionExists);

        if (!divisionExists) {
            JSONObject newDivision = new JSONObject();

            newDivision.put("chapter_list", new JSONArray());
            newDivision.put("max_update_time", division.getMax_update_time());
            newDivision.put("max_chapter_index", division.getMax_chapter_index());
            newDivision.put("division_id", division.getDivision_id());
            newDivision.put("division_index", division.getDivision_index());
            newDivision.put("division_name", division.getDivision_name());

            //Log.d("[locBook]|[newDivision]", newDivision.toJSONString());
            bookDivisionList.add(newDivision);

            //Log.d("[locBook]|[bookIndexJsonObj]", bookIndexJsonObj.toJSONString());
            bufferSave(bookIndexFile, bookIndexJsonObj.toJSONString());
        }
    }

    public static void AddChapterToDivision(String book_id, String division_id, chapter chapter) {
        File bookIndexFile = new File(MainAct.documentsBaseDir + "/Bookshelf/" + book_id + "/BookIndex.json");
        String bookIndexJson = bufferRead(bookIndexFile);
        JSONObject bookIndexJsonObj = JSON.parseObject(bookIndexJson);

        JSONArray bookDivisionList = bookIndexJsonObj.getJSONObject("data").getJSONArray("chapter_list");
        //Log.d("[locBook]|[bookDivisionList]", bookDivisionList.toJSONString());

        int divisionIndex = 0;
        for (int i = 0; i < (long) bookDivisionList.size(); i++) {
            //Log.d("[locBook]", bookDivisionList.getJSONObject(i).getString("division_id") + "><" + division_id);
            if (Objects.equals(bookDivisionList.getJSONObject(i).getString("division_id"), division_id)) {
                divisionIndex = i;
                break;
            }
        }

        JSONArray divisionChapterList = bookDivisionList.getJSONObject(divisionIndex).getJSONArray("chapter_list");
        boolean chapterExists = false;
        int chapterIndex = 0;
        for (int i = 0; i < (long) divisionChapterList.size(); i++) {
            //Log.d("[locBook]", divisionChapterList.getJSONObject(i).getString("chapter_id") + "><" + chapter.getChapter_id());
            if (Objects.equals(divisionChapterList.getJSONObject(i).getString("chapter_id"), chapter.getChapter_id())) {
                chapterExists = true;
                chapterIndex = i;
                break;
            }
        }
        //Log.d("Result", chapter.getChapter_id() + " | " + chapterExists);

        if (!chapterExists) {
            JSONObject newChapter = new JSONObject();

            newChapter.put("chapter_id", chapter.getChapter_id());
            newChapter.put("chapter_index", chapter.getChapter_index());
            newChapter.put("chapter_title", chapter.getChapter_title());
            newChapter.put("word_count", chapter.getWord_count());
            newChapter.put("tsukkomi_amount", chapter.getTsukkomi_amount());
            newChapter.put("is_paid", chapter.getIs_paid());
            newChapter.put("mtime", chapter.getMtime());
            newChapter.put("is_valid", chapter.getIs_valid());
            newChapter.put("auth_access", chapter.getAuth_access());

            //Log.d("[locBook]|[newChapter]", newChapter.toJSONString());
            divisionChapterList.add(newChapter);

            //Log.d("[locBook]|[bookIndexJsonObj]", bookIndexJsonObj.toJSONString());
            bufferSave(bookIndexFile, bookIndexJsonObj.toJSONString());
        } else {
            JSONObject existsChapter = divisionChapterList.getJSONObject(chapterIndex);

            existsChapter.put("chapter_id", chapter.getChapter_id());
            existsChapter.put("chapter_index", chapter.getChapter_index());
            existsChapter.put("chapter_title", chapter.getChapter_title());
            existsChapter.put("word_count", chapter.getWord_count());
            existsChapter.put("tsukkomi_amount", chapter.getTsukkomi_amount());
            existsChapter.put("is_paid", chapter.getIs_paid());
            existsChapter.put("mtime", chapter.getMtime());
            existsChapter.put("is_valid", chapter.getIs_valid());
            existsChapter.put("auth_access", chapter.getAuth_access());

            bufferSave(bookIndexFile, bookIndexJsonObj.toJSONString());
        }
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

    public static String[] SplitChapterContent(String content, int length) {
        int n = (content.length() + length - 1) / length; //获取整个字符串可以被切割成字符子串的个数
        String[] split = new String[n];
        for (int i = 0; i < n; i++) {
            if (i < (n - 1)) {
                split[i] = content.substring(i * length, (i + 1) * length);
                if (Objects.equals(split[i].substring(0, 1), "\n")) {
                    split[i] = split[i].substring(1);
                }
            } else {
                split[i] = content.substring(i * length);
            }
        }
        return split;
    }
}
