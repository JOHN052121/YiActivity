package com.yiactivity.SponsorMain;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.yiactivity.Utils.ImageToDB;
import com.yiactivity.Utils.IpAddress;
import com.yiactivity.model.User;
import java.util.ArrayList;


public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<User> mUserArrayList;

    private int mActivityId;

    static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        ImageView participantImage;
        TextView participantName;
        TextView participantPhone;
        TextView participantState;

        public ViewHolder(View view){
            super(view);
            relativeLayout = (RelativeLayout) view;
            participantImage = view.findViewById(R.id.participant_image);
            participantName = view.findViewById(R.id.participant_name);
            participantPhone = view.findViewById(R.id.participant_phone);
            participantState = view.findViewById(R.id.participant_state);
        }
    }

    public ParticipantAdapter(ArrayList<User> userArrayList,int activityId){
        mUserArrayList = userArrayList;
        mActivityId = activityId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.participant_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                User user = mUserArrayList.get(position);
                Intent intent = new Intent(mContext,ParticipantDetail.class);
                intent.putExtra("user_data",user);
                intent.putExtra("activity_id",mActivityId);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUserArrayList.get(position);
        Glide.with(mContext).load( IpAddress.URL_PIC + "userImage/" + user.getUserImage()).into(holder.participantImage);
        holder.participantName.setText(user.getUserName());
        holder.participantPhone.setText(user.getPhoneNum());
        switch (user.getStatement()){
            case 1:
                holder.participantState.setTextColor(Color.GREEN);
                holder.participantState.setText("待审核");
                break;
            case 2:
                holder.participantState.setTextColor(Color.LTGRAY);
                holder.participantState.setText("已同意");
                break;
            case 0:
                holder.participantState.setTextColor(Color.DKGRAY);
                holder.participantState.setText("已拒绝");
                break;
            case 5:
                holder.participantState.setTextColor(Color.DKGRAY);
                holder.participantState.setText("已取消");
        }
    }

    @Override
    public int getItemCount() {
        return mUserArrayList.size();
    }
}
