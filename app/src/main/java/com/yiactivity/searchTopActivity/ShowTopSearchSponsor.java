package com.yiactivity.searchTopActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.IpAddress;
import com.yiactivity.Utils.Search_all_activityAdapter;
import com.yiactivity.model.Sponsor;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ShowTopSearchSponsor extends Fragment {

    private int mUserId;
    private String mKeyword;
    private RecyclerView recyclerView;
    private ArrayList<Sponsor> sponsorArrayList = new ArrayList<>();
    private TopSearchSponsorAdapter adapter;
    private ProgressBar progressBar;
    private ImageView noInformation;

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
        noInformation = view.findViewById(R.id.show_search_sponsor_noInformation);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSearchSponsor(mKeyword);
    }

    private void getSearchSponsor(final String keyword){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("keyword",keyword)
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL + "sponsor/getSponsorSearch")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "连接失败，请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    if(jsonArray.length() < 1){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                    else {
                        for(int i=0;i<jsonArray.length();i++){
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
                                noInformation.setVisibility(View.GONE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                layoutManager.setOrientation(RecyclerView.VERTICAL);
                                recyclerView.setLayoutManager(layoutManager);
                                adapter= new TopSearchSponsorAdapter(sponsorArrayList,mUserId);
                                recyclerView.setAdapter(adapter);
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
