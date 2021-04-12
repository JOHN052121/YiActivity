package com.yiactivity.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.yiactivity.detailOfSponsor.SponsorDetail;
import com.yiactivity.model.Activity;
import com.yiactivity.model.Sponsor;

import java.util.ArrayList;

public class sponsorRandomAdapter extends RecyclerView.Adapter<sponsorRandomAdapter.ViewHolder> {
    private Context mContext;

    private ArrayList<Sponsor> mSponsorList;

    private int mUserId;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView sponsorImage;
        TextView sponsorName;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            sponsorImage = view.findViewById(R.id.randomSponsor_image_home);
            sponsorName = view.findViewById(R.id.sponsor_home_name);
        }
    }

    public sponsorRandomAdapter(ArrayList<Sponsor> sponsorArrayList,int userId){
        mSponsorList = sponsorArrayList;
        mUserId = userId;
    }

    @NonNull
    @Override
    public sponsorRandomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null);{
            mContext = parent.getContext();
        }
        final View view = LayoutInflater.from(mContext).inflate(R.layout.random_sponsor_home,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Sponsor sponsor =  mSponsorList.get(position);
                Intent intent = new Intent(mContext, SponsorDetail.class);
                intent.putExtra("sponsor_id",sponsor.getSponsorId());
                intent.putExtra("userId",mUserId);
                System.out.println(mUserId);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull sponsorRandomAdapter.ViewHolder holder, int position) {
        Sponsor sponsor = mSponsorList.get(position);
        holder.sponsorName.setText(sponsor.getOrg_Name());
        Glide.with(mContext).load("http://192.168.1.111:8080/SSM_war_exploded/asset/sponsorImage/" + sponsor.getSponsorImage1()).into(holder.sponsorImage);
    }

    @Override
    public int getItemCount() {
        return mSponsorList.size();
    }
}
