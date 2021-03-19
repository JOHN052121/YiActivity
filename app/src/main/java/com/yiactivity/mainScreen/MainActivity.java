package com.yiactivity.mainScreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.next.easynavigation.view.EasyNavigationBar;
import com.yiactivity.R;
import com.yiactivity.mainScreen.searchActivity.VolunteerActivityList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] tabText = {"主页","找活动","活动圈","我的"};
    private int[] normalIcon = {R.drawable.home,R.drawable.search_bottom,R.drawable.activity_square,R.drawable.myinfo};
    private int[] selectedIcon = {R.drawable.home_buttoned,R.drawable.search_buttoned,R.drawable.ins_buttoned,R.drawable.user_buttoned};
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        Intent intent = getIntent();
        int data = intent.getIntExtra("user_id",0);
        EasyNavigationBar navigationBar = findViewById(R.id.navigationBar);
        fragments.add(new homeFragment(data));
        fragments.add(new activityListFragment(data));
        fragments.add(new activitySquareFragment());
        fragments.add(new myInfoFragment(data));

        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectedIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .build();



    }

}
