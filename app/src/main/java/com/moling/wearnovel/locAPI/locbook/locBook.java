package com.moling.wearnovel.locAPI.locbook;

import static com.moling.wearnovel.utils.filebuffer.fileBuffer.*;
import static com.moling.wearnovel.utils.unmarshal.unmarshal.*;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.moling.wearnovel.MainAct;
import com.moling.wearnovel.utils.templates.bookshelfIndex.book_list;
import com.moling.wearnovel.utils.templates.divisionList.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

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
        for (int i = 0; i < (long) divisionChapterList.size(); i++) {
            //Log.d("[locBook]", divisionChapterList.getJSONObject(i).getString("chapter_id") + "><" + chapter.getChapter_id());
            if (Objects.equals(divisionChapterList.getJSONObject(i).getString("chapter_id"), chapter.getChapter_id())) {
                chapterExists = true;
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
        }
    }
}
