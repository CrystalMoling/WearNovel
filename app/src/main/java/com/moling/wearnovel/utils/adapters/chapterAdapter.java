package com.moling.wearnovel.utils.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moling.wearnovel.MainAct;
import com.moling.wearnovel.R;
import com.moling.wearnovel.utils.adapters.models.chapter_adapter;

import java.io.File;
import java.util.List;

public class chapterAdapter extends BaseAdapter {
    private List<chapter_adapter> mData; //定义数据
    private LayoutInflater mInflater; //定义Inflater,加载我们自定义的布局
    private String book_id;

    public chapterAdapter(LayoutInflater inflater, List<chapter_adapter> data, String bookId){
        mInflater = inflater;
        mData = data;
        book_id = bookId;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 获得ListView中的view
        View viewChapter = mInflater.inflate(R.layout.widget_chapter,null);
        // 获取 chapter 对象
        chapter_adapter chapter = mData.get(i);
        // 获得自定义布局中每一个控件的对象
        View view_status = (View) viewChapter.findViewById(R.id.view_status);
        View view_access = (View) viewChapter.findViewById(R.id.view_access);
        TextView text_title = (TextView) viewChapter.findViewById(R.id.text_title);
        TextView text_wordCount = (TextView) viewChapter.findViewById(R.id.text_wordcount);
        // 本地文件是否存在
        File loc_chapter = new File(MainAct.documentsBaseDir + "/Bookshelf/" + book_id + "/chapters/" + chapter.getChapter_id() + ".json");
        if (loc_chapter.exists()) {
            view_status.setBackgroundColor(Color.rgb(153, 235, 255));
        }
        // 是否具有 auth_access
        if (chapter.getAuth_access() == 1) {
            view_access.setBackgroundColor(Color.rgb(13, 208, 104));
        }
        // 将数据一一添加到自定义的布局中
        text_title.setText(chapter.getChapter_title());
        text_wordCount.setText("字数: " + chapter.getWord_count());

        return viewChapter;
    }
}
