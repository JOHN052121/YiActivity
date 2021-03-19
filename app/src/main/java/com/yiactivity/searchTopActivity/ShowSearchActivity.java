package com.yiactivity.searchTopActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.yiactivity.R;
import com.yiactivity.Utils.FragmentOrderListAdapter;
import com.yiactivity.mainScreen.searchActivity.AllActivityList;
import com.yiactivity.mainScreen.searchActivity.VolunteerActivityList;

import java.util.ArrayList;
import java.util.List;

public class ShowSearchActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView back_button;
    private String keyword;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_search);

        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //获取从SearchActivity传过来的userid和keyword
        Intent intent = getIntent();
        keyword = intent.getStringExtra("search_keyword");
        mUserId = intent.getIntExtra("user_id",0);
        //顶部导航栏
        tabLayout = findViewById(R.id.show_search_list_tabLayout);
        viewPager = findViewById(R.id.show_search_list_top_viewPager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ShowTopSearchActivity(mUserId,keyword));
        fragments.add(new ShowTopSearchSponsor(mUserId,keyword));
        FragmentPagerAdapter adapter = new FragmentOrderListAdapter(getSupportFragmentManager(),fragments, new String[]{"活动","主办方"});
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //返回按钮监听
         back_button = findViewById(R.id.show_search_return_button);
         back_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });

         //顶部搜索

    }
}
