package com.yiactivity.mainScreen;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.yiactivity.R;
import com.yiactivity.Utils.FragmentOrderListAdapter;
import com.yiactivity.mainScreen.searchActivity.*;
import com.yiactivity.searchTopActivity.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class activityListFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditText activity_list_search_false;
    private int mUserId;


    public activityListFragment(int userId){
        mUserId = userId;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list,container,false);
        tabLayout = view.findViewById(R.id.activity_list_tabLayout);
        viewPager = view.findViewById(R.id.activity_list_top_viewPager);
        activity_list_search_false = view.findViewById(R.id.activity_list_search);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new AllActivityList(mUserId));
        fragments.add(new VolunteerActivityList(mUserId));
        fragments.add(new SportActivityList(mUserId));
        fragments.add(new CommunityActivityList(mUserId));
        fragments.add(new CompetitionActivityList(mUserId));
        fragments.add(new LectureActivityList(mUserId));
        FragmentPagerAdapter adapter = new FragmentOrderListAdapter(getFragmentManager(),fragments, new String[]{"全部", "志愿活动", "文体比赛","社团活动","学术比赛","讲座演说"});
        viewPager.setOffscreenPageLimit(6);
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
                textView.setTextSize(20);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.main_top_item);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTypeface(Typeface.DEFAULT);
                textView.setTextSize(16);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);

        activity_list_search_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


}
