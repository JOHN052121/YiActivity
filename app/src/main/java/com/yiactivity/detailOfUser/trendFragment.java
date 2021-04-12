package com.yiactivity.detailOfUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yiactivity.R;
import com.yiactivity.Utils.IpAddress;
import com.yiactivity.Utils.TrendAdapter;
import com.yiactivity.model.Trend;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class trendFragment extends Fragment {

    public TrendAdapter trendAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Trend> trends = new ArrayList<>();
    private ImageView imageView;
    private int mUserId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trend_fragment,container,false);
        recyclerView = view.findViewById(R.id.trend_fragment_recycle);
        imageView = view.findViewById(R.id.trend_image);
        return view;
    }

    public trendFragment(int userId){
        mUserId = userId;
    }

    private void getTrendByUserId(final int userId){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userId",String.valueOf(userId))
                .build();
        Request request  = new Request.Builder()
                .url(IpAddress.URL + "trend/getTrendByUserId")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(getContext(),"连接失败，请检查网络！",Toast.LENGTH_SHORT).show();
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
                            if(trends.size() <1 ){
                                recyclerView.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                            }
                            else {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                layoutManager.setOrientation(RecyclerView.VERTICAL);
                                recyclerView.setLayoutManager(layoutManager);
                                trendAdapter = new TrendAdapter(trends,userId,1);
                                recyclerView.setAdapter(trendAdapter);
                            }

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
        trends.clear();
        getTrendByUserId(mUserId);
    }

}
