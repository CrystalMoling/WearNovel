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
        locBook.DownloadChapterContent(book_id, chapter_id, cover, sync, handler);
    }

    public static String[] SplitChapterContent(String content, int length) {
        return locBook.SplitChapterContent(content, length);
    }
}
