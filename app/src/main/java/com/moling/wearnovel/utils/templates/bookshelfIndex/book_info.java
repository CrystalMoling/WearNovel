package com.moling.wearnovel.utils.templates.bookshelfIndex;

public class book_info {
    private String book_id;
    private String book_name;
    private int is_original;
    private String category_index;
    private String total_word_count;
    private String review_amount;
    private String cover;
    private int discount;
    private String discount_end_time;
    private String author_name;
    private String uptime;
    private String update_status;

    public book_info(String book_id, String book_name, int is_original, String category_index, String total_word_count, String review_amount,
                     String cover, int discount, String discount_end_time, String author_name, String uptime, String update_status) {
        this.book_id = book_id;
        this.book_name = book_name;
        this.is_original = is_original;
        this.category_index = category_index;
        this.total_word_count = total_word_count;
        this.review_amount = review_amount;
        this.cover = cover;
        this.discount = discount;
        this.discount_end_time = discount_end_time;
        this.author_name = author_name;
        this.uptime = uptime;
        this.update_status =  update_status;
    }

    public String getBook_id() {
        return book_id;
    }
    public String getBook_name() {
        return book_name;
    }
    public int getIs_original() {
        return is_original;
    }
    public String getCategory_index() {
        return category_index;
    }
    public String getTotal_word_count() {
        return total_word_count;
    }
    public String getReview_amount() {
        return review_amount;
    }
    public String getCover() {
        return cover;
    }
    public int getDiscount() {
        return discount;
    }
    public String getDiscount_end_time() {
        return discount_end_time;
    }
    public String getAuthor_name() {
        return author_name;
    }
    public String getUptime() {
        return uptime;
    }
    public String getUpdate_status() {
        return update_status;
    }
}
