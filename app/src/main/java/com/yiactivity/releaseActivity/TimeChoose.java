package com.yiactivity.releaseActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bigkoo.pickerview.TimePickerView;
import com.yiactivity.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeChoose extends AppCompatActivity {

    private TextView beginTime;
    private TextView endTime;
    TimePickerView pvTime1;
    TimePickerView pvTime2;
    private Button save_button;
    private ImageView back_icon;
    private String get_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_choose);

        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        back_icon = findViewById(R.id.time_choose_back);
        save_button = findViewById(R.id.time_choose_save_button);



        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_time = beginTime.getText().toString()+"-"+endTime.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("choose_time",get_time);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        beginTime = findViewById(R.id.begin_time);
        endTime = findViewById(R.id.end_time);

        Calendar calendar = Calendar.getInstance();



        pvTime1 = new TimePickerView(this, TimePickerView.Type.MONTH_DAY_HOUR_MIN);
        pvTime1.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR));
        pvTime1.setTime(new Date());
        pvTime1.setCyclic(false);
        pvTime1.setCancelable(true);


        pvTime2 = new TimePickerView(this, TimePickerView.Type.MONTH_DAY_HOUR_MIN);
        pvTime2.setRange(calendar.get(Calendar.YEAR),calendar.get(Calendar.YEAR));
        pvTime2.setTime(new Date());
        pvTime2.setCyclic(false);
        pvTime2.setCancelable(true);



        beginTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime1.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime2.show();
            }
        });

        pvTime1.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                beginTime.setText(getTime(date));
            }
        });

        pvTime2.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                endTime.setText(getTime(date));
            }
        });
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}
