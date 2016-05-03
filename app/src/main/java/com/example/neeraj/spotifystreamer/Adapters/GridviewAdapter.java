package com.example.neeraj.spotifystreamer.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.neeraj.spotifystreamer.Classes.DataBaseHelper;
import com.example.neeraj.spotifystreamer.Model.MovieDetailModel;
import com.example.neeraj.spotifystreamer.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neeraj on 04-04-2016.
 */
public class GridviewAdapter extends BaseAdapter
{ List<MovieDetailModel> list=new ArrayList<>();
    Context context;
    public GridviewAdapter(List<MovieDetailModel> list,Context context) {
        this.list=list;
        this.context=context;

    }

    @Override
    public int getItemViewType(int position) {
        return IGNORE_ITEM_VIEW_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        return list.size();
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
        final ImageView imageView;
        if(convertView==null) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gridview_items, parent, false);
            imageView = (ImageView) v.findViewById(R.id.recyclerview_imageview);

        }
        else
        {
            imageView= (ImageView) convertView;
        }
        DataBaseHelper dataBaseHelper=new DataBaseHelper(context);
        SQLiteDatabase database= dataBaseHelper.getReadableDatabase();
        String[] projection = {
                DataBaseHelper.id,
                DataBaseHelper.movieTitle,
                DataBaseHelper.posterPath,
                DataBaseHelper.releaseDate,
                DataBaseHelper.voteAverage,
                DataBaseHelper.overView,
                DataBaseHelper.image
        };
        final String selection=DataBaseHelper.id+"=?";
        final String[] selectionArgs = { ""+list.get(position).getId()};
        Cursor cursor=database.query(DataBaseHelper.tableName, projection, selection, selectionArgs, null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()!=0)
        {
            Bitmap bitmap= BitmapFactory.decodeByteArray(cursor.getBlob(6), 0, cursor.getBlob(6).length);
            imageView.setImageBitmap(bitmap);
        }
      else {
            Picasso.with(context).load(context.getSharedPreferences("myPreference", Context.MODE_PRIVATE).getString("baseURLImage", null) + list.get(position).getPoster_path()).placeholder(R.drawable.common_ic_googleplayservices).error(R.drawable.common_full_open_on_phone).into(imageView, new Callback() {
                @Override
                public void onSuccess() {


                }

                @Override
                public void onError() {

                }
            });
        }
        return imageView;
    }
}

