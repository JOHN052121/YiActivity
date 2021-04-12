package com.yiactivity.detailOfActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.ImageToDB;
import com.yiactivity.Utils.IpAddress;
import com.yiactivity.detailOfSponsor.SponsorDetail;
import com.yiactivity.model.Activity;
import com.yiactivity.model.Sponsor;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ActivityDetail extends AppCompatActivity implements detailOfActivityContract.View{

    private int activityId;
    private int userId;
    private ImageView bigImage;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private detailOfActivityPresenter detailOfActivityPresenter;
    private ImageView detail_activity_poster;
    private TextView detail_activity_Name;
    private TextView detail_activity_time;
    private TextView detail_activity_content;
    private TextView detail_activity_address;
    private Button detail_activity_enroll_button;
    private ImageView sponsor_icon;
    private int sponsorId;
    private ImageView sponsor_img;
    private TextView sponsor_intro;
    private TextView sponsor_name;
    private TextView sponsor_activity_number;
    private TextView sponsor_funs;
    private Button subscribe_button;
    private ImageView collection_button;
    private Button sponsor_detail;
    private int code;
    private static String SPONSOR_IMG_URL = "http://192.168.1.111:8080/SSM_war_exploded/asset/sponsorImage/";
    private TextView collection_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //设置toolbar的home返回键
        toolbar = findViewById(R.id.detail_activity_toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        //获取从activityAdapter传过来的activity 和 userId
        final Intent intent = getIntent();
        userId = intent.getIntExtra("userId",0);
        final Activity activity = (Activity)intent.getSerializableExtra("activity_data");
        activityId = activity.getActivityId();
        sponsorId = activity.getSponsorId();

        //控件初始化
        detailOfActivityPresenter = new detailOfActivityPresenter(this);
        init();

        //查询关注按钮
        subscribe_select(userId,sponsorId);

        //查询收藏按钮
        collection_select(userId,activityId);

        //活动项内容填充
        detailOfActivityPresenter.getDetailOfActivity(activity);
        detailOfActivityPresenter.getSponsorInfo(sponsorId);
        detailOfActivityPresenter.getEnrollState(activityId,userId,activity.getState());


        //"主办方"按钮监听
        sponsor_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ActivityDetail.this, SponsorDetail.class);
                intent1.putExtra("sponsor_id",sponsorId);
                intent1.putExtra("user_id",userId);
                System.out.println("主办方id是"+sponsorId);
                startActivity(intent1);
            }
        });

        sponsor_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ActivityDetail.this, SponsorDetail.class);
                intent1.putExtra("sponsor_id",sponsorId);
                intent1.putExtra("user_id",userId);
                System.out.println("主办方id是"+sponsorId);
                startActivity(intent1);
            }
        });


        //关注按钮监听
        subscribe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subscribe_button.getText().toString().equals("+ 关注")){
                    subscribe(userId,sponsorId);

                }
                else if(subscribe_button.getText().toString().equals("已关注")){
                    cancelSubscribe(userId,sponsorId);
                }
            }
        });

        //收藏按钮监听
        collection_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collection_text.getText().toString().equals("收藏")){
                    collection(userId,sponsorId);

                }
                else if(collection_text.getText().toString().equals("已收藏")){
                    cancelCollection(userId,sponsorId);
                }
            }
        });

        //"立即报名"按钮监听
            detail_activity_enroll_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(detail_activity_enroll_button.getText().equals("立即报名")){
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDetail.this);
                        builder.setTitle("报名活动");
                        builder.setMessage("确定报名吗？");
                        builder.setCancelable(false);
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                detail_activity_enroll_button.setText("已报名");
                                detail_activity_enroll_button.setBackground(getResources().getDrawable(R.drawable.detail_activity_enroll_buttoned));
                                OkHttpClient client = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("userId",String.valueOf(userId))
                                        .add("activityId",String.valueOf(activityId))
                                        .build();
                                Request request = new Request.Builder()
                                        .url(IpAddress.URL + "user/enroll")
                                        .post(requestBody)
                                        .build();
                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ActivityDetail.this, "连接失败，请检查网络", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ActivityDetail.this, "报名成功！", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }

                }
            });


    }

    private void init(){
        detail_activity_Name = findViewById(R.id.detail_activity_name);
        detail_activity_address = findViewById(R.id.detail_activity_location_text);
        detail_activity_content = findViewById(R.id.detail_activity_content);
        detail_activity_time = findViewById(R.id.detail_activity_time_text);
        detail_activity_poster = findViewById(R.id.detail_activity_poster);
        detail_activity_enroll_button = findViewById(R.id.detail_activity_enroll_button);
        sponsor_icon = findViewById(R.id.activity_detail_sponsor);
        sponsor_img = findViewById(R.id.activity_detail_sponsor_img);
        sponsor_name = findViewById(R.id.activity_detail_sponsor_name);
        sponsor_intro = findViewById(R.id.activity_detail_sponsor_intro);
        sponsor_activity_number = findViewById(R.id.activity_detail_activity_number);
        subscribe_button = findViewById(R.id.subscribe_button);
        sponsor_detail = findViewById(R.id.sponsor_detail_button);
        sponsor_funs = findViewById(R.id.funs_number);
        collection_button = findViewById(R.id.activity_detail_collection);
        collection_text = findViewById(R.id.activity_detail_collection_text);
    }
    @Override
    public void setDetailOfActivity(final Activity activity) {
        detail_activity_Name.setText(activity.getActivityName());
        detail_activity_address.setText(activity.getAddress());
        detail_activity_content.setText(activity.getAddressContent());
        //活动背部高斯模糊大图
        bigImage = findViewById(R.id.detail_activity_bigImage);
        Glide.with(getApplicationContext()).load(IpAddress.URL_PIC +"activityPoster/" + activity.getPoster2())
                        .apply(bitmapTransform(new BlurTransformation(25))).into(bigImage);
        detail_activity_time.setText(activity.getTime());
        Glide.with(ActivityDetail.this).load(IpAddress.URL_PIC+ "activityPoster/" + activity.getPoster2()).into(detail_activity_poster);
    }

    @Override
    public void setEnrollButton(final int state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (state){
                    case 0:
                        detail_activity_enroll_button.setText("立即报名");
                        break;
                    case 1:
                        detail_activity_enroll_button.setBackground(getResources().getDrawable(R.drawable.detail_activity_enroll_buttoned));
                        detail_activity_enroll_button.setText("已报名");
                        break;
                    case 2:
                        detail_activity_enroll_button.setBackground(getResources().getDrawable(R.drawable.detail_activity_enroll_buttoned));
                        detail_activity_enroll_button.setText("参与中");
                        break;
                    case 3:
                        detail_activity_enroll_button.setBackground(getResources().getDrawable(R.drawable.detail_activity_enroll_buttoned));
                        detail_activity_enroll_button.setText("已参与");
                        break;
                    case 4:
                        detail_activity_enroll_button.setBackground(getResources().getDrawable(R.drawable.detail_activity_enroll_buttoned));
                        detail_activity_enroll_button.setText("已结束");
                        break;
                }
            }
        });
    }

    @Override
    public void setSponsorInfo(final Sponsor sponsor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sponsor_name.setText(sponsor.getOrg_Name());
                Glide.with(getApplicationContext()).load(IpAddress.URL_PIC+"sponsorImage/" + sponsor.getSponsorImage1()).into(sponsor_img);
                sponsor_activity_number.setText(String.valueOf(sponsor.getActivityNumOfSponsor()));
                sponsor_intro.setText(sponsor.getSponsorIntro());
                sponsor_funs.setText(String.valueOf(sponsor.getFuns()));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                Toast.makeText(ActivityDetail.this,"连接失败，请检查网络!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        subscribe_button.setTextColor(Color.parseColor("#dbdbdb"));
                        subscribe_button.setText("已关注");
                        subscribe_button.setBackground(getResources().getDrawable(R.drawable.detail_activity_enroll_buttoned));
                        Toast.makeText(ActivityDetail.this, "关注成功", Toast.LENGTH_SHORT).show();
                    }
                });
                detailOfActivityPresenter.getSponsorInfo(sponsorId);
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
                Toast.makeText(ActivityDetail.this,"连接失败，请检查网络!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        subscribe_button.setTextColor(Color.parseColor("#2aa515"));
                        subscribe_button.setBackground(getResources().getDrawable(R.drawable.subscribe_button_shape));
                        subscribe_button.setText("+ 关注");
                        Toast.makeText(ActivityDetail.this, "已取消关注", Toast.LENGTH_SHORT).show();
                    }
                });
                detailOfActivityPresenter.getSponsorInfo(sponsorId);
            }
        });
    }

    private void collection_select(int userId,int activityId){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userId",String.valueOf(userId))
                .add("activityId",String.valueOf(activityId))
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL +"user/selectCollection")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    final int flag = Integer.parseInt(response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(flag == 0){
                                collection_text.setText("收藏");
                            }
                            else if(flag == 1){
                                collection_button.setImageResource(R.drawable.collection_after);
                                collection_text.setText("已收藏");
                            }
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void collection(int userId,int activityId){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userId",String.valueOf(userId))
                .add("activityId",String.valueOf(activityId))
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL +"user/collection")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ActivityDetail.this, "连接失败,请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ActivityDetail.this, "已收藏", Toast.LENGTH_SHORT).show();
                        collection_button.setImageResource(R.drawable.collection_after);
                        collection_text.setText("已收藏");

                    }
                });
            }
        });
    }

    private void cancelCollection(int userId,int activityId){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userId",String.valueOf(userId))
                .add("activityId",String.valueOf(activityId))
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL +"user/cancelCollection")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ActivityDetail.this, "连接失败,请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ActivityDetail.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                        collection_button.setImageResource(R.drawable.collection_before);
                        collection_text.setText("收藏");
                    }
                });
            }
        });
    }
}
