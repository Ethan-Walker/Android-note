package com.example.ethanwalker.myapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * Created by EthanWalker on 2017/4/14.
 */

public class ChangePersonalFrag extends Fragment {
    private ChangeCallBack callback;


    public interface  ChangeCallBack{
        void submitMessage(Bundle bundle);
    }


    Button button ;
    EditText editNickName;
    RadioButton male;
    EditText editPhone;
    EditText editPersonal;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(callback ==null){
            callback = (ChangeCallBack)activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_personal_frag,container,false);

        init(view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("nickname",editNickName.getText().toString());
                String sex = male.isChecked()?"男":"女";
                bundle.putString("sex",sex);
                bundle.putString("phone",editPhone.getText().toString());
                bundle.putString("personal",editPersonal.getText().toString());
                callback.submitMessage(bundle);
            }
        });



        return view;
    }

    public void init(View view){
        button = (Button)view.findViewById(R.id.submit);
        editNickName =(EditText)view.findViewById(R.id.edit_nickname);
        editPhone =(EditText)view.findViewById(R.id.edit_phone);
        editPersonal= (EditText)view.findViewById(R.id.edit_personal);
        male = (RadioButton)view.findViewById(R.id.male);

    }



}
