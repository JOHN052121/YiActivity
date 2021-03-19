package com.yiactivity.login;

import android.util.Log;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.model.Sponsor;
import com.yiactivity.model.User;

public class loginPresenter implements loginContract.Presenter{
     loginContract.View loginView;

     public loginPresenter(loginContract.View loginView){
         this.loginView = loginView;
     }

    @Override
    public User LoginAsUser(String user, String password) {
         User returnUser = DBOperation.loginOperation(user,password);
         if(returnUser != null){
             loginView.loginSuccess();
             return returnUser;
         }
         else {
             loginView.loginFailed();
             return null;
         }
    }

    @Override
    public Sponsor LoginAsSponsor(String user,String password){
        Sponsor returnSponsor = DBOperation.loginAsSponsor(user,password);
        if(returnSponsor != null){
            loginView.loginSuccessAsSponsor();
            return returnSponsor;
        }
        else {
            loginView.loginFailed();
            return null;
        }
    }
}
