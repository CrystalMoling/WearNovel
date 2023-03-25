package com.moling.wearnovel.utils.templates.divisionList;

import java.util.List;

public class division {
    private List<chapter> chapter_list;
    private int max_update_time;
    private int max_chapter_index;
    private String division_id;
    private String division_index;
    private String division_name;

    public division(List<chapter> chapter_list, int max_update_time, int max_chapter_index, String division_id, String division_index, String division_name) {
        this.chapter_list = chapter_list;
        this.max_update_time = max_update_time;
        this.max_chapter_index = max_chapter_index;
        this.division_id = division_id;
        this.division_index = division_index;
        this.division_name = division_name;
    }

    public List<chapter> getChapter_list() {
        return chapter_list;
    }
    public int getMax_update_time() {
        return max_update_time;
    }
    public int getMax_chapter_index() {
        return max_chapter_index;
    }
    public String getDivision_id() {
        return division_id;
    }
    public String getDivision_index() {
        return division_index;
    }
    public String getDivision_name() {
        return division_name;
    }
}
