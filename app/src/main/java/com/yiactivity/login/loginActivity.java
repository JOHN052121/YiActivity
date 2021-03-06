package com.yiactivity.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.yiactivity.SponsorMain.SponsorMainScreen;
import com.yiactivity.mainScreen.MainActivity;
import com.yiactivity.model.Sponsor;
import com.yiactivity.model.User;
import com.yiactivity.register.registerActivity;
import com.yiactivity.R;

public class loginActivity extends AppCompatActivity implements loginContract.View{

    private EditText phoneEdit;
    private EditText passwordEdit;
    private Button loginButton;
    private loginPresenter loginPresenter;
    private String userPhone;
    private String password;
    private RadioButton loginAsUser;
    private RadioButton loginAsSponsor;
    private TextView register;
    private RadioGroup choose;
    private ProgressBar progressBar;
    private User user;
    private Sponsor sponsor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        TextView textView = findViewById(R.id.logo_text);
        Typeface type = Typeface.createFromAsset(this.getAssets(), "twoFont.ttf");
        textView.setTypeface(type);
        phoneEdit = findViewById(R.id.login_phone);
        passwordEdit = findViewById(R.id.login_password);
        register = findViewById(R.id.login_register);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);
        choose = findViewById(R.id.choose);
        loginAsSponsor = findViewById(R.id.asSponsor_login);
        loginAsUser = findViewById(R.id.asUser_login);
        loginPresenter = new loginPresenter(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this,registerActivity.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPhone = phoneEdit.getText().toString();
                password = passwordEdit.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if(choose.getCheckedRadioButtonId() == loginAsUser.getId()) {
                    loginPresenter.LoginAsUser(userPhone,password);
                }
                else if(choose.getCheckedRadioButtonId() == loginAsSponsor.getId()){
                    loginPresenter.LoginAsSponsor(userPhone,password);
                }
            }
        });
    }

    @Override
    public void loginSuccess(final User user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(loginActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(loginActivity.this, MainActivity.class);
                Log.d("??????",user.getUserName());
                intent.putExtra("user_data",user);
                startActivity(intent);
            }
        });
    }

    @Override
    public void loginFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(loginActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void loginSuccessAsSponsor(final Sponsor sponsor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(loginActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(loginActivity.this, SponsorMainScreen.class);
                intent.putExtra("sponsor_data",sponsor);
                startActivity(intent);
            }
        });

    }
}
