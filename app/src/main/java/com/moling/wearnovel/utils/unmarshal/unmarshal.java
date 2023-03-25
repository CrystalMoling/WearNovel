package com.moling.wearnovel.utils.unmarshal;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.moling.wearnovel.utils.templates.bookshelf.*;
import com.moling.wearnovel.utils.templates.bookshelfIndex.*;
import com.moling.wearnovel.utils.templates.*;
import com.moling.wearnovel.utils.templates.divisionList.*;

import java.util.ArrayList;
import java.util.List;

public class unmarshal {

    public static bookshelf UnmarshalBookshelf(String Json) {
        JSONObject JsonObj = JSON.parseObject(Json);
        List<shelf_list> data = new ArrayList<>();
        JSONArray shelf_list = JsonObj.getJSONObject("data").getJSONArray("shelf_list");
        for(int i = 0; i < (long) shelf_list.size(); i++) {
            JSONObject shelf = shelf_list.getJSONObject(i);
            data.add(new shelf_list(
                    shelf.getString("shelf_id"),
                    shelf.getString("reader_id"),
                    shelf.getString("shelf_name"),
                    shelf.getString("shelf_index"),
                    shelf.getInteger("book_limit")
            ));
        }
        return new bookshelf(
                JsonObj.getInteger("code"),
                data
        );
    }

    public static bookshelfIndex UnmarshalBookshelfIndex(String Json) {
        JSONObject JsonObj = JSON.parseObject(Json);
        List<book_list> data = new ArrayList<>();
        JSONArray book_list = JsonObj.getJSONObject("data").getJSONArray("book_list");
        for(int i = 0; i < (long) book_list.size(); i++) {
            JSONObject book = book_list.getJSONObject(i);
            JSONObject book_info = book.getJSONObject("book_info");
            data.add(new book_list(
                    book.getString("is_buy"),
                    new book_info(
                            book_info.getString("book_id"),
                            book_info.getString("book_name"),
                            book_info.getInteger("is_original"),
                            book_info.getString("category_index"),
                            book_info.getString("total_word_count"),
                            book_info.getString("review_amount"),
                            book_info.getString("cover"),
                            book_info.getInteger("discount"),
                            book_info.getString("discount_end_time"),
                            book_info.getString("author_name"),
                            book_info.getString("uptime"),
                            book_info.getString("update_status")
                    ),
                    book.getString("mod_time"),
                    book.getString("last_read_chapter_id"),
                    book.getString("last_read_chapter_update_time"),
                    book.getString("is_notify")
            ));
        }
        return new bookshelfIndex(
                JsonObj.getInteger("code"),
                data
        );
    }

    public static divisionList UnmarshalDivisionList(String Json) {
        JSONObject JsonObj = JSON.parseObject(Json);
        List<division> data = new ArrayList<>();
        JSONArray division_list = JsonObj.getJSONObject("data").getJSONArray("chapter_list");
        for(int i = 0; i < (long) division_list.size(); i++) {
            JSONObject division = division_list.getJSONObject(i);
            JSONArray chapter_list = division.getJSONArray("chapter_list");
            List<chapter> chapterArrayList = new ArrayList<>();
            for(int j = 0; j < (long) chapter_list.size(); j++) {
                JSONObject chapter = chapter_list.getJSONObject(j);
                chapterArrayList.add(new chapter(
                        chapter.getString("chapter_id"),
                        chapter.getString("chapter_index"),
                        chapter.getString("chapter_title"),
                        chapter.getString("word_count"),
                        chapter.getString("tsukkomi_amount"),
                        chapter.getString("is_paid"),
                        chapter.getString("mtime"),
                        chapter.getInteger("is_valid"),
                        chapter.getInteger("auth_access")
                ));
            }
            data.add(new division(
                    chapterArrayList,
                    division.getInteger("max_update_time"),
                    division.getInteger("max_chapter_index"),
                    division.getString("division_id"),
                    division.getString("division_index"),
                    division.getString("division_name")
            ));
        }
        return new divisionList(
                JsonObj.getInteger("code"),
                data
        );
    }

    public static chapterKey UnmarshalChapterKey(String Json) {
        JSONObject JsonObj = JSON.parseObject(Json);
        return new chapterKey(
                JsonObj.getInteger("code"),
                JsonObj.getJSONObject("data").getString("command")
        );
    }

    public static chapterContent UnmarshalChapterContent(String Json) {
        JSONObject JsonObj = JSON.parseObject(Json);
        JSONObject chapter_info = JsonObj.getJSONObject("data").getJSONObject("chapter_info");
        return new chapterContent(
                JsonObj.getInteger("code"),
                chapter_info.getString("chapter_id"),
                chapter_info.getString("book_id"),
                chapter_info.getString("division_id"),
                chapter_info.getInteger("unit_hlb"),
                chapter_info.getString("chapter_index"),
                chapter_info.getString("chapter_title"),
                chapter_info.getString("author_say"),
                chapter_info.getString("word_count"),
                chapter_info.getString("discount"),
                chapter_info.getString("is_paid"),
                chapter_info.getInteger("auth_access"),
                chapter_info.getString("buy_amount"),
                chapter_info.getString("tsukkomi_amount"),
                chapter_info.getString("total_hlb"),
                chapter_info.getString("uptime"),
                chapter_info.getString("mtime"),
                chapter_info.getString("ctime"),
                chapter_info.getString("recommend_book_info"),
                chapter_info.getString("base_status"),
                chapter_info.getString("txt_content")
        );
    }
}
