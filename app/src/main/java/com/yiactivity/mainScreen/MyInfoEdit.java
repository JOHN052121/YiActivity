package com.yiactivity.mainScreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.model.User;

public class MyInfoEdit extends AppCompatActivity {

    private ImageView user_img;
    private EditText user_name;
    private TextView user_gender;
    private EditText user_university;
    private EditText user_sign;
    private EditText user_email;
    private TextView save;
    private ImageView back_icon;
    private int mUserId;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_edit);

        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        init();

        //获取userId
        Intent intent =getIntent();
        mUserId = intent.getIntExtra("user_id",0);

        // 展示个人信息
        showMyInfo(mUserId);

        //返回按钮监听器
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //保存按钮监听器
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void init(){
        user_img = findViewById(R.id.edit_user_img);
        user_name = findViewById(R.id.edit_user_name);
        user_gender = findViewById(R.id.edit_user_gender);
        user_university = findViewById(R.id.edit_user_university);
        user_sign = findViewById(R.id.edit_user_sign);
        user_email = findViewById(R.id.edit_user_email);
        back_icon = findViewById(R.id.myInfo_edit_back);
        save = findViewById(R.id.myInfo_edit_save);
    }

    private void showMyInfo(final int userId){
        new Thread(new Runnable() {
            @Override
            public void run() {
               user = DBOperation.getUserInfo(userId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MyInfoEdit.this).load(user.getImage()).into(user_img);
                        user_email.setText(user.getEmail());
                        user_gender.setText(user.getGender());
                        user_name.setText(user.getUserName());
                        user_university.setText(user.getUniversity());
                    }
                });
            }
        }).start();
    }
}
