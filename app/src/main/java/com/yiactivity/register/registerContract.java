package com.yiactivity.register;

import com.yiactivity.model.Sponsor;
import com.yiactivity.model.User;

public interface registerContract {

    interface Presenter{

        void user_register(User user);
        void sponsor_register(Sponsor sponsor);
    }

    interface View{

        void user_register_success();
        void user_register_failed();
    }
}
