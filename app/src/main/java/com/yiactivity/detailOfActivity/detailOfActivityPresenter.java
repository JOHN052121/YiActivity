package com.yiactivity.detailOfActivity;

import android.util.Log;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.model.Activity;
import com.yiactivity.model.Sponsor;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class detailOfActivityPresenter implements detailOfActivityContract.Presenter {

    private static String URL = "http://192.168.1.111:8080/SSM_war_exploded/sponsor/";
    private static String ACTIVITY_URL = "http://192.168.1.111:8080/SSM_war_exploded/activity/";
    detailOfActivityContract.View detailOfActivityView;

    public detailOfActivityPresenter(detailOfActivityContract.View detailOfActivityView){
        this.detailOfActivityView = detailOfActivityView;
    }

    @Override
    public void getDetailOfActivity(Activity activity) {
        detailOfActivityView.setDetailOfActivity(activity);
    }

    @Override
    public void getSponsorInfo(int sponsorId) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("sponsorId",String.valueOf(sponsorId))
                .build();
        Request request = new Request.Builder()
                .url(URL + "getSponsorById")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Sponsor sponsor = new Sponsor();
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
                    detailOfActivityView.setSponsorInfo(sponsor);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getEnrollState(int activityId, int userId,int activityState) {
        if(activityState == 2){
            detailOfActivityView.setEnrollButton(4);
        }
        else {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("activityId",String.valueOf(activityId))
                    .add("userId",String.valueOf(userId))
                    .build();
            Request request = new Request.Builder()
                    .url(ACTIVITY_URL + "userToActivityState")
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try{
                        int state = Integer.parseInt(response.body().string());
                        detailOfActivityView.setEnrollButton(state);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
