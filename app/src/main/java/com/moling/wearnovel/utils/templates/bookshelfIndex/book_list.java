package com.moling.wearnovel.utils.templates.bookshelfIndex;

public class book_list {
    private String is_buy;
    private com.moling.wearnovel.utils.templates.bookshelfIndex.book_info book_info;
    private String mod_time;
    private String last_read_chapter_id;
    private String last_read_chapter_update_time;
    private String is_notify;

    public book_list(String is_buy, com.moling.wearnovel.utils.templates.bookshelfIndex.book_info book_info, String mod_time, String last_read_chapter_id, String last_read_chapter_update_time, String is_notify) {
        this.is_buy = is_buy;
        this.book_info = book_info;
        this.mod_time = mod_time;
        this.last_read_chapter_id = last_read_chapter_id;
        this.last_read_chapter_update_time = last_read_chapter_update_time;
        this.is_notify = is_notify;
    }

    public String getIs_buy() {
        return is_buy;
    }
    public com.moling.wearnovel.utils.templates.bookshelfIndex.book_info getBook_info() {
        return book_info;
    }
    public String getMod_time() {
        return mod_time;
    }
    public String getLast_read_chapter_id() {
        return last_read_chapter_id;
    }
    public String getLast_read_chapter_update_time() {
        return last_read_chapter_update_time;
    }
    public String getIs_notify() {
        return is_notify;
    }
}
