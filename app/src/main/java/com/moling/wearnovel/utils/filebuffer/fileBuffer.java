package com.moling.wearnovel.utils.filebuffer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class fileBuffer {

    public static void bufferSave(File file, String msg) {
        try {
            BufferedWriter bfw = new BufferedWriter(new FileWriter(file, false));
            bfw.write(msg);
            bfw.newLine();
            bfw.flush();
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String bufferRead(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(file));
            String line = bfr.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = bfr.readLine();
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
