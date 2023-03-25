package com.moling.wearnovel.catAPI.book;

import static com.moling.wearnovel.catAPI.request.request.*;

import com.moling.wearnovel.catAPI.request.UrlConstants;

import okhttp3.FormBody;
import okhttp3.Request;

public class api {
    public static String GET_BOOK_INFORMATION(String bid) {
        FormBody body = PostBuilder().add("book_id", bid).build();
        Request request = new Request.Builder()
                .url(UrlConstants.WEB_SITE + UrlConstants.BOOK_GET_INFO_BY_ID)
                .post(body).build();
        return Post(request);
    }

    public static String GET_DIVISION_LIST_BY_BOOKID(String bid) {
        FormBody body = PostBuilder().add("book_id", bid).build();
        Request request = new Request.Builder()
                .url(UrlConstants.WEB_SITE + UrlConstants.GET_DIVISION_LIST_NEW)
                .post(body).build();
        return Post(request);
    }

    public static String GET_KET_BY_CHAPTER_ID(String chapter_id) {
        FormBody body = PostBuilder().add("chapter_id", chapter_id).build();
        Request request = new Request.Builder()
                .url(UrlConstants.WEB_SITE + UrlConstants.GET_CHAPTER_COMMAND)
                .post(body).build();
        return Post(request);
    }

    public static String GET_CHAPTER_CONTENT(String chapter_id, String chapter_key) {
        FormBody body = PostBuilder()
                .add("chapter_id", chapter_id)
                .add("chapter_command", chapter_key)
                .build();
        Request request = new Request.Builder()
                .url(UrlConstants.WEB_SITE + UrlConstants.GET_CPT_IFM)
                .post(body).build();
        return Post(request);
    }
}
