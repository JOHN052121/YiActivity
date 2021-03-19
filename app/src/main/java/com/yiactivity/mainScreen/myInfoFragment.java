package com.yiactivity.mainScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pManager;
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
import com.yiactivity.mainScreen.ActivityMyInfo.EnrolledActivity;
import com.yiactivity.mainScreen.ActivityMyInfo.SignedActivity;
import com.yiactivity.mainScreen.ActivityMyInfo.SigningActivity;
import com.yiactivity.mainScreen.ActivityMyInfo.ThinkedActivity;
import com.yiactivity.model.User;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class myInfoFragment extends Fragment implements View.OnClickListener {

    private ImageView bigImage;
    private int mUserId;
    private ImageView myInfo_image;
    private TextView myInfo_name;
    private User user;
    private Button exit_button;
    private ImageView activity_enrolled;
    private ImageView activity_signing;
    private ImageView activity_signed;
    private ImageView activity_thinked;

    public myInfoFragment(int userId){
        mUserId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myinfo,container,false);
        bigImage = view.findViewById(R.id.BigImage);
        myInfo_image = view.findViewById(R.id.myInfo_image);
        myInfo_name = view.findViewById(R.id.myInfo_name);
        exit_button = view.findViewById(R.id.exit_login);
        activity_enrolled = view.findViewById(R.id.myInfo_enroll);
        activity_signed = view.findViewById(R.id.myInfo_signed);
        activity_signing = view.findViewById(R.id.myInfo_signing);
        activity_thinked = view.findViewById(R.id.myInfo_think);
        Glide.with(this).load(R.drawable.background)
                .apply(bitmapTransform(new BlurTransformation(25))).into(bigImage);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        activity_enrolled.setOnClickListener(this);
        activity_signing.setOnClickListener(this);
        activity_signed.setOnClickListener(this);
        activity_thinked.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myInfo_enroll:
                Intent intent = new Intent(getActivity(), EnrolledActivity.class);
                intent.putExtra("user_id",mUserId);
                startActivity(intent);
                break;
            case R.id.myInfo_think:
                Intent intent1 = new Intent(getActivity(), ThinkedActivity.class);
                intent1.putExtra("user_id",mUserId);
                startActivity(intent1);
                break;
            case R.id.myInfo_signed:
                Intent intent2 = new Intent(getActivity(), SignedActivity.class);
                intent2.putExtra("user_id",mUserId);
                startActivity(intent2);
                break;
            case R.id.myInfo_signing:
                Intent intent3 = new Intent(getActivity(), SigningActivity.class);
                intent3.putExtra("user_id",mUserId);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showUserInfo();

    }

    private void showUserInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                user = DBOperation.getUserInfo(mUserId);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myInfo_name.setText(user.getUserName());
                        Glide.with(myInfoFragment.this).load(user.getImage()).into(myInfo_image);
                    }
                });

            }
        }).start();
    }

}
