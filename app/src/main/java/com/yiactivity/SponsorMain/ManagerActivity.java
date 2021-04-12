package com.yiactivity.SponsorMain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.Search_all_activityAdapter;
import com.yiactivity.model.Activity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ManagerActivity extends Fragment {

    private int mSponsorId;
    private RecyclerView recyclerView;
    private ArrayList<Activity> activityArrayList = new ArrayList<>();
    private MangerActivityAdapter adapter;
    private TextView activity_count;
    private static String URL = "http://192.168.1.111:8080/SSM_war_exploded/";

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

    //通过sponsorId获取id下的所有活动
    private void getAllActivityById(final int sponsorId){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("sponsorId",String.valueOf(sponsorId))
                .build();
        Request request = new Request.Builder()
                .url(URL + "activity/getActivityBySponsorId")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"连接失败请检查网络",Toast.LENGTH_SHORT).show();
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
                        activityArrayList.add(activity);
                    }
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
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        activityArrayList.clear();
        getAllActivityById(mSponsorId);
    }
}
