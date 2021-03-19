package com.yiactivity.SponsorMain;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.yiactivity.R;

public class DataFragment extends Fragment {

    private int mSponsorId;
    private ImageView imageView;
    private int mActivityId;
    private String mActivityName;
    private String mActivityTime;
    private String mActivityAddress;
    private TextView activity_name;
    private TextView activity_address;
    private TextView activity_time;
    private TextView enrolled_number;

    public DataFragment(int sponsorId,int activityId,String activityAddress,String activityName,String activityTime){
        mSponsorId = sponsorId;
        mActivityId = activityId;
        mActivityAddress = activityAddress;
        mActivityTime = activityTime;
        mActivityName = activityName;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_fragment,container,false);
        activity_address = view.findViewById(R.id.data_fragment_location);
        activity_name = view.findViewById(R.id.data_activity_name);
        activity_time =view.findViewById(R.id.data_activity_time);
        enrolled_number = view.findViewById(R.id.data_fragment_enroll_number);

        activity_time.setText(mActivityTime);
        activity_name.setText(mActivityName);
        activity_address.setText(mActivityAddress);

        //返回按钮监听键
        imageView = view.findViewById(R.id.data_fragment_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("getEnrollNum");
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                enrolled_number.setText(String.valueOf(intent.getIntExtra("enrolled_number",0)));
            }
        };
        localBroadcastManager.registerReceiver(br,intentFilter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
