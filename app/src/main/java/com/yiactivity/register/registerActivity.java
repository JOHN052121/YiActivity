package com.yiactivity.register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.yiactivity.R;


public class registerActivity extends AppCompatActivity {

    private ImageView close;
    private RadioGroup register_radioGroup;
    private RadioButton choose_register;
    private Button next_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        close = findViewById(R.id.close_register);
        register_radioGroup = findViewById(R.id.choose_register);
        next_button = findViewById(R.id.register_next);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_register = findViewById(register_radioGroup.getCheckedRadioButtonId());
                String flag = choose_register.getText().toString();
                if(flag.equals("参与方用户注册")){
                    Intent intent = new Intent(registerActivity.this,userRegister.class);
                    startActivity(intent);

                }
                else if(flag.equals("主办方用户注册")){
                    Intent intent = new Intent(registerActivity.this,sponsorRegister.class);
                    startActivity(intent);
                }
            }
        });



    }
}
