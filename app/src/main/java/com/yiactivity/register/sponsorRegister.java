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
import com.yiactivity.model.Sponsor;

public class sponsorRegister extends AppCompatActivity implements registerContract.View{
    private ProgressBar progressBar;
    private ImageView close;
    private EditText sponsor_name;
    private EditText sponsor_university;
    private EditText sponsor_phoneNum;
    private EditText sponsor_orgName;
    private EditText sponsor_password;
    private EditText sponsor_email;
    private EditText sponsor_intro;
    private Button register_sponsorBtn;
    private registerPresenter registerPresenter;
    private ImageView sponsor_register_image;
    private Button sponsor_chooseFromAlbum;
    public static final int CHOOSE_PHOTO = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_register);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //初始化控件
        init();

        //返回按钮监听器
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //选择头像按钮监听器
        sponsor_chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(sponsorRegister.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(sponsorRegister.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                } else {
                    openAlbum();
                }
            }
        });

        //登录按钮监听器
        register_sponsorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final Sponsor sponsor = new Sponsor();
                Bitmap bitmap = ((BitmapDrawable)sponsor_register_image.getDrawable()).getBitmap();
                sponsor.setEmail(sponsor_email.getText().toString());
                sponsor.setFlag(1);
                sponsor.setOrg_Name(sponsor_orgName.getText().toString());
                sponsor.setPassword(sponsor_password.getText().toString());
                sponsor.setPhoneNum(sponsor_phoneNum.getText().toString());
                sponsor.setSpon_Name(sponsor_name.getText().toString());
                sponsor.setProof(" ");
                sponsor.setUniversity(sponsor_university.getText().toString());
                sponsor.setSponsorImage(ImageToDB.bitmaoToString(bitmap));
                sponsor.setSponsorIntro(sponsor_intro.getText().toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        registerPresenter.sponsor_register(sponsor);
                    }
                }).start();
            }
        });
    }

    private void init(){
        registerPresenter = new registerPresenter(this);
        close = findViewById(R.id.close_sponsor_register);
        progressBar = findViewById(R.id.spon_register_ProgressBar);
        sponsor_email = findViewById(R.id.register_SponsorEmail);
        sponsor_name = findViewById(R.id.register_sponsorName);
        sponsor_orgName = findViewById(R.id.register_org_Name);
        sponsor_password = findViewById(R.id.register_sponsorPassword);
        sponsor_phoneNum = findViewById(R.id.register_sponsorPhone);
        sponsor_university = findViewById(R.id.register_SponsorSchool);
        register_sponsorBtn = findViewById(R.id.sponsor_register_button);
        sponsor_chooseFromAlbum = findViewById(R.id.sponsor_chooseFromAlbum);
        sponsor_register_image = findViewById(R.id.sponsor_register_image);
        sponsor_intro = findViewById(R.id.register_SponsorIntro);

    }

    @Override
    public void user_register_failed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(android.view.View.GONE);
                Toast.makeText(sponsorRegister.this,"用户已存在",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void user_register_success() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(android.view.View.GONE);
                finish();
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
            sponsor_register_image.setImageBitmap(bitmap);
        }
        else
        {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }
}

