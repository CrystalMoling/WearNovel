package com.moling.wearnovel.utils.templates;

public class chapterKey {
    private int code;
    private String command;

    public chapterKey(int code, String command) {
        this.code = code;
        this.command = command;
    }

    public int getCode() {
        return code;
    }
    public String getCommand() {
        return command;
    }
}
