package com.yiactivity.login;

import android.content.Context;
import com.yiactivity.model.Sponsor;
import com.yiactivity.model.User;

public interface loginContract {

    interface Presenter{
        void LoginAsUser(String user, String password);
        void LoginAsSponsor(String user, String password);
    }

    interface View{
        void loginSuccess(User user);
        void loginFailed();
        void loginSuccessAsSponsor(Sponsor sponsor);
    }
}
