package com.example.finance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MListAdapter extends BaseAdapter {

    private ArrayList<Article> mArticleList;
    private int resourceId;
    private Context ctx;

    public MListAdapter(Context context, int textViewResourceId, ArrayList<Article> objects) {
        resourceId = textViewResourceId;
        this.mArticleList = objects;
        this.ctx = context;
    }
    @Override
    public int getCount() {

        return mArticleList.size();
    }

    @Override
    public Article getItem(int position) {
        return mArticleList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Article article = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view= LayoutInflater.from(ctx).inflate(resourceId, null);

            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.summary = (TextView) view.findViewById(R.id.summary);
            viewHolder.postTime = (TextView) view.findViewById(R.id.postTime);
            view.setTag(viewHolder);
        } else {
            view=convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.title.setText(article.getTitle());
        viewHolder.summary.setText(article.getSummary());
        viewHolder.postTime.setText(article.getPostTime());

        return view;
    }

    static class ViewHolder {
        public TextView title;
        public TextView summary;
        public TextView postTime;
    }
}

