package com.yiactivity.register;

import android.Manifest;
import android.content.ContentUris;
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
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.yiactivity.R;
import com.yiactivity.Utils.ImageToDB;
import com.yiactivity.mainScreen.MainActivity;
import com.yiactivity.model.User;
import com.yiactivity.login.loginActivity;

public class userRegister extends AppCompatActivity implements registerContract.View{

    private ImageView close;
    private EditText user_name;
    private EditText user_phoneNum;
    private EditText user_university;
    private EditText user_email;
    private EditText user_password;
    private RadioGroup gender_radioGroup;
    private RadioButton gender_radioButton;
    private Button user_registerBtn;
    private registerPresenter registerPresenter ;
    private ProgressBar progressBar;
    private Button choose_image_fromAlbum;
    private ImageView user_register_image;
    public static final int CHOOSE_PHOTO = 2;
    byte[] user_register_image_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        init();

        //返回按钮监听器
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //从相册选择用户头像
        choose_image_fromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(userRegister.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(userRegister.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                } else {
                    openAlbum();
                }
            }
        });

        //注册按钮监听器
        user_registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable)user_register_image.getDrawable()).getBitmap();
                user_register_image_string = ImageToDB.bitmaoToString(bitmap);
                progressBar.setVisibility(View.VISIBLE);
                gender_radioButton = findViewById(gender_radioGroup.getCheckedRadioButtonId());
                final User user = new User();
                user.setUserName(user_name.getText().toString());
                user.setUniversity(user_university.getText().toString());
                user.setPhoneNum(user_phoneNum.getText().toString());
                user.setPassword(user_password.getText().toString());
                user.setImage(user_register_image_string);
                user.setPoint(0);
                user.setEmail(user_email.getText().toString());
                user.setGender(gender_radioButton.getText().toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        registerPresenter.user_register(user);
                    }
                }).start();
            }
        });

    }

    private void init(){
        progressBar = findViewById(R.id.user_register_progressBar);
        registerPresenter = new registerPresenter(this);
        close = findViewById(R.id.close_user_register);
        user_name = findViewById(R.id.register_userName);
        user_phoneNum = findViewById(R.id.register_PhoneNum);
        user_university = findViewById(R.id.register_school);
        user_email = findViewById(R.id.register_email);
        user_password = findViewById(R.id.register_password);
        gender_radioGroup = findViewById(R.id.radio_group);
        user_registerBtn = findViewById(R.id.user_register_button);
        choose_image_fromAlbum = findViewById(R.id.choose_image_fromAlbum);
        user_register_image = findViewById(R.id.register_user_image);
    }


    @Override
    public void user_register_failed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(userRegister.this,"用户已存在",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void user_register_success() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(userRegister.this,loginActivity.class);
                startActivity(intent);
            }
        });
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
            user_register_image.setImageBitmap(bitmap);
        }
        else
        {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }
}
