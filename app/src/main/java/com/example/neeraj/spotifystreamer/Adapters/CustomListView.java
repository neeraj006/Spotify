package com.example.neeraj.spotifystreamer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neeraj.spotifystreamer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neeraj on 30-04-2016.
 */
public class CustomListView extends BaseAdapter {
    Context context;
    List<String> trailerNameList=new ArrayList<>();

    public CustomListView(Context context,List<String> trailerNameList) {
        this.context=context;
        this.trailerNameList=trailerNameList;
    }

    @Override
    public int getCount() {
        return trailerNameList.size();
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
                    .inflate(R.layout.custom_listview, parent, false);
             holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.dummy_imageview);
            holder.text= (TextView) convertView.findViewById(R.id.name_textview);
            convertView.setTag(holder);


        }
        else
        {

            holder= (ViewHolder) convertView.getTag();

        }
        holder.text.setText(trailerNameList.get(position));
        holder.icon.setImageResource(R.drawable.arrow);
        return convertView;
    }
    static class ViewHolder {
        TextView text;
        ImageView icon;
    }
}
