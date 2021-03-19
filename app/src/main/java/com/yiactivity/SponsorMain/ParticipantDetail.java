package com.yiactivity.SponsorMain;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.ImageToDB;
import com.yiactivity.model.User;

public class ParticipantDetail extends AppCompatActivity {

    private ImageView participant_image;
    private TextView participant_name;
    private TextView participant_university;
    private TextView participant_gender;
    private TextView participant_phoneNum;
    private TextView participant_email;
    private ImageView back_button;
    private User user;
    private Button agree_button;
    private Button disagree_button;
    private int mUserId;
    private int mActivityId;
    private int user_statement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_detail);

        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //初始化控件
        init();
        Intent intent = getIntent();
        mUserId = intent.getIntExtra("user_id",0);
        mActivityId = intent.getIntExtra("activity_id",0);
        user_statement = intent.getIntExtra("user_statement",0);
        getParticipantInfo(mUserId);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(user_statement == 1){
            //同意按钮监听器
            agree_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DBOperation.updateStatement(mUserId,mActivityId,1);
                            finish();
                        }
                    }).start();
                }
            });

            //拒绝按钮监听器
            disagree_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DBOperation.updateStatement(mUserId,mActivityId,2);
                            finish();
                        }
                    }).start();
                }
            });
        }
        else {
            agree_button.setVisibility(View.INVISIBLE);
            disagree_button.setVisibility(View.INVISIBLE);
        }

    }


    private void init(){
        participant_name = findViewById(R.id.participant_detail_name);
        participant_gender = findViewById(R.id.participant_detail_gender);
        participant_phoneNum = findViewById(R.id.participant_detail_phoneNum);
        participant_email = findViewById(R.id.participant_detail_email);
        participant_image = findViewById(R.id.participant_detail_image);
        participant_university = findViewById(R.id.participant_detail_university);
        back_button = findViewById(R.id.participant_detail_back);
        agree_button = findViewById(R.id.participant_detail_agree);
        disagree_button = findViewById(R.id.participant_detail_disagree);
    }

    private void getParticipantInfo(final int userId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                user = DBOperation.getUserInfo(userId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        participant_email.setText(user.getEmail());
                        participant_gender.setText(user.getGender());
                        participant_name.setText(user.getUserName());
                        participant_phoneNum.setText(user.getPhoneNum());
                        participant_university.setText(user.getUniversity());
                        Glide.with(ParticipantDetail.this).load(user.getImage()).into(participant_image);
                    }
                });
            }
        }).start();
    }
}
