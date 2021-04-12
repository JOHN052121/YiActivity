package com.yiactivity.releaseActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.yiactivity.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TagsChoose extends AppCompatActivity {

    private TagFlowLayout tagFlowLayout;
    private ImageView back_icon;
    private Button save_tags;
    private List<Integer> list = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_choose);

        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        tagFlowLayout = findViewById(R.id.tags_flowlayout);
        back_icon = findViewById(R.id.tags_choose_back);
        save_tags = findViewById(R.id.save_choose_tags);

        initList();
        layoutInflater = LayoutInflater.from(this);
        tagFlowLayout.setAdapter(new TagAdapter<String>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) layoutInflater.inflate(R.layout.tags_item,tagFlowLayout,false);
                textView.setText(s);
                return textView;
            }
        });


        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save_tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<Integer> set = tagFlowLayout.getSelectedList();
                String tag_string = "";
                list.addAll(set);
                for(int i=0;i<list.size();i++)
                {
                    String tag = tags.get(list.get(i));
                    tag_string = tag_string + "#"+ tag + " ";
                }
                Intent intent = new Intent();
                intent.putExtra("choose_tags",tag_string);
                setResult(RESULT_OK,intent);
                finish();
            }
        });




    }


    private void initList(){
        tags.add("志愿");
        tags.add("运动");
        tags.add("科技");
        tags.add("互联网");
        tags.add("学术");
        tags.add("法律");
        tags.add("财经");
        tags.add("金融");
        tags.add("辩论赛");
        tags.add("篮球");
        tags.add("足球");
        tags.add("健身");
        tags.add("讲座");
        tags.add("社团");
        tags.add("路演");
        tags.add("计算机竞赛");
        tags.add("法律知识");
        tags.add("勤工助学");
    }
}
