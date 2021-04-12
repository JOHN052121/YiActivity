package com.yiactivity.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yiactivity.R;
import com.yiactivity.detailOfUser.DetailUserMain;
import com.yiactivity.model.Trend;
import com.yiactivity.releaseTrends.ReleaseTrend;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class TrendAdapter extends RecyclerView.Adapter<TrendAdapter.ViewHolder> {

    private Context mContext;

    private int mUserId;

    private int mCode;

    private int otherId;

    private ArrayList<Trend> trends = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        TextView userName;
        TextView trendContent;
        ImageView trendImg;
        ImageView userImg;
        ImageView delete_trend;

        public ViewHolder(View view){
            super(view);
            relativeLayout = (RelativeLayout) view;
            userName = view.findViewById(R.id.activity_square_userName);
            trendContent = view.findViewById(R.id.activity_square_text);
            trendImg = view.findViewById(R.id.activity_square_img1);
            userImg = view.findViewById(R.id.activity_square_userImg);
            delete_trend = view.findViewById(R.id.delete_trend);
        }
    }

    public TrendAdapter(ArrayList<Trend> trendArrayList,int userId,int code){
        trends = trendArrayList;
        mUserId = userId;
        mCode = code;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_square_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Trend trend = trends.get(position);
        if(trend.getImage().equals("noimage") ){
            holder.userName.setText(trend.getUserName());
            holder.trendContent.setText(trend.getContent());
            Glide.with(mContext).load(IpAddress.URL_PIC+"userImage/"+trend.getUserImg()).into(holder.userImg);
        }
        else {
            holder.userName.setText(trend.getUserName());
            holder.trendContent.setText(trend.getContent());
            Glide.with(mContext).load(IpAddress.URL_PIC+"userImage/"+trend.getUserImg()).into(holder.userImg);
            holder.trendImg.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(IpAddress.URL_PIC+"trendImage/"+trend.getImage()).into(holder.trendImg);
        }
        if(mCode == 1){
            holder.delete_trend.setVisibility(View.VISIBLE);
            holder.delete_trend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("发布动态");
                    builder.setMessage("确定发布动态吗？");
                    builder.setCancelable(false);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("id",String.valueOf(trend.getId()))
                                    .add("userId",String.valueOf(trend.getUserId()))
                                    .build();
                            Request request = new Request.Builder()
                                    .url(IpAddress.URL +"trend/deleteTrend")
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
                            holder.relativeLayout.setVisibility(View.GONE);
                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        }
        if(mCode == 0){
            holder.userImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailUserMain.class);
                    intent.putExtra("user_id",trend.getUserId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return trends.size();
    }



}
