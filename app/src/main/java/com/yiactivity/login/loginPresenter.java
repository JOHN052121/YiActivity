package com.yiactivity.login;

import android.util.Log;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.model.Sponsor;
import com.yiactivity.model.User;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

public class loginPresenter implements loginContract.Presenter{
     loginContract.View loginView;

     private static String IP_URL = "http://192.168.1.111:8080/SSM_war_exploded/";

     public loginPresenter(loginContract.View loginView){
         this.loginView = loginView;
     }

    @Override
    public void LoginAsUser(String user, String password) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("phoneNum",user)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.1.111:8080/SSM_war_exploded/user/loginCheck")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                loginView.loginFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if(jsonObject.getInt("userId") == 0){
                        loginView.loginFailed();
                    }
                    else {
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
                        user.setPassword(jsonObject.getString("password"));
                        user.setSign(jsonObject.getString("sign"));
                        Log.d("presenter调试", user.getUserName());
                        loginView.loginSuccess(user);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void LoginAsSponsor(String user,String password){
       OkHttpClient client = new OkHttpClient();
       RequestBody requestBody = new FormBody.Builder()
               .add("phoneNum",user)
               .add("password",password)
               .build();
       Request request = new Request.Builder()
               .url("http://192.168.1.111:8080/SSM_war_exploded/sponsor/loginCheck")
               .post(requestBody)
               .build();
       client.newCall(request).enqueue(new Callback() {
           @Override
           public void onFailure(@NotNull Call call, @NotNull IOException e) {
               loginView.loginFailed();
           }

           @Override
           public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
               try{
                   JSONObject jsonObject = new JSONObject(response.body().string());
                   if(jsonObject.getInt("sponsorId") == 0){
                       loginView.loginFailed();
                   }
                   else {
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
                       loginView.loginSuccessAsSponsor(sponsor);
                   }

               }catch (Exception e){
                   e.printStackTrace();
               }


           }
       });

    }
}
