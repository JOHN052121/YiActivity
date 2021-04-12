package com.yiactivity.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yiactivity.R;
import com.yiactivity.detailOfActivity.ActivityDetail;
import com.yiactivity.model.Activity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private Context mContext;

    private int mUserId;

    private ArrayList<Activity> mActivityList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView activityPoster;
        TextView activityName;
        TextView activityType;
        TextView activityTime;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            activityPoster = view.findViewById(R.id.activity_poster);
            activityName = view.findViewById(R.id.activity_name);
            activityType = view.findViewById(R.id.activity_type);
            activityTime = view.findViewById(R.id.activity_time);
        }
    }

    public ActivityAdapter(ArrayList<Activity> activityList, int userId){
        mActivityList = activityList;
        mUserId = userId;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_itme,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final Activity activity = mActivityList.get(position);
                updateBrowserCount(activity.getActivityId());
                Intent intent = new Intent(mContext, ActivityDetail.class);
                intent.putExtra("activity_data",activity);
                intent.putExtra("userId",mUserId);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Activity activity = mActivityList.get(position);
        holder.activityName.setText(activity.getActivityName());
        Glide.with(mContext).load(IpAddress.URL_PIC +"activityPoster/" + activity.getPoster2()).into(holder.activityPoster);
        holder.activityType.setText(activity.getType().substring(0,4));
        holder.activityTime.setText(activity.getTime().substring(6,10));
    }

    @Override
    public int getItemCount() {
        return mActivityList.size();
    }

    public static void updateBrowserCount(final int activityId){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("activityId",String.valueOf(activityId))
                .build();
        Request request = new Request.Builder()
                .url(IpAddress.URL + "activity/updateBrowserCount")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }
}
