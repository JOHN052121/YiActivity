package com.yiactivity.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yiactivity.R;
import com.yiactivity.detailOfActivity.ActivityDetail;
import com.yiactivity.detailOfSponsor.SponsorDetail;
import com.yiactivity.model.Activity;
import com.yiactivity.model.Sponsor;

import java.util.List;

public class SubscribeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;

    private List<Sponsor> mSponsorList;

    private int mUserId;
    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;

    public SubscribeAdapter(List<Sponsor> dataList,int userId) {
        mSponsorList = dataList;
        mUserId = userId;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            if(mContext == null)
            {
                mContext = parent.getContext();
            }
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.all_activity_list_crad, parent, false);
            final RecyclerViewHolder holder = new RecyclerViewHolder(view);
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    final Sponsor sponsor = mSponsorList.get(position);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }).start();
                    Intent intent = new Intent(mContext, SponsorDetail.class);
                    intent.putExtra("userId", mUserId);
                    mContext.startActivity(intent);
                }
            });
            return holder;

        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loading_more, parent, false);
            return new SubscribeAdapter.FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            Sponsor sponsor = mSponsorList.get(position);
            Glide.with(mContext).load(sponsor.getSponsorImage()).into(recyclerViewHolder.sponsorImage);
            recyclerViewHolder.sponsorIntro.setText(sponsor.getOrg_Name());
            recyclerViewHolder.sponsorIntro.setText(sponsor.getSponsorIntro());
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                case LOADING: // 正在加载
                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_COMPLETE: // 加载完成
                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_END: // 加载到底
                    footViewHolder.pbLoading.setVisibility(View.GONE);
                    footViewHolder.tvLoading.setVisibility(View.GONE);
                    footViewHolder.llEnd.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mSponsorList.size() + 1;
    }

    private static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        ImageView sponsorImage;
        TextView sponsorName;
        TextView sponsorIntro;
        TextView sponsorFuns;
        TextView sponsorActivityNum;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView;
            sponsorImage = itemView.findViewById(R.id.subscribe_item_image);
            sponsorName = itemView.findViewById(R.id.subscribe_item_name);
            sponsorIntro =itemView.findViewById(R.id.subscribe_item_intro);
            sponsorFuns = itemView.findViewById(R.id.subscribe_item_funs_text);
            sponsorActivityNum = itemView.findViewById(R.id.subscribe_item_activity_text);
        }
    }

    private static class FootViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pbLoading;
        TextView tvLoading;
        LinearLayout llEnd;

        FootViewHolder(View itemView) {
            super(itemView);
            pbLoading = itemView.findViewById(R.id.pb_loading);
            tvLoading = itemView.findViewById(R.id.tv_loading);
            llEnd = itemView.findViewById(R.id.ll_end);
        }
    }

    /**
     * 设置上拉加载状态
     *
     * @param loadState 0.正在加载 1.加载完成 2.加载到底
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }
}

