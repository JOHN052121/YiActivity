package com.yiactivity.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.*;

public class ImageToDB {

    private static Bitmap bitmap;
    private static String string;
    public static byte[] stringToBitmap(String string) {
        //数据库中的String类型转换成Bitmap
        if (string != null) {
            byte[] bytes = Base64.decode(string, Base64.DEFAULT);
            return bytes;
        } else {
            return null;
        }
    }

    public static byte[] bitmaoToString(Bitmap bitmap){
        //用户在活动中上传的图片转换成String进行存储
        if(bitmap!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
            byte[] bytes = stream.toByteArray();// 转为byte数组
            return bytes;
        }
        else {
            return null;
        }
    }

    /*public static byte[] bitmapTo_JPG(Bitmap bitmap){
        FileOutputStream out = null;
        try{
            String path="/android_asset/banner.jpg";
            File file = new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)){
                out.flush();
                out.close();
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return out.toString().getBytes();
    }*/
}
