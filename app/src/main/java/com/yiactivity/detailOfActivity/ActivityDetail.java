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
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.ImageToDB;
import com.yiactivity.detailOfSponsor.SponsorDetail;
import com.yiactivity.model.Activity;
import com.yiactivity.model.Sponsor;
import jp.wasabeef.glide.transformations.BlurTransformation;
import org.w3c.dom.Text;

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



        //获取从activityAdapter传过来的activityId 和 userId
        final Intent intent = getIntent();
        activityId = intent.getIntExtra("activityId",0);
        userId = intent.getIntExtra("userId",0);

        //控件初始化
        detailOfActivityPresenter = new detailOfActivityPresenter(this);
        init();

        //活动项内容填充
        new Thread(new Runnable() {
            @Override
            public void run() {
                detailOfActivityPresenter.getDetailOfActivity(activityId);
                detailOfActivityPresenter.getEnrollState(activityId,userId);
                detailOfActivityPresenter.getSponsorInfo(sponsorId);

            }
        }).start();

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
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DBOperation.enroll_activity(userId,activityId);
                                    }
                                }).start();
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
    }
    @Override
    public void setDetailOfActivity(final Activity activity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                detail_activity_Name.setText(activity.getActivityName());
                detail_activity_address.setText(activity.getAddress());
                detail_activity_content.setText(activity.getAddressContent());
                //活动背部高斯模糊大图
                bigImage = findViewById(R.id.detail_activity_bigImage);
                Glide.with(getApplicationContext()).load(activity.getPoster())
                        .apply(bitmapTransform(new BlurTransformation(40))).into(bigImage);
                detail_activity_time.setText(activity.getTime());
                Glide.with(ActivityDetail.this).load(activity.getPoster()).into(detail_activity_poster);
                sponsorId = activity.getSponsorId();
            }
        });
    }

    @Override
    public void setEnrollButton(int state) {
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

    @Override
    public void setSponsorInfo(final Sponsor sponsor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sponsor_name.setText(sponsor.getOrg_Name());
                Glide.with(getApplicationContext()).load(sponsor.getSponsorImage()).into(sponsor_img);
                sponsor_activity_number.setText(String.valueOf(sponsor.getActivityNumOfSponsor()));
                sponsor_intro.setText(sponsor.getSponsorIntro());
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

}
