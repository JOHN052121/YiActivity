package com.yiactivity.login;

import android.content.Context;
import com.yiactivity.model.Sponsor;
import com.yiactivity.model.User;

public interface loginContract {

    interface Presenter{
        User LoginAsUser(String user, String password);
        Sponsor LoginAsSponsor(String user, String password);
    }

    interface View{
        void loginSuccess();
        void loginFailed();
        void loginSuccessAsSponsor();
    }
}
