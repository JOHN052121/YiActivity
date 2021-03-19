package com.yiactivity.register;

import android.util.Log;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.model.Sponsor;
import com.yiactivity.model.User;

public class registerPresenter implements registerContract.Presenter {

    registerContract.View registerView;
    public registerPresenter(registerContract.View registerView){
        this.registerView = registerView;
    }

    @Override
    public void user_register(User user) {
        int returnId = DBOperation.userRegister(user);
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
