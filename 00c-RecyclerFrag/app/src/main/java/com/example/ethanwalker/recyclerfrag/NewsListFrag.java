package com.example.ethanwalker.recyclerfrag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EthanWalker on 2017/5/19.
 */

public class NewsListFrag extends Fragment {
    boolean isTwoPane;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_list_frag, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.news_list_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        NewsAdapter adapter = new NewsAdapter(getNewses());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.large_news_content_frag) != null) {
            isTwoPane = true;
        } else {
            isTwoPane = false;
        }
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        List<News> newses;

        public NewsAdapter(List<News> newses){
            this.newses = newses;
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;

            public ViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.news_item_view);
            }

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news = newses.get(viewHolder.getAdapterPosition());

                    if (isTwoPane) {
                        NewsContentFrag newsContentFrag = (NewsContentFrag) getActivity().getSupportFragmentManager().findFragmentById(R.id.large_news_content_frag);
                        newsContentFrag.refresh(news.getTitle(), news.getContent());
                    } else {
                        NewsContentActivity.actionStart(getActivity(), news.getTitle(), news.getContent());
                    }
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            News news = newses.get(position);
            holder.title.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return newses.size();
        }

    }
    public List<News> getNewses(){
        List<News> newses = new ArrayList<>();
        for(int i=0;i<20;i++){
            String title = "title--"+i;
            String content = "content--"+i;
            News news = new News(title,content);
            newses.add(news);
        }
        return newses;
    }
}
