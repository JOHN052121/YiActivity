package com.yiactivity.searchTopActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import com.yiactivity.R;
import com.yiactivity.Utils.FlowLayout;
import com.yiactivity.Utils.searchHistory;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ImageView back_button;
    private EditText search_keyword;
    private String keyword;
    private int mUserId;
    private List<String> historyList  = new ArrayList<>();
    private FlowLayout flowLayout;
    private ImageView delete_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent getIntent = getIntent();
        mUserId = getIntent.getIntExtra("user_id",0);
        flowLayout = findViewById(R.id.activity_search_flow);
        delete_history = findViewById(R.id.delete_search_history);
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //初始化控件
        back_button = findViewById(R.id.search_return_button);
        search_keyword = findViewById(R.id.search_activity_keyword);

        //返回按钮点击事件
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //关键字和用户id获取并传递至ShowSearchActivity
        search_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    keyword = search_keyword.getText().toString();
                    searchHistory.saveSearchHistory(keyword,getApplicationContext(),mUserId);
                    final Intent intent = new Intent(SearchActivity.this,ShowSearchActivity.class);
                    Log.d("调试",keyword);
                    intent.putExtra("search_keyword",keyword);
                    intent.putExtra("user_id",mUserId);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        delete_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchHistory.clearSearchHistory(getApplicationContext(),mUserId);
                flowLayout.removeAllViews();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        historyList = searchHistory.getSearchHistory(getApplicationContext(),mUserId);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 15, 10, 15);
        if (flowLayout != null) {
            flowLayout.removeAllViews();
        }
        for (int i = 0; i < historyList.size(); i++) {
            final int j = i;
            TextView tv = new TextView(this);
            CardView cardView = new CardView(this);
            tv.setPadding(28, 10, 28, 10);
            tv.setText(historyList.get(i));
            tv.setMaxEms(10);
            tv.setSingleLine();
            tv.setBackgroundColor(Color.parseColor("#ffffff"));
            tv.setLayoutParams(layoutParams);
            cardView.addView(tv);
            cardView.setElevation(7);
            cardView.setRadius(8);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search_keyword.setText(historyList.get(j));
                }
            });
            flowLayout.addView(cardView, layoutParams);
        }
    }
}
