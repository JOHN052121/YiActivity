package com.yiactivity.Utils;

import android.content.Context;
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
import com.yiactivity.model.Trend;

import java.util.ArrayList;

public class TrendAdapter extends RecyclerView.Adapter<TrendAdapter.ViewHolder> {

    private Context mContext;

    private int mUserId;

    private ArrayList<Trend> trends = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        TextView userName;
        TextView trendContent;
        ImageView trendImg;
        ImageView userImg;

        public ViewHolder(View view){
            super(view);
            relativeLayout = (RelativeLayout) view;
            userName = view.findViewById(R.id.activity_square_userName);
            trendContent = view.findViewById(R.id.activity_square_text);
            trendImg = view.findViewById(R.id.activity_square_img1);
            userImg = view.findViewById(R.id.activity_square_userImg);
        }
    }

    public TrendAdapter(ArrayList<Trend> trendArrayList,int userId){
        trends = trendArrayList;
        mUserId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_square_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trend trend = trends.get(position);
        holder.userName.setText(trend.getUserName());
        holder.trendContent.setText(trend.getContent());
        Glide.with(mContext).load(trend.getUserImg()).into(holder.userImg);
        if(trend.getImage() !=null ){
            holder.trendImg.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(trend.getImage()).into(holder.trendImg);
        }
    }

    @Override
    public int getItemCount() {
        return trends.size();
    }
}
