package com.yiactivity.mainScreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.yiactivity.R;
import com.yiactivity.Utils.*;
import com.yiactivity.mainScreen.searchActivity.VolunteerActivityList;
import com.yiactivity.model.Activity;
import com.yiactivity.model.Sponsor;
import com.yiactivity.searchTopActivity.SearchActivity;
import com.youth.banner.Banner;
import com.yiactivity.mainScreen.typeActivity.diffTypeActivity;
import java.util.ArrayList;
import java.util.List;

public class homeFragment extends Fragment {

    private ArrayList<String> bannerImgs = new ArrayList<>();
    private ArrayList<Bitmap> bannerImgs_bit = new ArrayList<>();
    private ArrayList<Activity> activityArrayList_recommend = new ArrayList<>();
    private ArrayList<Activity> activityArrayList_volunteer = new ArrayList<>();
    private ArrayList<Sponsor> sponsorArrayList = new ArrayList<>();
    private ActivityAdapter activityAdapter_recommend;
    private ActivityAdapter activityAdapter_volunteer;
    private sponsorRandomAdapter sponsorRandomAdapter;
    private RecyclerView recyclerView_recommend;
    private RecyclerView recyclerView_volunteer;
    private RecyclerView recyclerView_randomSponsor;
    private ProgressBar homeProgressBar;
    private ProgressBar progressIndicator;
    private float scrollX;
    private float percentage;
    private int mUserId;
    private int totalDistanceX;
    private SwipeRefreshLayout swipeRefresh;
    private EditText search_false;
    private ImageView volunteer;
    private ImageView sport;
    private ImageView lecture;
    private ImageView competiton;
    private ImageView community;


    public homeFragment(int userId) {
        mUserId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        List images = new ArrayList();
        images.add("file:///android_asset/poster1.jpg");
        images.add("file:///android_asset/poster2.jpg");
        Banner banner = view.findViewById(R.id.banner);
        banner.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 30);
            }
        });
        banner.setClipToOutline(true);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(images);
        banner.start();
        recyclerView_recommend = view.findViewById(R.id.recyclerView_recommend);
        recyclerView_volunteer = view.findViewById(R.id.recyclerView_volunteer_home);
        homeProgressBar = view.findViewById(R.id.home_progressBar);
        homeProgressBar.setVisibility(View.VISIBLE);
        progressIndicator = view.findViewById(R.id.H_indicator);
        swipeRefresh = view.findViewById(R.id.home_swipe_refresh);
        recyclerView_randomSponsor = view.findViewById(R.id.sponsor_random_recyclerView);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        getRecommendActivity();


        //顶部搜索框通过intent跳转至searchActivity
        search_false = view.findViewById(R.id.search_false);
        search_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("user_id",mUserId);
                startActivity(intent);
            }
        });

        //滑动指示器
        recyclerView_recommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int xOffset = recyclerView.computeHorizontalScrollOffset();
                int range = recyclerView.computeHorizontalScrollRange();
                int extent = recyclerView.computeHorizontalScrollExtent();
                totalDistanceX = range - extent;
                if (percentage == 0)//记录上一次的滑动百分比
                    percentage = xOffset * 1f / totalDistanceX;

                scrollX = percentage * totalDistanceX + dx;
                if (xOffset == 0)
                    scrollX = 0;
                if (xOffset == totalDistanceX)
                    scrollX = xOffset;

                percentage = scrollX / totalDistanceX;

                //此处并不能获取rv的精准滑动距离 先这样
                progressIndicator.setMax(totalDistanceX);
                progressIndicator.setProgress((int) scrollX + totalDistanceX / 8);

                super.onScrolled(recyclerView, dx, dy);
            }
        });

        //刷新监视器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRecommendActivity();
                swipeRefresh.setRefreshing(false);
            }
        });

        //五类活动图案点击监听器
        volunteer = view.findViewById(R.id.volunteer);
        sport = view.findViewById(R.id.sport);
        lecture = view.findViewById(R.id.lecture);
        competiton = view.findViewById(R.id.competition);
        community = view.findViewById(R.id.community);

        volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),diffTypeActivity.class);
                intent.putExtra("user_id",mUserId);
                intent.putExtra("type","志愿活动类");
                startActivity(intent);
            }
        });

        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),diffTypeActivity.class);
                intent.putExtra("user_id",mUserId);
                intent.putExtra("type","文体比赛类");
                startActivity(intent);
            }
        });

        lecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),diffTypeActivity.class);
                intent.putExtra("user_id",mUserId);
                intent.putExtra("type","讲座演说类");
                startActivity(intent);
            }
        });

        competiton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),diffTypeActivity.class);
                intent.putExtra("user_id",mUserId);
                intent.putExtra("type","学术竞赛类");
                startActivity(intent);
            }
        });

        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),diffTypeActivity.class);
                intent.putExtra("user_id",mUserId);
                intent.putExtra("type","社团活动类");
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap;
                bannerImgs = DBOperation.getBannerImg();
                for (String bannerImg : bannerImgs) {
                    bitmap = ImageToDB.stringToBitmap(bannerImg);
                    bannerImgs_bit.add(bitmap);
                }
            }
        }).start();*/
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void getRecommendActivity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                activityArrayList_recommend = DBOperation.getRecommendActivity();
                activityArrayList_volunteer = DBOperation.getRecommendActivity();
                sponsorArrayList = DBOperation.getRandomSponsor();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GridLayoutManager layoutManager_recommend = new GridLayoutManager(getContext(), 2);
                        layoutManager_recommend.setOrientation(RecyclerView.HORIZONTAL);
                        recyclerView_recommend.setLayoutManager(layoutManager_recommend);
                        activityAdapter_recommend = new ActivityAdapter(activityArrayList_recommend, mUserId);
                        recyclerView_recommend.setAdapter(activityAdapter_recommend);

                        GridLayoutManager layoutManager_volunteer = new GridLayoutManager(getContext(), 2);
                        layoutManager_volunteer.setOrientation(RecyclerView.HORIZONTAL);
                        recyclerView_volunteer.setLayoutManager(layoutManager_volunteer);
                        activityAdapter_volunteer = new ActivityAdapter(activityArrayList_volunteer, mUserId);
                        recyclerView_volunteer.setAdapter(activityAdapter_volunteer);

                        LinearLayoutManager layoutManager_randomSponsor = new LinearLayoutManager(getContext());
                        layoutManager_randomSponsor.setOrientation(RecyclerView.HORIZONTAL);
                        recyclerView_randomSponsor.setLayoutManager(layoutManager_randomSponsor);
                        sponsorRandomAdapter = new sponsorRandomAdapter(sponsorArrayList,mUserId);
                        recyclerView_randomSponsor.setAdapter(sponsorRandomAdapter);

                        homeProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }

}