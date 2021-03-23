package com.yiactivity.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yiactivity.R;
import com.yiactivity.detailOfActivity.ActivityDetail;
import com.yiactivity.model.Activity;
import java.util.ArrayList;
import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {
    private Context mContext;

    private int mUserId;

    private ArrayList<Activity> mActivityList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        ImageView activityPoster;
        TextView activityName;
        TextView activityBrowser;

        public ViewHolder(View view){
            super(view);
            relativeLayout = (RelativeLayout) view;
            activityPoster = view.findViewById(R.id.popular_activity_poster);
            activityName = view.findViewById(R.id.popular_activity_name);
            activityBrowser = view.findViewById(R.id.popular_activity_browser);
        }
    }

    public PopularAdapter(ArrayList<Activity> activityArrayList,int userId){
        mActivityList = activityArrayList;
        mUserId = userId;
        System.out.println("adapter="+mActivityList.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.popular_activity_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final Activity activity = mActivityList.get(position);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DBOperation.updateBrowserCount(mUserId,activity.getActivityId());
                    }
                }).start();
                Intent intent = new Intent(mContext, ActivityDetail.class);
                intent.putExtra("activityId",activity.getActivityId());
                intent.putExtra("userId",mUserId);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Activity activity = mActivityList.get(position);
        Glide.with(mContext).load(activity.getPoster()).into(holder.activityPoster);
        holder.activityName.setText(activity.getActivityName());
        holder.activityBrowser.setText(String.valueOf(activity.getBrowserCount()));
    }

    @Override
    public int getItemCount() {
        return mActivityList.size();
    }
}
