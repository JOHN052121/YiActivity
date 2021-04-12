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
import com.yiactivity.Utils.sponsorRandomAdapter;
import com.yiactivity.model.Sponsor;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class subscribeFragment extends Fragment {


    private RecyclerView recyclerView;
    private subscribeAdapter subscribeAdapter;
    private ImageView imageView;
    private ArrayList<Sponsor> sponsorArrayList = new ArrayList<>();
    private int mUserId;


    public subscribeFragment(int userId){
        mUserId = userId;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subscribe_fragment,container,false);
        recyclerView = view.findViewById(R.id.subscribe_fragment_recycle);
        imageView = view.findViewById(R.id.subscribe_image);
        return view;
    }


    private void getSponsors(final int userId){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userId",String.valueOf(userId))
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL +"sponsor/getSponsorByUserId")
                .post(requestBody)
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
                    for(int i=0;i<jsonArray.length();i++) {
                        Sponsor sponsor = new Sponsor();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        sponsor.setSponsorId(jsonObject.getInt("sponsorId"));
                        sponsor.setSpon_Name(jsonObject.getString("spon_Name"));
                        sponsor.setOrg_Name(jsonObject.getString("org_Name"));
                        sponsor.setPassword(jsonObject.getString("password"));
                        sponsor.setUniversity(jsonObject.getString("university"));
                        sponsor.setPhoneNum(jsonObject.getString("phoneNum"));
                        sponsor.setEmail(jsonObject.getString("email"));
                        sponsor.setSponsorImage1(jsonObject.getString("sponsorImage"));
                        sponsor.setSponsorIntro(jsonObject.getString("sponIntro"));
                        sponsor.setActivityNumOfSponsor(jsonObject.getInt("activityNum"));
                        sponsor.setFuns(jsonObject.getInt("funs"));
                        sponsorArrayList.add(sponsor);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(sponsorArrayList.size() < 1){
                                recyclerView.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                            }
                            else {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                layoutManager.setOrientation(RecyclerView.VERTICAL);
                                recyclerView.setLayoutManager(layoutManager);
                                subscribeAdapter = new subscribeAdapter(sponsorArrayList,userId);
                                recyclerView.setAdapter(subscribeAdapter);
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
        sponsorArrayList.clear();
        getSponsors(mUserId);
    }
}
