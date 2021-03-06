package com.yiactivity.mainScreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.next.easynavigation.view.EasyNavigationBar;
import com.yiactivity.R;
import com.yiactivity.Services.UpdateStateService;
import com.yiactivity.mainScreen.searchActivity.VolunteerActivityList;
import com.yiactivity.model.User;

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
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }
        setContentView(R.layout.activity_main);
        User user = (User) getIntent().getSerializableExtra("user_data");
        EasyNavigationBar navigationBar = findViewById(R.id.navigationBar);
        int data = user.getUserId();
        fragments.add(new homeFragment(data));
        fragments.add(new activityListFragment(data));
        fragments.add(new activitySquareFragment(data));
        fragments.add(new myInfoFragment(user));

        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectedIcon)
                .fragmentList(fragments)
                .setOnTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabSelectEvent(View view, int position) {
                        switch (position){
                            case 0:
                                if(Build.VERSION.SDK_INT >= 21){
                                    View decorView = getWindow().getDecorView();
                                    decorView.setSystemUiVisibility(
                                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                                    getWindow().setStatusBarColor(Color.WHITE);
                                }
                                break;
                            case 1:
                                if(Build.VERSION.SDK_INT >= 21){
                                    View decorView = getWindow().getDecorView();
                                    decorView.setSystemUiVisibility(
                                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                                    getWindow().setStatusBarColor(Color.WHITE);
                                }
                                break;
                            case 2:
                                if(Build.VERSION.SDK_INT >= 21){
                                    View decorView = getWindow().getDecorView();
                                    decorView.setSystemUiVisibility(
                                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                                    getWindow().setStatusBarColor(Color.WHITE);
                                }
                                break;
                            case 3:
                                if(Build.VERSION.SDK_INT >= 21){
                                    View decorView = getWindow().getDecorView();
                                    decorView.setSystemUiVisibility(
                                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                                    getWindow().setStatusBarColor(Color.TRANSPARENT);
                                }
                        }
                        return false;
                    }

                    @Override
                    public boolean onTabReSelectEvent(View view, int position) {
                        return false;
                    }
                })
                .fragmentManager(getSupportFragmentManager())
                .build();

        Intent startIntent = new Intent(this, UpdateStateService.class);
        startService(startIntent);


    }

}
