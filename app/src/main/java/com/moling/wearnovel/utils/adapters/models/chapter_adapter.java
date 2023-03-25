package com.moling.wearnovel.utils.adapters.models;

public class chapter_adapter {
    private String chapter_id;
    private String chapter_title;
    private String word_count;
    private int auth_access;
    public chapter_adapter(String chapter_id, String chapter_title, String word_count, int auth_access) {
        this.chapter_id = chapter_id;
        this.chapter_title = chapter_title;
        this.word_count = word_count;
        this.auth_access = auth_access;
    }

    public String getChapter_id() {
        return chapter_id;
    }
    public String getChapter_title() {
        return chapter_title;
    }
    public String getWord_count() {
        return word_count;
    }
    public int getAuth_access() {
        return auth_access;
    }
}
