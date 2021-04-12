package com.yiactivity.detailOfSponsor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yiactivity.R;
import com.yiactivity.Utils.*;
import com.yiactivity.detailOfActivity.ActivityDetail;
import com.yiactivity.model.Activity;
import com.yiactivity.model.Sponsor;
import com.yiactivity.searchTopActivity.TopSearchSponsorAdapter;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SponsorDetail extends AppCompatActivity {

    private Sponsor sponsor;
    private ImageView sponsor_Img;
    private TextView sponsor_OrgName;
    private TextView sponsor_OrgNameBar;
    private RecyclerView recyclerView;
    private TextView sponsor_introduce;
    private int mUserId;
    private ArrayList<Activity> activityArrayList = new ArrayList<>();
    private Search_all_activityAdapter activityAdapter;
    private ImageView back_button;
    private TextView sposnor_funs;
    private TextView sponsor_activity_Num;
    private int sponsorId;
    private Button subscribe_button;

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
        sponsorId = intent.getIntExtra("sponsor_id",0);

        subscribe_select(mUserId,sponsorId);

        subscribe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subscribe_button.getText().toString().equals("+ 关注")){
                    subscribe(mUserId,sponsorId);
                }
                else if(subscribe_button.getText().toString().equals("已关注")){
                    cancelSubscribe(mUserId,sponsorId);
                }
            }
        });
        //获取该sponsorId下的详细信息和活动并展示
        getSponsorDetailById(sponsorId);
        getActivityBySponId(sponsorId);

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
        sposnor_funs = findViewById(R.id.sponsor_detail_funs);
        sponsor_activity_Num = findViewById(R.id.sponsor_detail_activity);
        subscribe_button = findViewById(R.id.sponsor_detail_subscribe_button);
    }

    private void getActivityBySponId(final int sponsorId){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("sponsorId",String.valueOf(sponsorId))
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.1.111:8080/SSM_war_exploded/activity/getActivityBySponsorId")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SponsorDetail.this, "连接失败请查看网络", Toast.LENGTH_SHORT).show();
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
                        activityArrayList.add(activity);
                    }
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
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getSponsorDetailById(final int sponsorId){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("sponsorId",String.valueOf(sponsorId))
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL +"sponsor/getSponsorById")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SponsorDetail.this,"连接失败请检查网络",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    final Sponsor sponsor = new Sponsor();
                    sponsor.setSponsorId(jsonObject.getInt("sponsorId"));
                    sponsor.setSpon_Name(jsonObject.getString("spon_Name"));
                    sponsor.setOrg_Name(jsonObject.getString("org_Name"));
                    sponsor.setPassword(jsonObject.getString("password"));
                    sponsor.setUniversity(jsonObject.getString("university"));
                    sponsor.setPhoneNum(jsonObject.getString("phoneNum"));
                    sponsor.setEmail(jsonObject.getString("email"));
                    sponsor.setSponsorImage1(jsonObject.getString("sponsorImage"));
                    sponsor.setSponsorIntro(jsonObject.getString("sponIntro"));
                    sponsor.setActivityNumOfSponsor(jsonObject.getInt("activityNum"));
                    sponsor.setFuns(jsonObject.getInt("funs"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(SponsorDetail.this).load(IpAddress.URL_PIC+"sponsorImage/" + sponsor.getSponsorImage1()).into(sponsor_Img);
                            sponsor_introduce.setText(sponsor.getSponsorIntro());
                            sponsor_OrgName.setText(sponsor.getOrg_Name());
                            sponsor_OrgNameBar.setText(sponsor.getOrg_Name());
                            sposnor_funs.setText(String.valueOf(sponsor.getFuns()));
                            sponsor_activity_Num.setText(String.valueOf(sponsor.getActivityNumOfSponsor()));
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void updateSubscribeState(final int sponsorId){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("sponsorId",String.valueOf(sponsorId))
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL +"sponsor/getSponsorById")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SponsorDetail.this,"连接失败请检查网络",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    final Sponsor sponsor = new Sponsor();
                    sponsor.setFuns(jsonObject.getInt("funs"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sposnor_funs.setText(String.valueOf(sponsor.getFuns()));
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void subscribe_select(int userId,int sponsorId){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userId",String.valueOf(userId))
                .add("sponsorId",String.valueOf(sponsorId))
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL + "user/selectSubscribe")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    final int flag = Integer.parseInt(response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(flag == 0){
                                subscribe_button.setTextColor(Color.parseColor("#2aa515"));
                                subscribe_button.setBackground(getResources().getDrawable(R.drawable.subscribe_button_shape));
                                subscribe_button.setText("+ 关注");
                            }
                            else if(flag == 1){
                                subscribe_button.setTextColor(Color.parseColor("#dbdbdb"));
                                subscribe_button.setText("已关注");
                                subscribe_button.setBackground(getResources().getDrawable(R.drawable.detail_activity_enroll_buttoned));
                            }
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void subscribe(int userId, final int sponsorId){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userId",String.valueOf(userId))
                .add("sponsorId",String.valueOf(sponsorId))
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL + "user/subscribe")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(SponsorDetail.this,"连接失败，请检查网络!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        subscribe_button.setTextColor(Color.parseColor("#dbdbdb"));
                        subscribe_button.setText("已关注");
                        subscribe_button.setBackground(getResources().getDrawable(R.drawable.detail_activity_enroll_buttoned));
                        Toast.makeText(SponsorDetail.this, "关注成功", Toast.LENGTH_SHORT).show();
                    }
                });
                updateSubscribeState(sponsorId);
            }
        });
    }

    private void cancelSubscribe(int userId, final int sponsorId){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userId",String.valueOf(userId))
                .add("sponsorId",String.valueOf(sponsorId))
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL + "user/cancelSubscribe")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(SponsorDetail.this,"连接失败，请检查网络!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        subscribe_button.setTextColor(Color.parseColor("#2aa515"));
                        subscribe_button.setBackground(getResources().getDrawable(R.drawable.subscribe_button_shape));
                        subscribe_button.setText("+ 关注");
                        Toast.makeText(SponsorDetail.this, "已取消关注", Toast.LENGTH_SHORT).show();
                    }
                });
                updateSubscribeState(sponsorId);
            }
        });
    }
}
