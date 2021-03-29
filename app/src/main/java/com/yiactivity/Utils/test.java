package com.yiactivity.Utils;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.yiactivity.R;
import com.yiactivity.model.Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class test extends AppCompatActivity {

    private ArrayList<Activity> activityArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final String datetime = tempDate.format(new java.util.Date());

        new Thread(new Runnable() {
            @Override
            public void run() {
                activityArrayList = DBOperation.getAllActivity(1,5);
                long time1 = stringToLong(activityArrayList.get(1).getTime().substring(17));
                System.out.println(time1);
                System.out.println(stringToLong(datetime));
                if(stringToLong(datetime) > time1){
                    Log.d("调试","当前时间大于活动时间");
                }
                else {
                    Log.d("调试","当前时间小于活动时间");
                }
            }
        }).start();

    }

    private long stringToLong(String time){
        long longstr1 = Long.valueOf(time.replaceAll("[-\\s:]",""));
        return longstr1;
    }
}
