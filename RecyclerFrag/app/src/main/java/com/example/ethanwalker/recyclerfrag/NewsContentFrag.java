package com.example.ethanwalker.recyclerfrag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by EthanWalker on 2017/5/19.
 */

public class NewsContentFrag extends Fragment {
    TextView titleView;
    TextView contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_content_frag,container,false);
        titleView = (TextView)view.findViewById(R.id.news_content_title);
        contentView = (TextView) view.findViewById(R.id.news_content_content);
        return view;
    }
    public void refresh(String title,String content){
        titleView.setText(title);
        contentView.setText(content);
    }
}
