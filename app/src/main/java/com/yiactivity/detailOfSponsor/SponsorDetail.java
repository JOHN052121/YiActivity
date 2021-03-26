package com.yiactivity.detailOfSponsor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yiactivity.R;
import com.yiactivity.Utils.ActivityAdapter;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.ImageToDB;
import com.yiactivity.Utils.Search_all_activityAdapter;
import com.yiactivity.model.Activity;
import com.yiactivity.model.Sponsor;
import com.yiactivity.searchTopActivity.TopSearchSponsorAdapter;

import java.util.ArrayList;

public class SponsorDetail extends AppCompatActivity {

    private Sponsor sponsor;
    private ImageView sponsor_Img;
    private TextView sponsor_OrgName;
    private TextView sponsor_OrgNameBar;
    private RecyclerView recyclerView;
    private TextView sponsor_introduce;
    private int mUserId;
    private int mSponsorId;
    private ArrayList<Activity> activityArrayList = new ArrayList<>();
    private Search_all_activityAdapter activityAdapter;
    private ImageView back_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_detail);

        //初始化各控件
        init();

        recyclerView.setNestedScrollingEnabled(false);

        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //获取从其他页面点传过来的sponsorId和userId;
        Intent intent = getIntent();
        mUserId = intent.getIntExtra("user_id",0);
        mSponsorId = intent.getIntExtra("sponsor_id",0);


        //获取该sponsorId下的详细信息和活动并展示
        getSponsorDetailById(mSponsorId);
        getActivityBySponId(mSponsorId);

        //返回按钮监听
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init(){
        sponsor_Img = findViewById(R.id.detail_sponsor_image);
        sponsor_OrgName = findViewById(R.id.detail_sponsor_name);
        sponsor_OrgNameBar = findViewById(R.id.detail_sponsor_bar_name);
        sponsor_introduce = findViewById(R.id.detail_sponsor_introduce);
        recyclerView = findViewById(R.id.detail_sponsor_activity_recycle);
        back_button = findViewById(R.id.detail_sponsor_back);
    }

    private void getActivityBySponId(final int sponsorId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                activityArrayList = DBOperation.getActivityBySponsorId(sponsorId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(SponsorDetail.this);
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        activityAdapter = new Search_all_activityAdapter(activityArrayList,mUserId);
                        recyclerView.setAdapter(activityAdapter);
                    }
                });
            }
        }).start();
    }

    private void getSponsorDetailById(final int sponsorId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sponsor = DBOperation.getSponsorDetailById(sponsorId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(SponsorDetail.this).load(sponsor.getSponsorImage()).into(sponsor_Img);
                        sponsor_introduce.setText(sponsor.getSponsorIntro());
                        sponsor_OrgName.setText(sponsor.getOrg_Name());
                        sponsor_OrgNameBar.setText(sponsor.getOrg_Name());
                    }
                });
            }
        }).start();
    }
}
