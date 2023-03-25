package com.moling.wearnovel.utils.templates.divisionList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class divisionList {
    private int code;
    private List<division> data;

    public divisionList(int code, List<division> data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }
    public List<division> getData() {
        return data;
    }
    public List<chapter> toChapterList() {
        List<chapter> chapter_list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            chapter_list.addAll(data.get(i).getChapter_list());
        }
        return chapter_list;
    }
    public int findChapter(String chapter_id) {
        int chapterIndex = 0;
        List<chapter> chapter_list = this.toChapterList();
        for (int i = 0; i < chapter_list.size() - 1; i++) {
            if (Objects.equals(chapter_id, chapter_list.get(i).getChapter_id())) {
                chapterIndex = i;
                break;
            }
        }
        return chapterIndex;
    }
    public int findDivision(String division_id) {
        int divisionIndex = 0;
        for (int i = 0; i < this.getData().size() - 1; i++) {
            if (Objects.equals(division_id, this.getData().get(i).getDivision_id())){
                divisionIndex = i;
                break;
            }
        }
        return divisionIndex;
    }
}
