package com.yiactivity.mainScreen.searchActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.EndlessRecyclerOnScrollListener;
import com.yiactivity.Utils.LoadMoreAdapter;
import com.yiactivity.Utils.Search_all_activityAdapter;
import com.yiactivity.model.Activity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class LectureActivityList extends Fragment {

    private ArrayList<Activity> all_activity_list = new ArrayList<>();
    private RecyclerView all_recyclerView;
    private Search_all_activityAdapter all_activityAdapter;
    private String type = "讲座演说类";
    private int mUserId;
    private int begin = 1;
    private int end = 5;
    ArrayList<Activity> currentList = new ArrayList<>();
    private int count = 6;
    private LoadMoreAdapter loadMoreAdapter;


    public LectureActivityList(int userId){
        mUserId = userId;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lecture_activity_list,container,false);
        all_recyclerView = view.findViewById(R.id.lecture_activity_list_recycle);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLectureActivity();
    }

    public void getLectureActivity(){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody =new FormBody.Builder()
                .add("type",type)
                .add("begin","1")
                .add("end","5")
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
                        all_activity_list.add(activity);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            layoutManager.setOrientation(RecyclerView.VERTICAL);
                            all_recyclerView.setLayoutManager(layoutManager);
                            loadMoreAdapter = new LoadMoreAdapter(all_activity_list,mUserId);
                            all_recyclerView.setAdapter(loadMoreAdapter);

                            all_recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
                                @Override
                                public void onLoadMore() {
                                    loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING);
                                    if(all_activity_list.size() < 5){
                                        loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                                    }
                                    else{
                                        if (all_activity_list.size() < count) {
                                            begin = begin + 5;
                                            end = end + 5;
                                            OkHttpClient client1 = new OkHttpClient();
                                            RequestBody requestBody1 = new FormBody.Builder()
                                                    .add("type",type)
                                                    .add("begin",String.valueOf(begin))
                                                    .add("end",String.valueOf(end))
                                                    .build();
                                            Request request1 = new Request.Builder()
                                                    .url("http://192.168.1.111:8080/SSM_war_exploded/activity/typeActivity")
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
                                                                    all_activity_list.addAll(currentList);
                                                                    count = count + currentList.size();
                                                                    loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
                                                                    currentList.clear();
                                                                }
                                                                else {
                                                                    if(currentList.size() == 0){
                                                                        loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                                                                    }
                                                                    else {
                                                                        all_activity_list.addAll(currentList);
                                                                        count = all_activity_list.size();
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
                            });
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}
