package com.moling.wearnovel.utils.templates.bookshelf;

import java.util.List;

public class bookshelf {
    private int code;
    private List<shelf_list> data;

    public bookshelf(int code, List<shelf_list> data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }
    public List<shelf_list> getData() {
        return data;
    }
}

