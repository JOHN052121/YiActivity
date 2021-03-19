package com.yiactivity.SponsorMain;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.Utils.ImageToDB;
import com.yiactivity.model.Sponsor;

public class SponsorMyInfo extends Fragment {

    private Button exit_button;
    private ImageView sponsor_image;
    private TextView sponsor_name;
    private int mSponsorId;
    private Sponsor sponsor;

    public SponsorMyInfo(int sponsorId){
        mSponsorId = sponsorId;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sponsor_my_info,container, false);
        sponsor_image = view.findViewById(R.id.sponsor_detail_image);
        exit_button = view.findViewById(R.id.sponsor_exit_button);
        sponsor_name = view.findViewById(R.id.sponsor_detail_name);
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSponsorInfo(mSponsorId);
    }

    private void getSponsorInfo(final int sponsorId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sponsor = DBOperation.getSponsorDetailById(sponsorId);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(SponsorMyInfo.this).load(sponsor.getSponsorImage()).into(sponsor_image);
                        sponsor_name.setText(sponsor.getOrg_Name());
                    }
                });
            }
        }).start();
    }
}
