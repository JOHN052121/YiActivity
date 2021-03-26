package com.yiactivity.detailOfActivity;

import android.util.Log;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.model.Activity;
import com.yiactivity.model.Sponsor;

public class detailOfActivityPresenter implements detailOfActivityContract.Presenter {

    detailOfActivityContract.View detailOfActivityView;

    public detailOfActivityPresenter(detailOfActivityContract.View detailOfActivityView){
        this.detailOfActivityView = detailOfActivityView;
    }

    @Override
    public void getDetailOfActivity(int activityId) {
        Activity activity = DBOperation.getDetailActivity(activityId);
        detailOfActivityView.setDetailOfActivity(activity);
    }

    @Override
    public void getSponsorInfo(int sponsorId) {
        Sponsor sponsor =DBOperation.getSponsorInfo(sponsorId);
        detailOfActivityView.setSponsorInfo(sponsor);
    }

    @Override
    public void getEnrollState(int activityId, int userId) {
        if(DBOperation.getActivityState(activityId) == 2){
            detailOfActivityView.setEnrollButton(4);
        }
        else {
            int state = DBOperation.getUserToActivity_status(activityId,userId);
            detailOfActivityView.setEnrollButton(state);
        }
    }
}
