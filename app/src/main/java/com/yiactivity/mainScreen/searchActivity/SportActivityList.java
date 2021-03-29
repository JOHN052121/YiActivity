package com.yiactivity.mainScreen.searchActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

public class SportActivityList extends Fragment {

    private ArrayList<Activity> all_activity_list = new ArrayList<>();
    private RecyclerView all_recyclerView;
    private Search_all_activityAdapter all_activityAdapter;
    private String type = "文体比赛类";
    private int mUserId;
    private int begin = 1;
    private int end = 5;
    ArrayList<Activity> currentList = new ArrayList<>();
    private int count = 6;
    private LoadMoreAdapter loadMoreAdapter;
    public SportActivityList(int userId){
        mUserId = userId;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sport_activity_list,container,false);
        all_recyclerView = view.findViewById(R.id.sport_activity_list_recycle);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSportActivity();
    }

    public void getSportActivity(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                all_activity_list = DBOperation.getDiffTypeActivity(type);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        all_recyclerView.setLayoutManager(layoutManager);
                        //all_activityAdapter = new Search_all_activityAdapter(all_activity_list,mUserId);
                        //all_recyclerView.setAdapter(all_activityAdapter);
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
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                begin = begin + 5;
                                                end = end + 5;
                                                currentList = DBOperation.getAllActivity(begin,end);
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if(currentList.size() > 4){
                                                            all_activity_list.addAll(currentList);
                                                            count = count + currentList.size();
                                                            loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
                                                        }
                                                        else {
                                                            if(currentList.size() == 0){
                                                                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                                                            }
                                                            else {
                                                                all_activity_list.addAll(currentList);
                                                                count = all_activity_list.size();
                                                                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
                                                            }
                                                        }
                                                    }
                                                });

                                            }
                                        }).start();
                                    } else {
                                        // 显示加载到底的提示
                                        loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                                    }
                                }

                            }
                        });

                    }
                });
            }
        }).start();
    }
}
