package com.yiactivity.detailOfUser;

import android.content.Context;
import android.content.Intent;
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
import com.yiactivity.Utils.IpAddress;
import com.yiactivity.detailOfActivity.ActivityDetail;
import com.yiactivity.detailOfSponsor.SponsorDetail;
import com.yiactivity.model.Sponsor;


import java.util.ArrayList;

public class subscribeAdapter extends RecyclerView.Adapter<subscribeAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<Sponsor> mSponsorArrayList;

    private int mUserId;

    static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        ImageView sponsorImage;
        TextView sponsorName;
        TextView sponsorIntro;
        TextView sponosrFuns;
        TextView sponsorActivityNum;

        public ViewHolder(View view){
            super(view);
            relativeLayout = (RelativeLayout) view;
            sponsorImage = view.findViewById(R.id.subscribe_item_image);
            sponsorName = view.findViewById(R.id.subscribe_item_name);
            sponsorIntro = view.findViewById(R.id.subscribe_item_intro);
            sponosrFuns = view.findViewById(R.id.subscribe_item_funs_num);
            sponsorActivityNum = view.findViewById(R.id.subscribe_item_activity_num);
        }
    }

    public subscribeAdapter(ArrayList<Sponsor> sponsorArrayList,int userId){
        mSponsorArrayList = sponsorArrayList;
        mUserId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.subscribe_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final Sponsor sponsor = mSponsorArrayList.get(position);
                Intent intent = new Intent(mContext, SponsorDetail.class);
                intent.putExtra("user_id", mUserId);
                intent.putExtra("sponsor_id",sponsor.getSponsorId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sponsor sponsor = mSponsorArrayList.get(position);
        holder.sponsorActivityNum.setText(String.valueOf(sponsor.getActivityNumOfSponsor()));
        holder.sponosrFuns.setText(String.valueOf(sponsor.getFuns()));
        holder.sponsorIntro.setText(sponsor.getSponsorIntro());
        holder.sponsorName.setText(sponsor.getOrg_Name());
        Glide.with(mContext).load(IpAddress.URL_PIC + "sponsorImage/"+sponsor.getSponsorImage1()).into(holder.sponsorImage);
    }

    @Override
    public int getItemCount() {
        return mSponsorArrayList.size();
    }
}
