package com.moling.wearnovel.locAPI.bookshelf;

import static com.moling.wearnovel.utils.filebuffer.fileBuffer.*;
import static com.moling.wearnovel.utils.unmarshal.unmarshal.UnmarshalBookshelfIndex;

import android.util.Log;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.moling.wearnovel.MainAct;
import com.moling.wearnovel.utils.templates.bookshelfIndex.book_list;
import com.moling.wearnovel.utils.templates.bookshelfIndex.bookshelfIndex;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class bookshelf {
    public static File InitBookShelf() {
        // 如果不存在 Bookshelf 目录,则创建
        File bookshelfDir = new File(MainAct.documentsBaseDir + "/Bookshelf");
        if(!bookshelfDir.exists()) {
            bookshelfDir.mkdirs();
        }
        // 如果不存在 Bookshelf.json 文件,则创建
        File bookshelfFile = new File(MainAct.documentsBaseDir + "/Bookshelf.json");
        if(!bookshelfFile.exists()) {
            JSONObject data = new JSONObject();
            data.put("book_list", new JSONArray());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 100000);
            jsonObject.put("data", data);
            try {
                FileWriter fw = new FileWriter(bookshelfFile);
                fw.write(jsonObject.toJSONString());
                fw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return bookshelfFile;
    }

    public static bookshelfIndex GetBooksOnBookshelf() {
        String bookshelfJson = bufferRead(InitBookShelf());
        return UnmarshalBookshelfIndex(bookshelfJson);
    }

    public static void AddBookOnBookShelf(book_list book_list) {
        File bookshelfFile = InitBookShelf();

        String bookshelfJson = bufferRead(bookshelfFile);
        JSONObject bookshelfObj = JSON.parseObject(bookshelfJson);
        bookshelfIndex bookshelfIndexObj = UnmarshalBookshelfIndex(bookshelfJson);

        boolean exists = false;
        for (int i = 0; i < (long) bookshelfIndexObj.getData().size(); i++) {
            Log.d("Book", bookshelfIndexObj.getData().get(i).getBook_info().getBook_id() + "><" + book_list.getBook_info().getBook_id());
            if(Objects.equals(bookshelfIndexObj.getData().get(i).getBook_info().getBook_id(), book_list.getBook_info().getBook_id())) {
                exists = true;
                break;
            }
        }
        Log.d("Book", String.valueOf(exists));

        if (!exists) {
            JSONArray book_list_array = bookshelfObj.getJSONObject("data").getJSONArray("book_list");

            JSONObject book_info = new JSONObject();
            book_info.put("book_id", book_list.getBook_info().getBook_id());
            book_info.put("book_name", book_list.getBook_info().getBook_name());
            book_info.put("is_original", book_list.getBook_info().getIs_original());
            book_info.put("category_index", book_list.getBook_info().getCategory_index());
            book_info.put("total_word_count", book_list.getBook_info().getTotal_word_count());
            book_info.put("review_amount", book_list.getBook_info().getReview_amount());
            book_info.put("cover", book_list.getBook_info().getCover());
            book_info.put("discount", book_list.getBook_info().getDiscount());
            book_info.put("discount_end_time", book_list.getBook_info().getDiscount_end_time());
            book_info.put("author_name", book_list.getBook_info().getAuthor_name());
            book_info.put("uptime", book_list.getBook_info().getUptime());
            book_info.put("update_status", book_list.getBook_info().getUpdate_status());

            JSONObject data = new JSONObject();
            data.put("is_buy", book_list.getIs_buy());
            data.put("book_info", book_info);
            data.put("mod_time", book_list.getMod_time());
            data.put("last_read_chapter_id", book_list.getLast_read_chapter_id());
            data.put("last_read_chapter_update_time", book_list.getLast_read_chapter_update_time());
            data.put("is_notify", book_list.getIs_notify());

            book_list_array.add(data);

            bufferSave(bookshelfFile, bookshelfObj.toJSONString());

            //InitBookFolder(book_list);
        }
    }
}
