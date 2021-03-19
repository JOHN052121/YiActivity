package com.yiactivity.SponsorMain;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yiactivity.R;
import com.yiactivity.Utils.DBOperation;
import com.yiactivity.model.User;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ParticipantFragment extends Fragment {

    private int mSponsorId;
    private ImageView participant_back_button;
    private RecyclerView recyclerView;
    private ArrayList<User> userArrayList = new ArrayList<>();
    private ParticipantAdapter participantAdapter;
    private int mActivityId;
    private TextView participant_title;
    private String mActivityName;

    public ParticipantFragment(int sponsorId,int activityId,String activityName){
        mSponsorId = sponsorId;
        mActivityId = activityId;
        mActivityName = activityName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.participant_fragment,container,false);
        participant_back_button = view.findViewById(R.id.participant_fragment_back);
        recyclerView = view.findViewById(R.id.participant_fragment_recycle);
        participant_title = view.findViewById(R.id.participant_fragment_title);
        participant_title.setText(mActivityName);
        //返回键监听
        participant_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getParticipant(mActivityId);
    }

    private void getParticipant(final int activityId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                userArrayList = DBOperation.getParticipant(activityId);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        participantAdapter = new ParticipantAdapter(userArrayList,mActivityId);
                        recyclerView.setAdapter(participantAdapter);
                        Intent intent = new Intent("getEnrollNum");
                        intent.putExtra("enrolled_number",participantAdapter.getItemCount());
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        getParticipant(mActivityId);
    }
}
