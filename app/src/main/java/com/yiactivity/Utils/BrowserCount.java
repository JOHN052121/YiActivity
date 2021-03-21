package com.yiactivity.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class BrowserCount{

    private final static String BROWSER_NAME = "activity_browser";


    //存储浏览量
    public static void saveBrowserCount(Context context,int activityId){
        SharedPreferences sp =context.getSharedPreferences(BROWSER_NAME, Context.MODE_PRIVATE);
        int count = sp.getInt("browser"+activityId,0) + 1;
        Log.d("调试","count是"+count);
        Log.d("调试","activityId是"+activityId);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("browser"+activityId,count);
        editor.apply();
    }
   //读取浏览量
    public static int getBrowserCount(Context context,int activityId){
        SharedPreferences sp = context.getSharedPreferences(BROWSER_NAME, Context.MODE_PRIVATE);
        int count = sp.getInt("browser"+activityId,0);
        return count;
    }
}
