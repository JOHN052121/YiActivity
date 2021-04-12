package com.yiactivity.detailOfActivity;

import com.yiactivity.model.Activity;
import com.yiactivity.model.Sponsor;

public interface detailOfActivityContract {

    interface Presenter {
        void getDetailOfActivity(Activity activity);

        void getEnrollState(int activityId, int userId,int activityState);

        void getSponsorInfo(int sponsorId);
    }

    interface View {
        void setDetailOfActivity(Activity activity);

        void setEnrollButton(int state);

        void setSponsorInfo(Sponsor sponsor);
    }
}
