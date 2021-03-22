package com.yiactivity.mainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.yiactivity.R;
import com.yiactivity.releaseTrends.ReleaseTrend;

public class activitySquareFragment extends Fragment {

    private ImageView release_trend;
    private int mUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_square,container,false);
        release_trend = view.findViewById(R.id.add_activity_square);

        release_trend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReleaseTrend.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
