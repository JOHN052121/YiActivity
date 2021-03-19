package com.yiactivity.searchTopActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

public class ShowTopSearchActivity extends Fragment {

    private int mUserId;
    private String mKeyword;
    private RecyclerView recyclerView;
    private ArrayList<Activity> activityArrayList = new ArrayList<>();
    private Search_all_activityAdapter all_activityAdapter;
    private ProgressBar progressBar;

    public ShowTopSearchActivity(int userId,String keyword){
        mUserId = userId;
        mKeyword = keyword;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_search_activity,container,false);
        recyclerView = view.findViewById(R.id.show_search_activity_recycle);
        progressBar = view.findViewById(R.id.show_search_activity_progressBar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSearchActivity(mKeyword);
    }

    private void getSearchActivity(final String keyword){
        new Thread(new Runnable() {
            @Override
            public void run() {
                activityArrayList = DBOperation.getTopSearchResult(keyword);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        all_activityAdapter = new Search_all_activityAdapter(activityArrayList,mUserId);
                        recyclerView.setAdapter(all_activityAdapter);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }
}
