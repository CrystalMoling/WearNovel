package com.moling.wearnovel;

import static com.moling.wearnovel.locAPI.locAPI.GetBookIndex;
import static com.moling.wearnovel.utils.constants.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.moling.wearnovel.locAPI.locAPI;
import com.moling.wearnovel.utils.templates.divisionList.*;

import java.util.List;
import java.util.Objects;

public class MenuAct extends Activity {
    public static Activity ctx;
    ListView menuList;
    String[] itemsList;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        ctx = this;

        // 界面元素
        menuList = (ListView) findViewById(R.id.list_menu);

        // 向 ListView 添加 Header
        LayoutInflater inflater = LayoutInflater.from(this);
        View headView = inflater.inflate(R.layout.widget_title, null);
        backButton = (Button) headView.findViewById(R.id.button_back);
        menuList.addHeaderView(backButton,null,true);

        // 设置标题文本
        backButton.setText(MainAct.menuTitle);

        // 返回按钮点击事件
        backButton.setOnClickListener(view -> {
            if(MainAct.page == MENU_PAGE_BOOK_SOURCE) {
                MainAct.page = MENU_PAGE_MAIN;
                MainAct.menuTitle = "设置";
                Intent menuIntent = new Intent(ctx, MenuAct.class);
                startActivity(menuIntent);
                finish();
            }
        });

        // 向列表添加选项
        if(MainAct.page == MENU_PAGE_MAIN){
            itemsList = new String[]{"书源设置", "调试"};
        } else if (MainAct.page == MENU_PAGE_BOOK_SOURCE) {
            itemsList = new String[]{"刺猬猫"};
        } else if (MainAct.page == MENU_PAGE_DIVISION) {
            itemsList = new String[]{"ID:" + MainAct.menuArgs[0], "下载本卷所有已购章节"};
        } else if (MainAct.page == MENU_PAGE_CHAPTER) {
            itemsList = new String[]{"ID:" + MainAct.menuArgs[0], "下载该章节"};
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,itemsList);
        menuList.setAdapter(adapter);

        // 菜单项点击事件
        menuList.setOnItemClickListener((l, v, position, id) -> {
            String s = itemsList[position - 1];
            if(s.equals("书源设置"))
            {
                MainAct.page = MENU_PAGE_BOOK_SOURCE;
                MainAct.menuTitle = s;
                Intent menuIntent = new Intent(ctx, MenuAct.class);
                startActivity(menuIntent);
                finish();
            } else if (s.equals("调试")) {
                Intent settingCatIntent = new Intent(ctx, DebugAct.class);
                startActivity(settingCatIntent);
                finish();
            } else if (s.equals("刺猬猫")) {
                Intent settingCatIntent = new Intent(ctx, SettingCatAct.class);
                startActivity(settingCatIntent);
                finish();
            } else if (s.equals("下载本卷所有已购章节")) {
                // 当前书籍章节列表
                divisionList division_list = GetBookIndex(MainAct.externalBookId);
                // 当前卷章节列表
                List<chapter> chapter_list = division_list.getData().get(division_list.findDivision(MainAct.menuArgs[0])).getChapter_list();
                // 共下载章节数
                int total_down_count = 0;
                for (int i = 0; i < chapter_list.size(); i++) {
                    if (Objects.equals(chapter_list.get(i).getAuth_access(), 1)){
                        locAPI.DownloadChapterContent(MainAct.externalBookId, chapter_list.get(i).getChapter_id(), false, false, null);
                        total_down_count += 1;
                    }
                }
                Toast.makeText(this, "下载完成,已下载" + total_down_count + "章,共" + chapter_list.size() + "章", Toast.LENGTH_SHORT).show();
            } else if (s.equals("下载该章节")) {
                locAPI.DownloadChapterContent(MainAct.externalBookId, MainAct.menuArgs[0], false, false, null);
            }
        });
    }
}
