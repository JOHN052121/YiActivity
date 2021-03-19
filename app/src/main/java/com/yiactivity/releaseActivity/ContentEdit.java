package com.yiactivity.releaseActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.yiactivity.R;

import java.sql.ResultSet;

public class ContentEdit extends AppCompatActivity {

    private TextView activity_content;
    private ImageView back_icon;
    private Button content_save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_edit);

        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //返回按钮
        back_icon = findViewById(R.id.content_edit_back);

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //将填写的活动内容返回到上一个活动
        content_save_button = findViewById(R.id.content_edit_save_button);
        content_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_content = findViewById(R.id.content_edit_content);
                Intent intent = new Intent();
                intent.putExtra("content_edit",activity_content.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }
}
