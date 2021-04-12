package com.yiactivity.SponsorMain;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.next.easynavigation.view.EasyNavigationBar;
import com.yiactivity.R;
import com.yiactivity.model.Activity;

import java.util.ArrayList;
import java.util.List;

public class MangerActivityInDetail extends AppCompatActivity {

    private int mSponsorId;
    private int mActivityId;
    private String mActivityName;
    private EasyNavigationBar easyNavigationBar;
    private String[] tabText = {"数据","参与者"};
    private int[] normalIcon = {R.drawable.data,R.drawable.participant};
    private int[] selectIcon = {R.drawable.data_buttoned,R.drawable.participant_buttoned};
    private List<Fragment> fragments = new ArrayList<>();
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger_in_detail);

        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.myColor));
        }
        //获取传过来的sponsorId,和activity对象
        Intent intent = getIntent();
        mSponsorId = intent.getIntExtra("sponsor_id",0);
        activity = (Activity) intent.getSerializableExtra("activity_data");

        //将两个fragment的实例加入fragment列表
        mActivityId = activity.getActivityId();
        mActivityName = activity.getActivityName();
        fragments.add(new DataFragment(mSponsorId,activity));
        fragments.add(new ParticipantFragment(mSponsorId,mActivityId,mActivityName));

        //设置底部导航栏
        easyNavigationBar = findViewById(R.id.activity_manger_in_detail_easyNavigationBar);
        easyNavigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .setOnTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabSelectEvent(View view, int position) {
                        if(position == 0){
                            if(Build.VERSION.SDK_INT >= 21){
                                View decorView = getWindow().getDecorView();
                                decorView.setSystemUiVisibility(
                                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                                getWindow().setStatusBarColor(getResources().getColor(R.color.myColor));
                                return false;
                            }

                        }
                        else if(position == 1){
                            if(Build.VERSION.SDK_INT >= 21){
                                View decorView = getWindow().getDecorView();
                                decorView.setSystemUiVisibility(
                                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                                getWindow().setStatusBarColor(Color.TRANSPARENT);
                                return false;
                            }
                        }
                        return false;
                    }

                    @Override
                    public boolean onTabReSelectEvent(View view, int position) {
                        return false;
                    }
                })
                .build();
    }

}
