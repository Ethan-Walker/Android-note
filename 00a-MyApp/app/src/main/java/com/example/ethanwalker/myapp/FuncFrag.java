package com.example.ethanwalker.myapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by EthanWalker on 2017/6/10.
 */

public class FuncFrag extends Fragment {

    Button getContacts;
    Button download;
    Button xmlParse;
    StartCallBack callback;
    interface StartCallBack{
        void startGetContacts();
        void startDownload();
        void startXmlParse();
    }


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (callback == null) {
            callback = (FuncFrag.StartCallBack) activity;
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.func_frag, container,false);
        getContacts = (Button) view.findViewById(R.id.get_contacts);
        getContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.startGetContacts();
            }
        });
        download= (Button)view.findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.startDownload();
            }
        });
        xmlParse = (Button)view.findViewById(R.id.xml_parse);
        xmlParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.startXmlParse();
            }
        });
        return view;
    }
}
