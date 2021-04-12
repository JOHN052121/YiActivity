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
import com.yiactivity.model.Sponsor;
import com.yiactivity.releaseActivity.ReleaseActivities;

import java.util.ArrayList;
import java.util.List;

public class SponsorMainScreen extends AppCompatActivity {

    private EasyNavigationBar easyNavigationBar;
    private String[] tabText = {"活动管理","发布活动","我的"};
    private int[] normalIcon ={R.drawable.manager,R.drawable.add_activity,R.drawable.myinfo} ;
    private int[] selectIcon ={R.drawable.manager_choosed,R.drawable.add_activity,R.drawable.user_buttoned} ;
    private List<Fragment> fragments = new ArrayList<>();
    private Sponsor sponsor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_main_screen);

        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        //获取传过来的sponsor对象
        Intent intent = getIntent();
        sponsor = (Sponsor) intent.getSerializableExtra("sponsor_data");

        //将管理活动和我的两个fragment加入fragment列表
        fragments.add(new ManagerActivity(sponsor.getSponsorId()));
        fragments.add(new SponsorMyInfo(sponsor));

        //设置底部导航栏easyNavigationBar
        easyNavigationBar = findViewById(R.id.Sponsor_main_EasyNavigationBar);
        easyNavigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .setOnTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabSelectEvent(View view, int position) {
                        if (position == 1){
                            Intent intent = new Intent(SponsorMainScreen.this,ReleaseActivities.class);
                            intent.putExtra("sponsor_id",sponsor.getSponsorId());
                            startActivity(intent);
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean onTabReSelectEvent(View view, int position) {
                        return false;
                    }
                })
                .centerNormalTextColor(Color.parseColor("#000000"))
                .fragmentManager(getSupportFragmentManager())
                .build();

    }
}
