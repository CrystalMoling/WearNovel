package com.moling.wearnovel.utils.templates.bookshelfIndex;

import java.util.List;

public class bookshelfIndex {
    private int code;
    private List<book_list> data;

    public bookshelfIndex(int code, List<book_list> data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }
    public List<book_list> getData() {
        return data;
    }
}
