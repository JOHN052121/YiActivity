package com.yiactivity.releaseActivity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.yiactivity.R;
import com.yiactivity.SponsorMain.ManagerActivity;
import com.yiactivity.SponsorMain.SponsorMainScreen;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.ImageToDB;
import com.yiactivity.model.Activity;
import com.yiactivity.register.userRegister;

public class ReleaseActivities extends AppCompatActivity {

    private ImageView back_button;
    private ImageView activity_image;
    private EditText activity_name;
    private EditText activity_time;
    private EditText activity_address;
    private EditText activity_participantNum;
    private EditText activity_type;
    private EditText activity_content;
    private Button release_button;
    private Activity activity = new Activity();
    public static final int CHOOSE_PHOTO = 2;
    private String content_edit_string;
    private String chose_type;
    private int mSponsorId;
    private ProgressBar progressBar;
    private String activity_time_from_time_choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_activities);

        //系统栏透明
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //初始化控件
        init();

        //获取sponsorId
        final Intent intent = getIntent();
        mSponsorId = intent.getIntExtra("sponsor_id",0);

        //选择海报、选择活动种类、编写活动内容
        activity_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(ReleaseActivities.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ReleaseActivities.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                } else {
                    openAlbum();
                }
            }
        });

        activity_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReleaseActivities.this,TypeChose.class);
                startActivityForResult(intent,3);
            }
        });

        activity_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReleaseActivities.this,ContentEdit.class);
                startActivityForResult(intent,4);
            }
        });

        activity_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReleaseActivities.this,TimeChoose.class);
                startActivityForResult(intent,5);
            }
        });


        //返回按钮监听器
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //活动发布按钮监听
        release_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder bulider = new AlertDialog.Builder(ReleaseActivities.this);
                bulider.setTitle("发布活动");
                bulider.setMessage("确定发布活动吗?");
                bulider.setCancelable(false);
                bulider.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //将信息填入实体
                        activity.setAddress(activity_address.getText().toString());
                        activity.setActivityName(activity_name.getText().toString());
                        activity.setPoster(ImageToDB.bitmaoToString(((BitmapDrawable)activity_image.getDrawable()).getBitmap()));
                        activity.setAddressContent(content_edit_string);
                        activity.setType(chose_type);
                        activity.setState(1);
                        activity.setTime(activity_time_from_time_choose);
                        activity.setSponsorId(mSponsorId);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DBOperation.releaseActivity(activity);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.VISIBLE);
                                        Toast.makeText(ReleaseActivities.this,"发布成功",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                finish();
                            }
                        }).start();
                    }
                });
                bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                bulider.show();
            }
        });
    }


    private void init(){
        back_button = findViewById(R.id.release_activity_back);
        activity_address = findViewById(R.id.release_activity_address);
        activity_image = findViewById(R.id.release_activity_poster);
        activity_name = findViewById(R.id.release_activity_name);
        activity_time = findViewById(R.id.release_activity_time);
        activity_participantNum = findViewById(R.id.release_activity_number);
        activity_content = findViewById(R.id.release_activity_content);
        activity_type = findViewById(R.id.release_activity_type);
        release_button = findViewById(R.id.release_activity_button);
        progressBar = findViewById(R.id.release_progressbar);
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)  {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this,"您拒绝了打开相册的请求",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case CHOOSE_PHOTO:
                handleImageOnKitKat(data);
                break;
            case 3:
                if(resultCode == RESULT_OK){
                    chose_type = data.getStringExtra("chose_type");
                    activity_type.setText(chose_type);
                    activity_type.setTextColor(Color.parseColor("#7cba59"));
                }
                break;
            case 4:
                if(resultCode == RESULT_OK){
                    content_edit_string = data.getStringExtra("content_edit");
                    activity_content.setText("已完善");
                    activity_content.setTextColor(Color.parseColor("#7cba59"));
                }
                break;
            case 5:
                if(resultCode == RESULT_OK){
                    activity_time_from_time_choose = data.getStringExtra("choose_time");
                    activity_time.setText(activity_time_from_time_choose);
                    activity_time.setTextColor(Color.parseColor("#7cba59"));
                }
            default:
                break;
        }
    }

    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1]; //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        } else if("content".equalsIgnoreCase(uri.getScheme())){
            // 如果是content类型的Uri,则使用普通方式处理
            imagePath = getImagePath(uri,null);
        } else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection){
        String path = null;
        //通过Uri 和 selection 来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath !=null ){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            activity_image.setImageBitmap(bitmap);
        }
        else
        {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }


}
