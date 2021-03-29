package com.yiactivity.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.model.Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UpdateStateService extends Service {

    private ArrayList<Activity> activityArrayList = new ArrayList<>();
    private long nowTime;
    public UpdateStateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                activityArrayList = DBOperation.getAllOfActivity();
                for(Activity activity:activityArrayList){
                    if(stringToLong(activity.getTime().substring(17)) < dateToStringToLong() ){
                        DBOperation.overdueActivity(activity.getActivityId());
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private long stringToLong(String time){
        long longstr1 = Long.valueOf(time.replaceAll("[-\\s:]",""));
        return longstr1;
    }

    private long dateToStringToLong(){
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datetime = tempDate.format(new java.util.Date());
        long time = stringToLong(datetime);
        return time;
    }
}
