package com.yiactivity.searchTopActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.IpAddress;
import com.yiactivity.Utils.Search_all_activityAdapter;
import com.yiactivity.model.Activity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ShowTopSearchActivity extends Fragment {

    private int mUserId;
    private String mKeyword;
    private RecyclerView recyclerView;
    private ArrayList<Activity> activityArrayList = new ArrayList<>();
    private Search_all_activityAdapter all_activityAdapter;
    private ProgressBar progressBar;
    private ImageView noInformation;

    public ShowTopSearchActivity(int userId,String keyword){
        mUserId = userId;
        mKeyword = keyword;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_search_activity,container,false);
        recyclerView = view.findViewById(R.id.show_search_activity_recycle);
        noInformation = view.findViewById(R.id.show_search_activity_noInformation);
        progressBar = view.findViewById(R.id.show_search_activity_progressBar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSearchActivity(mKeyword);
    }

    private void getSearchActivity(final String keyword){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("keyword",keyword)
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL + "activity/getActivitySearch")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"连接失败，请检查网络",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    JSONArray jsonArray1 = new JSONArray(response.body().string());
                    if(jsonArray1.length() < 1)
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                    }
                    else {
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
                            activityArrayList.add(activity);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                noInformation.setVisibility(View.GONE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                layoutManager.setOrientation(RecyclerView.VERTICAL);
                                recyclerView.setLayoutManager(layoutManager);
                                all_activityAdapter = new Search_all_activityAdapter(activityArrayList,mUserId);
                                recyclerView.setAdapter(all_activityAdapter);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
