package com.example.administrator.hodlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.hodlife.R;


/**
 * Created by Dell on 7/31/2017.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView =  inflater.inflate(R.layout.setting_fragment, container, false);

        Button sendBt = (Button) convertView.findViewById(R.id.send_bt);
        sendBt.setOnClickListener(this);
        Button logoutBt = (Button) convertView.findViewById(R.id.fb_logout_bt);
        sendBt.setOnClickListener(this);
        return convertView;
    }

    public void onClick(View view){
        switch(view.getId()) {
            case R.id.send_bt:
                break;
            case R.id.fb_logout_bt:
                break;
        }
    }
}
