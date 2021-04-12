package com.yiactivity.detailOfUser;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.FragmentOrderListAdapter;
import com.yiactivity.Utils.IpAddress;
import com.yiactivity.mainScreen.searchActivity.AllActivityList;
import com.yiactivity.mainScreen.searchActivity.SportActivityList;
import com.yiactivity.mainScreen.searchActivity.VolunteerActivityList;
import com.yiactivity.model.User;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailUserMain extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView user_img;
    private TextView user_name_title;
    private TextView user_name;
    private TextView user_sign;
    private TextView user_subscribe_number;
    private TextView user_trend_number;
    private TextView user_collect_number;
    private ImageView back_icon;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //系统栏设置
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        setContentView(R.layout.activity_detail_user_main);

        //控制初始化
        init();

        //返回按钮
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        mUserId = intent.getIntExtra("user_id",0);

        //用户信息填充
        getUserInfo(mUserId);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new trendFragment(mUserId));
        fragments.add(new collectFragment());
        fragments.add(new subscribeFragment(mUserId));
        FragmentPagerAdapter adapter = new FragmentOrderListAdapter(getSupportFragmentManager(),fragments, new String[]{"动态", "收藏", "关注主办方"});
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.main_top_item);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextColor( tabLayout.getTabTextColors());
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setTextSize(14);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.main_top_item);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTypeface(Typeface.DEFAULT);
                textView.setTextSize(14);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo(mUserId);
    }

    private void init(){
        tabLayout = findViewById(R.id.detail_user_tabLayout);
        viewPager = findViewById(R.id.detail_user_viewPager);
        user_img = findViewById(R.id.detail_user_img);
        user_name_title = findViewById(R.id.detail_user_name_title);
        user_name = findViewById(R.id.detail_user_name);
        user_sign = findViewById(R.id.detail_user_sign);
        user_subscribe_number = findViewById(R.id.detail_user_subscribe);
        user_trend_number = findViewById(R.id.detail_user_trend);
        user_collect_number = findViewById(R.id.detail_user_collect);
        back_icon = findViewById(R.id.detail_user_back);
    }

    private void getUserInfo(final int userId) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userId",String.valueOf(userId))
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL + "user/getUserById")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(DetailUserMain.this,"连接失败，请检查网络",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    final User user = new User();
                    user.setUserId(jsonObject.getInt("userId"));
                    user.setUserName(jsonObject.getString("userName"));
                    user.setPhoneNum(jsonObject.getString("phoneNum"));
                    user.setGender(jsonObject.getString("gender"));
                    user.setUniversity(jsonObject.getString("university"));
                    user.setEmail(jsonObject.getString("email"));
                    user.setUserImage(jsonObject.getString("image"));
                    user.setSubscribeNum(jsonObject.getInt("subscribeNum"));
                    user.setCollectionNum(jsonObject.getInt("collectionNum"));
                    user.setTrendNum(jsonObject.getInt("trendNum"));
                    user.setPassword(jsonObject.getString("password"));
                    user.setSign(jsonObject.getString("sign"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(DetailUserMain.this).load(IpAddress.URL_PIC+"userImage/"+user.getUserImage()).into(user_img);
                            user_name.setText(user.getUserName());
                            user_name_title.setText(user.getUserName());
                            if (user.getSign().equals("nothing")) {
                                user_sign.setText("签名: 这个人很懒");
                            } else {
                                user_sign.setText(user.getSign());
                            }
                            user_collect_number.setText(String.valueOf(user.getCollectionNum()));
                            user_trend_number.setText(String.valueOf(user.getTrendNum()));
                            user_subscribe_number.setText(String.valueOf(user.getSubscribeNum()));
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

}
