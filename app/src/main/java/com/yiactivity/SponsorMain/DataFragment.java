package com.yiactivity.SponsorMain;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
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
import com.yiactivity.model.Activity;
import com.yiactivity.Utils.DBOperation;

public class DataFragment extends Fragment {

    private int mSponsorId;
    private ImageView imageView;
    private TextView activity_name;
    private TextView activity_address;
    private TextView activity_time;
    private TextView enrolled_number;
    private TextView browser_count;
    private Activity mActivity;

    public DataFragment(int sponsorId, Activity activity){
        mSponsorId = sponsorId;
        mActivity = activity;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_fragment,container,false);
        activity_address = view.findViewById(R.id.data_fragment_location);
        activity_name = view.findViewById(R.id.data_activity_name);
        activity_time =view.findViewById(R.id.data_activity_time);
        enrolled_number = view.findViewById(R.id.data_fragment_enroll_number);
        browser_count = view.findViewById(R.id.data_fragment_browser_number);


        activity_time.setText(mActivity.getTime());
        activity_name.setText(mActivity.getActivityName());
        activity_address.setText(mActivity.getAddress());
        enrolled_number.setText(String.valueOf(mActivity.getParticipant()));
        browser_count.setText(String.valueOf(mActivity.getBrowserCount()));

        //返回按钮监听键
        imageView = view.findViewById(R.id.data_fragment_back);
        imageView.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
