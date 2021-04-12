package com.yiactivity.register;

import android.util.Log;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.IpAddress;
import com.yiactivity.model.Sponsor;
import com.yiactivity.model.User;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class registerPresenter implements registerContract.Presenter {

    registerContract.View registerView;
    public registerPresenter(registerContract.View registerView){
        this.registerView = registerView;
    }

    @Override
    public void user_register(User user) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userName",user.getUserName())
                .add("phoneNum",user.getPhoneNum())
                .add("password",user.getPassword())
                .add("gender",user.getGender())
                .add("university",user.getUniversity())
                .add("email",user.getEmail())
                .add("image",user.getUserImage())
                .add("sign",user.getSign())
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL + "user/newUser")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                registerView.user_register_success();
            }
        });
    }

    @Override
    public void sponsor_register(Sponsor sponsor) {
        int returnId = DBOperation.sponsorRegister(sponsor);
        switch (returnId){
            case 0:
                registerView.user_register_failed();
                break;
            case 1:
                registerView.user_register_success();
                break;
            case 2:
                Log.d("调试","2");
                break;
            case 3:
                Log.d("调试","3");
                break;
        }
    }
}
