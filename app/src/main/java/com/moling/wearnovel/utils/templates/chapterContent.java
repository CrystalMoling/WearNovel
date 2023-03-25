package com.moling.wearnovel.utils.templates;

public class chapterContent {
    private int code;
    private String chapter_id;
    private String book_id;
    private String division_id;
    private int unit_hlb;
    private String chapter_index;
    private String chapter_title;
    private String author_say;
    private String word_count;
    private String discount;
    private String is_paid;
    private int auth_access;
    private String buy_amount;
    private String tsukkomi_amount;
    private String total_hlb;
    private String uptime;
    private String mtime;
    private String ctime;
    private String recommend_book_info;
    private String base_status;
    private String txt_content;

    public chapterContent(int code, String chapter_id, String book_id, String division_id, int unit_hlb, String chapter_index,String chapter_title,
                          String author_say, String word_count, String discount, String is_paid, int auth_access, String buy_amount, String tsukkomi_amount,
                          String total_hlb, String uptime, String mtime, String ctime, String recommend_book_info, String base_status, String txt_content) {
        this.code = code;
        this.chapter_id = chapter_id;
        this.book_id = book_id;
        this.division_id = division_id;
        this.unit_hlb = unit_hlb;
        this.chapter_index = chapter_index;
        this.chapter_title = chapter_title;
        this.author_say = author_say;
        this.word_count = word_count;
        this.discount = discount;
        this.is_paid = is_paid;
        this.auth_access = auth_access;
        this.buy_amount = buy_amount;
        this.tsukkomi_amount = tsukkomi_amount;
        this.total_hlb = total_hlb;
        this.uptime = uptime;
        this.mtime = mtime;
        this.ctime = ctime;
        this.recommend_book_info = recommend_book_info;
        this.base_status = base_status;
        this.txt_content = txt_content;
    }

    public int getCode() {
        return code;
    }
    public String getChapter_id() {
        return chapter_id;
    }
    public String getBook_id() {
        return book_id;
    }
    public String getDivision_id() {
        return division_id;
    }
    public int getUnit_hlb() {
        return unit_hlb;
    }
    public String getChapter_index() {
        return chapter_index;
    }
    public String getChapter_title() {
        return chapter_title;
    }
    public String getAuthor_say() {
        return author_say;
    }
    public String getWord_count() {
        return word_count;
    }
    public String getDiscount() {
        return discount;
    }
    public String getIs_paid() {
        return is_paid;
    }
    public int getAuth_access() {
        return auth_access;
    }
    public String getBuy_amount() {
        return buy_amount;
    }
    public String getTsukkomi_amount() {
        return tsukkomi_amount;
    }
    public String getTotal_hlb() {
        return total_hlb;
    }
    public String getUptime() {
        return uptime;
    }
    public String getMtime() {
        return mtime;
    }
    public String getCtime() {
        return ctime;
    }
    public String getRecommend_book_info() {
        return recommend_book_info;
    }
    public String getBase_status() {
        return base_status;
    }
    public String getTxt_content() {
        return txt_content;
    }
}
