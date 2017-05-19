package com.example.ethanwalker.myapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by EthanWalker on 2017/4/14.
 */

public class WeixinFragment extends Fragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(WeixinAdapter.callback ==null){
            WeixinAdapter.callback = (WeixinAdapter.WeixinCallback)activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.weixin_fragment, container,false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.weixin_list);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        WeixinAdapter adapter = new WeixinAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        return v;
    }
}
