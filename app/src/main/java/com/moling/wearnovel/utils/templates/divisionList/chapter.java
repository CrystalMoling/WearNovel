package com.moling.wearnovel.utils.templates.divisionList;

public class chapter {
    private String chapter_id;
    private String chapter_index;
    private String chapter_title;
    private String word_count;
    private String tsukkomi_amount;
    private String is_paid;
    private String mtime;
    private int is_valid;
    private int auth_access;

    public chapter(String chapter_id, String chapter_index, String chapter_title, String word_count,
                   String tsukkomi_amount, String is_paid, String mtime, int is_valid, int auth_access) {
        this.chapter_id = chapter_id;
        this.chapter_index = chapter_index;
        this.chapter_title = chapter_title;
        this.word_count = word_count;
        this.tsukkomi_amount = tsukkomi_amount;
        this.is_paid = is_paid;
        this.mtime = mtime;
        this.is_valid = is_valid;
        this.auth_access = auth_access;
    }

    public String getChapter_id() {
        return chapter_id;
    }
    public String getChapter_index() {
        return chapter_index;
    }
    public String getChapter_title() {
        return chapter_title;
    }
    public String getWord_count() {
        return word_count;
    }
    public String getTsukkomi_amount() {
        return tsukkomi_amount;
    }
    public String getIs_paid() {
        return is_paid;
    }
    public String getMtime() {
        return mtime;
    }
    public int getIs_valid() {
        return is_valid;
    }
    public int getAuth_access() {
        return auth_access;
    }
}
