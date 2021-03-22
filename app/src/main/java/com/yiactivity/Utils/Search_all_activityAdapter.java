package com.yiactivity.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yiactivity.R;
import com.yiactivity.detailOfActivity.ActivityDetail;
import com.yiactivity.model.Activity;

import java.util.ArrayList;

public class Search_all_activityAdapter extends RecyclerView.Adapter<Search_all_activityAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<Activity> mActivityList;

    private int mUserId;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView activityPoster;
        TextView activityName;
        TextView activityType;
        TextView activityTime;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            activityPoster = view.findViewById(R.id.all_activity_image);
            activityName = view.findViewById(R.id.all_activity_name);
            activityType = view.findViewById(R.id.all_activity_type);
            activityTime = view.findViewById(R.id.all_activity_time);
        }
    }

    public Search_all_activityAdapter(ArrayList<Activity> activityList,int userId){
        mActivityList = activityList;
        mUserId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.all_activity_list_crad,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
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
                intent.putExtra("userId",mUserId);
                intent.putExtra("activityId",activity.getActivityId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Activity activity = mActivityList.get(position);
        holder.activityName.setText(activity.getActivityName());
        Glide.with(mContext).load(activity.getPoster()).into(holder.activityPoster);
        holder.activityType.setText(activity.getType().substring(0,4));
        holder.activityTime.setText(activity.getTime());
    }

    @Override
    public int getItemCount() {
        return mActivityList.size();
    }
}
