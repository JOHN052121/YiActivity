package com.yiactivity.mainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.IpAddress;
import com.yiactivity.Utils.TrendAdapter;
import com.yiactivity.model.Trend;
import com.yiactivity.releaseTrends.ReleaseTrend;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class activitySquareFragment extends Fragment {

    private ImageView release_trend;
    private int mUserId;
    private RecyclerView recyclerView;
    private ArrayList<Trend> trends = new ArrayList<>();
    private TrendAdapter adapter;


    public activitySquareFragment(int userId){
        mUserId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_square,container,false);
        release_trend = view.findViewById(R.id.add_activity_square);
        recyclerView = view.findViewById(R.id.activity_square_recycle);

        release_trend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReleaseTrend.class);
                intent.putExtra("user_id",mUserId);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        trends.clear();
        getTrends(mUserId);
    }

    private void getTrends(final int userId){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(IpAddress.URL + "trend/getTrend")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(getContext(),"连接失败，请检查网络",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    for(int i=0;i<jsonArray.length();i++){
                        Trend trend = new Trend();
                        JSONObject jsonObject  = jsonArray.getJSONObject(i);
                        trend.setId(jsonObject.getInt("id"));
                        trend.setUserName(jsonObject.getString("userName"));
                        trend.setContent(jsonObject.getString("content"));
                        trend.setImage(jsonObject.getString("image"));
                        trend.setUserImg(jsonObject.getString("userImg"));
                        trend.setUserId(jsonObject.getInt("userId"));
                        trends.add(trend);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            layoutManager.setOrientation(RecyclerView.VERTICAL);
                            recyclerView.setLayoutManager(layoutManager);
                            adapter = new TrendAdapter(trends,userId,0);
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
}
