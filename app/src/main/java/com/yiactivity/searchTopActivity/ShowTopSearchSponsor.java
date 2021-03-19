package com.yiactivity.searchTopActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.Search_all_activityAdapter;
import com.yiactivity.model.Sponsor;

import java.util.ArrayList;

public class ShowTopSearchSponsor extends Fragment {

    private int mUserId;
    private String mKeyword;
    private RecyclerView recyclerView;
    private ArrayList<Sponsor> sponsorArrayList = new ArrayList<>();
    private TopSearchSponsorAdapter adapter;
    private ProgressBar progressBar;

    public ShowTopSearchSponsor(int userId,String keyword){
        mUserId = userId;
        mKeyword = keyword;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_search_sponsor,container,false);
        recyclerView = view.findViewById(R.id.show_search_sponsor_recycle);
        progressBar = view.findViewById(R.id.show_search_sponsor_progressBar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSearchSponsor(mKeyword);
    }

    private void getSearchSponsor(final String keyword){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sponsorArrayList = DBOperation.getTopSearchSponsor(keyword);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter= new TopSearchSponsorAdapter(sponsorArrayList,mUserId);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }
}
