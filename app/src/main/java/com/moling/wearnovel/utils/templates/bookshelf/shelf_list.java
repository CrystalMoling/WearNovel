package com.moling.wearnovel.utils.templates.bookshelf;

public class shelf_list {
    private String shelf_id;
    private String reader_id;
    private String shelf_name;
    private String shelf_index;
    private int book_limit;

    public shelf_list(String shelf_id, String reader_id, String shelf_name, String shelf_index, int book_limit) {
        this.shelf_id = shelf_id;
        this.reader_id = reader_id;
        this.shelf_name = shelf_name;
        this.shelf_index = shelf_index;
        this.book_limit = book_limit;
    }

    public String getShelf_id() {
        return shelf_id;
    }
    public String getReader_id() {
        return reader_id;
    }
    public String getShelf_name() {
        return shelf_name;
    }
    public void setShelf_index(String shelf_index) {
        this.shelf_index = shelf_index;
    }
    public int getBook_limit() {
        return book_limit;
    }
}
