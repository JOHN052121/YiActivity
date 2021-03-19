package com.yiactivity.SponsorMain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

public class ManagerActivity extends Fragment {

    private int mSponsorId;
    private RecyclerView recyclerView;
    private ArrayList<Activity> activityArrayList = new ArrayList<>();
    private MangerActivityAdapter adapter;
    private TextView activity_count;

    public ManagerActivity(int sponsorId){
        mSponsorId = sponsorId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manager_activity_sponsor,container,false);
        recyclerView = view.findViewById(R.id.manger_activity_sponsor_recycle);
        activity_count = view.findViewById(R.id.activity_number);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getAllActivityById(mSponsorId);

    }


    //通过sponsorId获取id下的所有活动
    private void getAllActivityById(final int sponsorId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                activityArrayList = DBOperation.getActivityBySponsorId(sponsorId);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new MangerActivityAdapter(activityArrayList,mSponsorId);
                        recyclerView.setAdapter(adapter);
                        activity_count.setText(String.valueOf(activityArrayList.size()));
                    }
                });
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllActivityById(mSponsorId);
    }
}
