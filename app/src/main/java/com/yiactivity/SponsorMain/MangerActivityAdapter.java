package com.yiactivity.SponsorMain;

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
import com.yiactivity.Utils.ImageToDB;
import com.yiactivity.model.Activity;

import java.util.ArrayList;

public class MangerActivityAdapter extends RecyclerView.Adapter<MangerActivityAdapter.ViewHolder> {
    private Context mContext;

    private ArrayList<Activity> mActivityList;

    private int mSponsorId;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView activityPoster;
        TextView activityName;
        TextView activityType;
        TextView activityTime;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            activityPoster = view.findViewById(R.id.manger_activity_image);
            activityName = view.findViewById(R.id.manger_activity_name);
            activityType = view.findViewById(R.id.manger_activity_type);
            activityTime = view.findViewById(R.id.manger_activity_time);
        }
    }

    public MangerActivityAdapter(ArrayList<Activity> activityList,int sponsorId){
        mActivityList = activityList;
        mSponsorId = sponsorId;
    }

    @NonNull
    @Override
    public MangerActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.manger_activity_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Activity activity = mActivityList.get(position);
                Intent intent = new Intent(mContext, MangerActivityInDetail.class);
                intent.putExtra("sponsorId",mSponsorId);
                intent.putExtra("activityName",activity.getActivityName());
                intent.putExtra("activityAddress",activity.getAddress());
                intent.putExtra("activityTime",activity.getTime());
                intent.putExtra("activityId",activity.getActivityId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MangerActivityAdapter.ViewHolder holder, int position) {
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
