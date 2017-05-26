package com.example.ethanwalker.myapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by EthanWalker on 2017/4/14.
 */

public class MeFragment extends Fragment {

    Button change;
    TextView nickname;
    TextView sex;
    TextView phone;
    TextView personal;

    private MeCallBack callbacks;
    public interface MeCallBack {
        void startChange(View v);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(callbacks==null){
            callbacks = (MeCallBack)activity;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.me,null);
        init(view);
       change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.startChange(view);
            }
        });

        Bundle arguments = getArguments();
        if(arguments!=null){
            nickname.setText(arguments.get("nickname").toString());
            sex.setText(arguments.get("sex").toString());
            phone.setText(arguments.get("phone").toString());
            personal.setText(arguments.get("personal").toString());
        }
        return view;
    }

    public void init(View view){
        change = (Button)view.findViewById(R.id.change_personal);
        nickname =(TextView)view.findViewById(R.id.nickname);
        sex = (TextView)view.findViewById(R.id.sex);
        phone = (TextView)view.findViewById(R.id.phone);
        personal =(TextView)view.findViewById(R.id.personal);

    }
}
