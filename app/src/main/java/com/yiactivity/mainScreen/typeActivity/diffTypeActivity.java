package com.yiactivity.mainScreen.typeActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yiactivity.R;
import com.yiactivity.Utils.*;
import com.yiactivity.model.Activity;
import net.sourceforge.jtds.jdbc.RequestStream;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class diffTypeActivity extends AppCompatActivity {

    private RecyclerView all_recyclerView;
    private ImageView back_button;
    private ImageView poster;
    private TextView textView;
    private int mUserId;
    private String mType;
    private int begin = 1;
    private int end = 5;
    private int count = 6;
    ArrayList<Activity> currentList = new ArrayList<>();
    private LoadMoreAdapter loadMoreAdapter;
    private ArrayList<Activity> all_activity_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_type);

        //控件初始化
        all_recyclerView = findViewById(R.id.diff_type_recycle);
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

    public void getDiffTypeActivity(final String type){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody =new FormBody.Builder()
                .add("type",type)
                .add("begin","1")
                .add("end","5")
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL + "activity/typeActivity")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(diffTypeActivity.this,"连接失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    final JSONArray jsonArray = new JSONArray(response.body().string());
                    for(int i = 0;i<jsonArray.length();i++){
                        Activity activity = new Activity();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        activity.setActivityId(jsonObject.getInt("activityId"));
                        activity.setActivityName(jsonObject.getString("activityName"));
                        activity.setTime(jsonObject.getString("time"));
                        activity.setAddress(jsonObject.getString("address"));
                        activity.setAddressContent(jsonObject.getString("activityContent"));
                        activity.setType(jsonObject.getString("type"));
                        activity.setState(jsonObject.getInt("state"));
                        activity.setSponsorId(jsonObject.getInt("sponsorId"));
                        activity.setPoster2(jsonObject.getString("poster"));
                        activity.setBrowserCount(jsonObject.getInt("browserCount"));
                        activity.setTag(jsonObject.getString("tag"));
                        activity.setParticipant(jsonObject.getInt("participant"));
                        all_activity_list.add(activity);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            layoutManager.setOrientation(RecyclerView.VERTICAL);
                            all_recyclerView.setLayoutManager(layoutManager);
                            loadMoreAdapter = new LoadMoreAdapter(all_activity_list,mUserId);
                            all_recyclerView.setAdapter(loadMoreAdapter);

                            all_recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
                                @Override
                                public void onLoadMore() {
                                    loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING);
                                    if(all_activity_list.size() < 5){
                                        loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                                    }
                                    else{
                                        if (all_activity_list.size() < count) {
                                            begin = begin + 5;
                                            end = end + 5;
                                            OkHttpClient client1 = new OkHttpClient();
                                            RequestBody requestBody1 = new FormBody.Builder()
                                                    .add("type",type)
                                                    .add("begin",String.valueOf(begin))
                                                    .add("end",String.valueOf(end))
                                                    .build();
                                            Request request1 = new Request.Builder()
                                                    .url(IpAddress.URL + "activity/typeActivity")
                                                    .post(requestBody1)
                                                    .build();
                                            client1.newCall(request1).enqueue(new Callback() {
                                                @Override
                                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(diffTypeActivity.this,"加载数据失败，请检查网络或者重新加载",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                                    try{
                                                        JSONArray jsonArray1 = new JSONArray(response.body().string());
                                                        for(int i = 0;i<jsonArray1.length();i++){
                                                            Activity activity = new Activity();
                                                            JSONObject jsonObject = jsonArray1.getJSONObject(i);
                                                            activity.setActivityId(jsonObject.getInt("activityId"));
                                                            activity.setActivityName(jsonObject.getString("activityName"));
                                                            activity.setTime(jsonObject.getString("time"));
                                                            activity.setAddress(jsonObject.getString("address"));
                                                            activity.setAddressContent(jsonObject.getString("activityContent"));
                                                            activity.setType(jsonObject.getString("type"));
                                                            activity.setState(jsonObject.getInt("state"));
                                                            activity.setSponsorId(jsonObject.getInt("sponsorId"));
                                                            activity.setPoster2(jsonObject.getString("poster"));
                                                            activity.setBrowserCount(jsonObject.getInt("browserCount"));
                                                            activity.setTag(jsonObject.getString("tag"));
                                                            activity.setParticipant(jsonObject.getInt("participant"));
                                                            currentList.add(activity);
                                                        }
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if(currentList.size() > 4){
                                                                    all_activity_list.addAll(currentList);
                                                                    count = count + currentList.size();
                                                                    loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
                                                                    currentList.clear();
                                                                }
                                                                else {
                                                                    if(currentList.size() == 0){
                                                                        loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                                                                    }
                                                                    else {
                                                                        all_activity_list.addAll(currentList);
                                                                        count = all_activity_list.size();
                                                                        loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
                                                                        currentList.clear();
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                        else {
                                            // 显示加载到底的提示
                                            loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                                        }
                                    }

                                }
                            });
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }


}
