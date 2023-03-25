package com.moling.wearnovel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;

public class DebugAct extends Activity {
    Handler h = null;
    EditText DebugOutput;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug);

        DebugOutput = (EditText) findViewById(R.id.debug_output);

        h = new Handler(){
            public void handleMessage(Message msg){
                DebugOutput.setText((String)msg.obj);
            }
        };
    }

    public void ButtonDebug_onClick(View view) {
        String res;
        new Thread(new Runnable(){
            @Override
            public void run() {
                //Message msg = Message.obtain();
                //String decrypted = new String(CatAESDecryptor.decode((String)GET_BOOK_SHELF_INFORMATION(), ""));
                //msg.obj = decrypted;
                //msg.obj = (String)GET_BOOK_SHELF_INFORMATION();
                //String str = (String)GET_BOOK_SHELF_INFORMATION();
                //Log.d("tag", str);
                //JSONObject bookshelfJsonObj = JSON.parseObject(str);
                //JSONArray bookList = bookshelfJsonObj.getJSONObject("data").getJSONArray("shelf_list");
                //Log.d("tag",Long.toString(bookList.stream().count()));
                //h.sendMessage(msg);
                //msg.obj = (String)GET_BOOK_SHELF_INDEXES_INFORMATION("9669015");
                //Log.d("tag", (String)GET_BOOK_SHELF_INDEXES_INFORMATION("9669016"));
                //Log.d("tag", (String) msg.obj);

                //try {
                //    FileWriter fw = new FileWriter(MainAct.documentsBaseDir + File.separator + "bookshelf" + ".txt");
                //    fw.write("Test text");
                //    fw.close();
                //} catch (IOException e) {
                //    throw new RuntimeException(e);
                //}

                //List<book> books = catAPI.GetBooksOnBookshelf();
                //for(int i = 0; i < books.stream().count(); i++) {
                //    book bk = books.get(i);
                //    Log.d(bk.getBook_id(), bk.getBook_id());
                //    Log.d(bk.getBook_id(), bk.getBook_name());
                //    Log.d(bk.getBook_id(), bk.getAuthor_name());
                //    Log.d(bk.getBook_id(), bk.getLast_read_chapter_id());
                //}

                //File bookshelfFile = new File(MainAct.documentsBaseDir + "/Bookshelf.json");
                //bookshelfFile.delete();

                //File tempFile = new File(MainAct.documentsBaseDir + "/bookshelf.json");
                //tempFile.delete();
                deleteFile(new File(MainAct.documentsBaseDir + "/Bookshelf"));
                new File(MainAct.documentsBaseDir + "/bookshelf.json").delete();
                //new File(MainAct.documentsBaseDir + "/temp.txt").delete();
                Log.d("Debug", "File deleted");
                //String chapterCommandJson = (String) GET_KET_BY_CHAPTER_ID("106187868");
                //JSONObject chapterCommandJsonObj = JSON.parseObject(chapterCommandJson);
                //String command = chapterCommandJsonObj.getJSONObject("data").getString("command");
                //Log.d("Debug", "Chapter command: " + command);
                //bufferSave(tempFile, (String)GET_CHAPTER_CONTENT("106187868", command));
                //Log.d("Debug", "Buffer saved");

                //String tmp = (String)GET_BOOK_SHELF_INDEXES_INFORMATION("9669015");
                //bufferSave(tempFile, tmp);
                //Log.d("Debug", "Buffer saved");
            }
        }).start();
    }

    public static boolean deleteFile(File dirFile) {
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }

        return dirFile.delete();
    }
}
