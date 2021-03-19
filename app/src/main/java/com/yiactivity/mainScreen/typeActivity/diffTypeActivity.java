package com.yiactivity.mainScreen.typeActivity;

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
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.Search_all_activityAdapter;
import com.yiactivity.model.Activity;

import java.util.ArrayList;

public class diffTypeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView back_button;
    private ImageView poster;
    private TextView textView;
    private int mUserId;
    private String mType;
    private int mActivityId;
    private Search_all_activityAdapter all_activityAdapter;
    private ArrayList<Activity> activityArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_type);

        //控件初始化
        recyclerView = findViewById(R.id.diff_type_recycle);
        back_button = findViewById(R.id.activity_diff_type_back);
        textView = findViewById(R.id.activity_diff_type_name);
        poster = findViewById(R.id.type_image);
        //系统栏设置
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //获取intent的值
        Intent intent = getIntent();
        mType = intent.getStringExtra("type");
        mUserId = intent.getIntExtra("user_id",0);

        //设置标题和海报
        textView.setText(mType.substring(0,4));
        switch (mType){
            case "志愿活动类":
                poster.setImageResource(R.drawable.volunteer_type_poster);
                break;
            case "文体比赛类":
                poster.setImageResource(R.drawable.sport_type_poster);
                break;
            case "学术竞赛类":
                poster.setImageResource(R.drawable.competition_type_poster);
                break;
            case "讲座演说类":
                poster.setImageResource(R.drawable.lecture_type_poster);
                break;
            case "社团活动类":
                poster.setImageResource(R.drawable.community_type_poster);
                break;
        }
        //左上角返回按钮
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getDiffTypeActivity(mType);

    }

    private void getDiffTypeActivity(final String type){
        new Thread(new Runnable() {
            @Override
            public void run() {
                activityArrayList = DBOperation.getDiffTypeActivity(type);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(diffTypeActivity.this);
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        all_activityAdapter = new Search_all_activityAdapter(activityArrayList,mUserId);
                        recyclerView.setAdapter(all_activityAdapter);

                    }
                });
            }
        }).start();
    }


}
