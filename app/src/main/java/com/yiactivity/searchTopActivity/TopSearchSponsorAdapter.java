package com.yiactivity.searchTopActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yiactivity.R;
import com.yiactivity.Utils.ImageToDB;
import com.yiactivity.detailOfSponsor.SponsorDetail;
import com.yiactivity.model.Sponsor;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TopSearchSponsorAdapter extends RecyclerView.Adapter<TopSearchSponsorAdapter.ViewHolder> {

    private Context mContext;

    private int mUserId;

    private ArrayList<Sponsor> mArrayList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        ImageView sponsor_Img;
        TextView Org_Name;
        TextView Org_introduce;
        Button check_button;

        private ViewHolder(View view){
            super(view);
            relativeLayout = (RelativeLayout) view;
            Org_Name = view.findViewById(R.id.top_search_sponsor_item_orgName);
            sponsor_Img = view.findViewById(R.id.top_search_sponsor_item_Image);
            Org_introduce = view.findViewById(R.id.top_search_sponsor_item_introduce);
            check_button = view.findViewById(R.id.check_sponsor_detail);
        }
    }

    public TopSearchSponsorAdapter(ArrayList<Sponsor> sponsorArrayList,int userId){
        mArrayList = sponsorArrayList;
        mUserId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        final View view = LayoutInflater.from(mContext).inflate(R.layout.top_search_sponsor_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Sponsor sponsor = mArrayList.get(position);
                Intent intent = new Intent(mContext, SponsorDetail.class);
                intent.putExtra("user_id",mUserId);
                intent.putExtra("sponsor_id",sponsor.getSponsorId());
                mContext.startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sponsor sponsor = mArrayList.get(position);
        holder.Org_introduce.setText(sponsor.getSponsorIntro());
        holder.Org_Name.setText(sponsor.getOrg_Name());
        Glide.with(mContext).load(sponsor.getSponsorImage()).into(holder.sponsor_Img);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
}
