package com.yiactivity.detailOfActivity;

import com.yiactivity.model.Activity;

public interface detailOfActivityContract {

    interface Presenter {
        void getDetailOfActivity(int activityId);

        void getEnrollState(int activityId, int userId);
    }

    interface View {
        void setDetailOfActivity(Activity activity);

        void setEnrollButton(int state);
    }
}
