package com.example.neeraj.spotifystreamer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.neeraj.spotifystreamer.R;

import java.util.List;

/**
 * Created by Neeraj on 02-05-2016.
 */
public class ReviewListview extends BaseAdapter {
    Context context=null;
    List<String> authorList;
    List<String> contentList;
    public ReviewListview(Context context,List<String> authorList,List<String> contentList) {
        this.contentList=contentList;
        this.context=context;
        this.authorList=authorList;
    }

    @Override
    public int getCount() {
        return contentList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;

        if(convertView==null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_listview, parent, false);
            holder = new ViewHolder();
            holder.author = (TextView) convertView.findViewById(R.id.author_textview);
            holder.content= (TextView) convertView.findViewById(R.id.content_textview);
            convertView.setTag(holder);


        }
        else
        {

            holder= (ViewHolder) convertView.getTag();

        }
        holder.author.setText(authorList.get(position));
        holder.content.setText(contentList.get(position));
        return convertView;
    }
    static class ViewHolder {
        TextView author;
        TextView content;
    }
}
