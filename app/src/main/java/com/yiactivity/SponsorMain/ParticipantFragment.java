package com.yiactivity.SponsorMain;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.IpAddress;
import com.yiactivity.model.User;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class ParticipantFragment extends Fragment {

    private int mSponsorId;
    private ImageView participant_back_button;
    private RecyclerView recyclerView;
    private ArrayList<User> userArrayList = new ArrayList<>();
    private ParticipantAdapter participantAdapter;
    private int mActivityId;
    private TextView participant_title;
    private String mActivityName;

    public ParticipantFragment(int sponsorId,int activityId,String activityName){
        mSponsorId = sponsorId;
        mActivityId = activityId;
        mActivityName = activityName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.participant_fragment,container,false);
        participant_back_button = view.findViewById(R.id.participant_fragment_back);
        recyclerView = view.findViewById(R.id.participant_fragment_recycle);
        participant_title = view.findViewById(R.id.participant_fragment_title);
        participant_title.setText(mActivityName);
        //返回键监听
        participant_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getParticipant(final int activityId){
        OkHttpClient client  = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("activityId",String.valueOf(activityId))
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL + "user/getUserByActivityId")
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
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        User user = new User();
                        user.setUserId(jsonObject.getInt("userId"));
                        user.setUserName(jsonObject.getString("userName"));
                        user.setPhoneNum(jsonObject.getString("phoneNum"));
                        user.setGender(jsonObject.getString("gender"));
                        user.setUniversity(jsonObject.getString("university"));
                        user.setEmail(jsonObject.getString("email"));
                        user.setUserImage(jsonObject.getString("image"));
                        user.setSubscribeNum(jsonObject.getInt("subscribeNum"));
                        user.setCollectionNum(jsonObject.getInt("collectionNum"));
                        user.setTrendNum(jsonObject.getInt("trendNum"));
                        user.setStatement(jsonObject.getInt("statement"));
                        userArrayList.add(user);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            layoutManager.setOrientation(RecyclerView.VERTICAL);
                            recyclerView.setLayoutManager(layoutManager);
                            participantAdapter = new ParticipantAdapter(userArrayList,mActivityId);
                            recyclerView.setAdapter(participantAdapter);
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
        userArrayList.clear();
        super.onResume();
        getParticipant(mActivityId);
    }
}
