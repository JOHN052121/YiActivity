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
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.gson.JsonArray;
import com.yiactivity.R;
import com.yiactivity.Services.UpdateStateService;
import com.yiactivity.Utils.*;
import com.yiactivity.mainScreen.searchActivity.VolunteerActivityList;
import com.yiactivity.model.Activity;
import com.yiactivity.model.Sponsor;
import com.yiactivity.searchTopActivity.SearchActivity;
import com.youth.banner.Banner;
import com.yiactivity.mainScreen.typeActivity.diffTypeActivity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class homeFragment extends Fragment {

    private ArrayList<String> bannerImgs = new ArrayList<>();
    private ArrayList<Bitmap> bannerImgs_bit = new ArrayList<>();
    private ArrayList<Activity> activityArrayList_recommend = new ArrayList<>();
    private ArrayList<Activity> activityArrayList_volunteer = new ArrayList<>();
    private ArrayList<Activity> activityArrayList_popular = new ArrayList<>();
    private ArrayList<Sponsor> sponsorArrayList = new ArrayList<>();
    private ArrayList<Activity> activityArrayList_school = new ArrayList<>();
    private ActivityAdapter activityAdapter_recommend;
    private ActivityAdapter activityAdapter_volunteer;
    private PopularAdapter activityAdapter_popular;
    private sponsorRandomAdapter sponsorRandomAdapter;
    private LoadMoreAdapter loadMoreAdapter;
    private RecyclerView recyclerView_recommend;
    private RecyclerView recyclerView_volunteer;
    private RecyclerView recyclerView_randomSponsor;
    private RecyclerView recyclerView_popular;
    private RecyclerView recyclerView_school;
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
    private ScrollView scrollView;
    private List<Activity> currentList = new ArrayList<>();
    private int begin = 1;
    private int end = 5;
    private int count = 6;

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
        recyclerView_popular = view.findViewById(R.id.recyclerView_popular_home);
        recyclerView_school = view.findViewById(R.id.school_activity_recycler);
        homeProgressBar = view.findViewById(R.id.home_progressBar);
        homeProgressBar.setVisibility(View.VISIBLE);
        progressIndicator = view.findViewById(R.id.H_indicator);
        swipeRefresh = view.findViewById(R.id.home_swipe_refresh);
        recyclerView_randomSponsor = view.findViewById(R.id.sponsor_random_recyclerView);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        scrollView = view.findViewById(R.id.home_fragment_scroll);


        getRecommend();
        getVolunteer();
        getPopular();
        getSchool();
        getSponsor();
        homeProgressBar.setVisibility(View.GONE);


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
                sponsorArrayList.clear();
                getSponsor();
                homeProgressBar.setVisibility(View.GONE);
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void getRecommend(){
        OkHttpClient client  = new OkHttpClient();
        RequestBody requestBody =new FormBody.Builder()
                .add("begin","1")
                .add("end","8")
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.1.111:8080/SSM_war_exploded/activity/allActivity")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"连接失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    final JSONArray jsonArray = new JSONArray(response.body().string());
                    for(int i = 0;i<jsonArray.length();i++){
                        Activity activity = new Activity();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        activity.setActivityId(jsonObject.getInt("activityId"));
                        activity.setActivityName(jsonObject.getString("activityName"));
                        activity.setTime(jsonObject.getString("time"));
                        activity.setAddress(jsonObject.getString("address"));
                        activity.setAddressContent(jsonObject.getString("activityContent"));
                        activity.setType(jsonObject.getString("type"));
                        activity.setState(jsonObject.getInt("state"));
                        activity.setSponsorId(jsonObject.getInt("sponsorId"));
                        activity.setPoster2(jsonObject.getString("poster"));
                        activity.setBrowserCount(jsonObject.getInt("browserCount"));
                        activity.setTag(jsonObject.getString("tag"));
                        activity.setParticipant(jsonObject.getInt("participant"));
                        activityArrayList_recommend.add(activity);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GridLayoutManager layoutManager_recommend = new GridLayoutManager(getContext(), 2);
                            layoutManager_recommend.setOrientation(RecyclerView.HORIZONTAL);
                            recyclerView_recommend.setLayoutManager(layoutManager_recommend);
                            activityAdapter_recommend = new ActivityAdapter(activityArrayList_recommend, mUserId);
                            recyclerView_recommend.setAdapter(activityAdapter_recommend);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getVolunteer(){
        OkHttpClient client  = new OkHttpClient();
        RequestBody requestBody =new FormBody.Builder()
                .add("type","志愿活动类")
                .add("begin","1")
                .add("end","8")
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.1.111:8080/SSM_war_exploded/activity/typeActivity")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"连接失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    final JSONArray jsonArray = new JSONArray(response.body().string());
                    for(int i = 0;i<jsonArray.length();i++){
                        Activity activity = new Activity();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        activity.setActivityId(jsonObject.getInt("activityId"));
                        activity.setActivityName(jsonObject.getString("activityName"));
                        activity.setTime(jsonObject.getString("time"));
                        activity.setAddress(jsonObject.getString("address"));
                        activity.setAddressContent(jsonObject.getString("activityContent"));
                        activity.setType(jsonObject.getString("type"));
                        activity.setState(jsonObject.getInt("state"));
                        activity.setSponsorId(jsonObject.getInt("sponsorId"));
                        activity.setPoster2(jsonObject.getString("poster"));
                        activity.setBrowserCount(jsonObject.getInt("browserCount"));
                        activity.setTag(jsonObject.getString("tag"));
                        activity.setParticipant(jsonObject.getInt("participant"));
                        activityArrayList_volunteer.add(activity);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GridLayoutManager layoutManager_volunteer = new GridLayoutManager(getContext(), 2);
                            layoutManager_volunteer.setOrientation(RecyclerView.HORIZONTAL);
                            recyclerView_volunteer.setLayoutManager(layoutManager_volunteer);
                            activityAdapter_volunteer = new ActivityAdapter(activityArrayList_volunteer, mUserId);
                            recyclerView_volunteer.setAdapter(activityAdapter_volunteer);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getPopular(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.1.111:8080/SSM_war_exploded/activity/popularActivity")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"连接失败请重新刷新",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    final JSONArray jsonArray = new JSONArray(response.body().string());
                    for(int i = 0;i<jsonArray.length();i++){
                        Activity activity = new Activity();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        activity.setActivityId(jsonObject.getInt("activityId"));
                        activity.setActivityName(jsonObject.getString("activityName"));
                        activity.setTime(jsonObject.getString("time"));
                        activity.setAddress(jsonObject.getString("address"));
                        activity.setAddressContent(jsonObject.getString("activityContent"));
                        activity.setType(jsonObject.getString("type"));
                        activity.setState(jsonObject.getInt("state"));
                        activity.setSponsorId(jsonObject.getInt("sponsorId"));
                        activity.setPoster2(jsonObject.getString("poster"));
                        activity.setBrowserCount(jsonObject.getInt("browserCount"));
                        activity.setTag(jsonObject.getString("tag"));
                        activity.setParticipant(jsonObject.getInt("participant"));
                        activityArrayList_popular.add(activity);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager layoutManager_popular = new LinearLayoutManager(getContext());
                            layoutManager_popular.setOrientation(RecyclerView.VERTICAL);
                            recyclerView_popular.setLayoutManager(layoutManager_popular);
                            recyclerView_popular.setNestedScrollingEnabled(false);
                            activityAdapter_popular = new PopularAdapter(activityArrayList_popular,mUserId);
                            recyclerView_popular.setAdapter(activityAdapter_popular);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getSchool(){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody =new FormBody.Builder()
                .add("userId",String.valueOf(mUserId))
                .add("begin","1")
                .add("end","5")
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.1.111:8080/SSM_war_exploded/activity/schoolActivity")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"连接失败请重新刷新页面",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    final JSONArray jsonArray = new JSONArray(response.body().string());
                    for(int i = 0;i<jsonArray.length();i++){
                        Activity activity = new Activity();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        activity.setActivityId(jsonObject.getInt("activityId"));
                        activity.setActivityName(jsonObject.getString("activityName"));
                        activity.setTime(jsonObject.getString("time"));
                        activity.setAddress(jsonObject.getString("address"));
                        activity.setAddressContent(jsonObject.getString("activityContent"));
                        activity.setType(jsonObject.getString("type"));
                        activity.setState(jsonObject.getInt("state"));
                        activity.setSponsorId(jsonObject.getInt("sponsorId"));
                        activity.setPoster2(jsonObject.getString("poster"));
                        activity.setBrowserCount(jsonObject.getInt("browserCount"));
                        activity.setTag(jsonObject.getString("tag"));
                        activity.setParticipant(jsonObject.getInt("participant"));
                        activityArrayList_school.add(activity);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager layoutManager_school = new LinearLayoutManager(getContext());
                            layoutManager_school.setOrientation(RecyclerView.VERTICAL);
                            recyclerView_school.setLayoutManager(layoutManager_school);
                            recyclerView_school.setNestedScrollingEnabled(false);
                            loadMoreAdapter = new LoadMoreAdapter(activityArrayList_school, mUserId);
                            recyclerView_school.setAdapter(loadMoreAdapter);

                            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                                @Override
                                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                    View contentView = scrollView.getChildAt(0);
                                    if(contentView != null && contentView.getMeasuredHeight() == (scrollView.getScrollY() + scrollView.getHeight())){
                                        loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING);
                                        if(activityArrayList_school.size() < 5){
                                            loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                                        }
                                        else{
                                            if (activityArrayList_school.size() < count) {
                                                begin = begin + 5;
                                                end = end + 5;
                                                OkHttpClient client1 = new OkHttpClient();
                                                RequestBody requestBody1 = new FormBody.Builder()
                                                        .add("userId",String.valueOf(mUserId))
                                                        .add("begin",String.valueOf(begin))
                                                        .add("end",String.valueOf(end))
                                                        .build();
                                                Request request1 = new Request.Builder()
                                                        .url("http://192.168.1.111:8080/SSM_war_exploded/activity/schoolActivity")
                                                        .post(requestBody1)
                                                        .build();
                                                client1.newCall(request1).enqueue(new Callback() {
                                                    @Override
                                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(getContext(),"加载数据失败，请检查网络或者重新加载",Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                                        try{
                                                            JSONArray jsonArray1 = new JSONArray(response.body().string());
                                                            for(int i = 0;i<jsonArray1.length();i++){
                                                                Activity activity = new Activity();
                                                                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                                                                activity.setActivityId(jsonObject.getInt("activityId"));
                                                                activity.setActivityName(jsonObject.getString("activityName"));
                                                                activity.setTime(jsonObject.getString("time"));
                                                                activity.setAddress(jsonObject.getString("address"));
                                                                activity.setAddressContent(jsonObject.getString("activityContent"));
                                                                activity.setType(jsonObject.getString("type"));
                                                                activity.setState(jsonObject.getInt("state"));
                                                                activity.setSponsorId(jsonObject.getInt("sponsorId"));
                                                                activity.setPoster2(jsonObject.getString("poster"));
                                                                activity.setBrowserCount(jsonObject.getInt("browserCount"));
                                                                activity.setTag(jsonObject.getString("tag"));
                                                                activity.setParticipant(jsonObject.getInt("participant"));
                                                                currentList.add(activity);
                                                            }
                                                            getActivity().runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    if(currentList.size() > 4){
                                                                        activityArrayList_school.addAll(currentList);
                                                                        count = count + currentList.size();
                                                                        loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
                                                                        currentList.clear();
                                                                    }
                                                                    else {
                                                                        if(currentList.size() == 0){
                                                                            loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                                                                        }
                                                                        else {
                                                                            activityArrayList_school.addAll(currentList);
                                                                            count = activityArrayList_school.size();
                                                                            loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
                                                                            currentList.clear();
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                        }catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }
                                            else {
                                                // 显示加载到底的提示
                                                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                                            }
                                        }
                                    }
                                }
                            });

                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getSponsor(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.1.111:8080/SSM_war_exploded/sponsor/getSponsorRandom")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"连接失败请重新刷新页面",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    for(int i=0;i<jsonArray.length();i++){
                        Sponsor sponsor = new Sponsor();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        sponsor.setSponsorId(jsonObject.getInt("sponsorId"));
                        sponsor.setSpon_Name(jsonObject.getString("spon_Name"));
                        sponsor.setOrg_Name(jsonObject.getString("org_Name"));
                        sponsor.setPassword(jsonObject.getString("password"));
                        sponsor.setUniversity(jsonObject.getString("university"));
                        sponsor.setPhoneNum(jsonObject.getString("phoneNum"));
                        sponsor.setEmail(jsonObject.getString("email"));
                        sponsor.setSponsorImage1(jsonObject.getString("sponsorImage"));
                        sponsor.setSponsorIntro(jsonObject.getString("sponIntro"));
                        sponsor.setActivityNumOfSponsor(jsonObject.getInt("activityNum"));
                        sponsor.setFuns(jsonObject.getInt("funs"));
                        sponsorArrayList.add(sponsor);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager layoutManager_randomSponsor = new LinearLayoutManager(getContext());
                            layoutManager_randomSponsor.setOrientation(RecyclerView.HORIZONTAL);
                            recyclerView_randomSponsor.setLayoutManager(layoutManager_randomSponsor);
                            sponsorRandomAdapter = new sponsorRandomAdapter(sponsorArrayList,mUserId);
                            recyclerView_randomSponsor.setAdapter(sponsorRandomAdapter);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
