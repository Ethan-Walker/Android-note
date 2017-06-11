package com.example.ethanwalker.myapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by EthanWalker on 2017/4/14.
 */

public class WeixinAdapter extends RecyclerView.Adapter<WeixinAdapter.ViewHolder> {
    public static WeixinCallback callback;

    public interface WeixinCallback {
        void startChat(String firendName);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            imageView = (ImageView) view.findViewById(R.id.weixin_img);
            textView = (TextView) view.findViewById(R.id.weixin_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weixin_list_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserList.User user = UserList.users.get(viewHolder.getAdapterPosition());

//                Toast.makeText(v.getContext(), "点击的是第" + viewHolder.getAdapterPosition() + "项", Toast.LENGTH_SHORT).show();
                if (callback != null) {
                    callback.startChat(user.getName());
//                    Log.e(TAG, "user.getName():   " + user.getName());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserList.User user = UserList.users.get(holder.getAdapterPosition());
        holder.imageView.setImageResource((int) (user.getImageId()));
        holder.textView.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return UserList.users.size();
    }

    private static final String TAG = "WeixinAdapter";
}
