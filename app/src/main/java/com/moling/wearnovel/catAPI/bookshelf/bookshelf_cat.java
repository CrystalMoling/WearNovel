package com.moling.wearnovel.catAPI.bookshelf;

import static com.moling.wearnovel.catAPI.request.request.Post;
import static com.moling.wearnovel.catAPI.request.request.PostBuilder;

import com.moling.wearnovel.catAPI.request.UrlConstants;

import okhttp3.FormBody;
import okhttp3.Request;

public class bookshelf_cat {
    public static String GET_BOOK_SHELF_INDEXES_INFORMATION(String shelf_id) {
        FormBody body = PostBuilder()
                .add("shelf_id", shelf_id)
                .add("direction", "prev")
                .add("last_mod_time", "0")
                .build();
        Request request = new Request.Builder()
                .url(UrlConstants.WEB_SITE + UrlConstants.BOOKSHELF_GET_SHELF_BOOK_LIST)
                .post(body).build();
        return Post(request);
    }

    public static String GET_BOOK_SHELF_INFORMATION() {
        FormBody body = PostBuilder().build();
        Request request = new Request.Builder()
                .url(UrlConstants.WEB_SITE + UrlConstants.BOOKSHELF_GET_SHELF_LIST)
                .post(body).build();
        return Post(request);
    }
}
