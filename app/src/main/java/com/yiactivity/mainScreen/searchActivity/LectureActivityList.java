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
import com.yiactivity.Utils.Search_all_activityAdapter;
import com.yiactivity.model.Activity;

import java.util.ArrayList;

public class LectureActivityList extends Fragment {

    private ArrayList<Activity> all_activity_list = new ArrayList<>();
    private RecyclerView all_recyclerView;
    private Search_all_activityAdapter all_activityAdapter;
    private String type = "讲座演说类";
    private int mUserId;


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
                        all_activityAdapter = new Search_all_activityAdapter(all_activity_list,mUserId);
                        all_recyclerView.setAdapter(all_activityAdapter);

                    }
                });
            }
        }).start();
    }
}
