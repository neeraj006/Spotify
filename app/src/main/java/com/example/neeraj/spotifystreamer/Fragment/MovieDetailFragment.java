package com.example.neeraj.spotifystreamer.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.neeraj.spotifystreamer.Adapters.CustomListView;
import com.example.neeraj.spotifystreamer.Adapters.CustomTypeFactory;
import com.example.neeraj.spotifystreamer.Adapters.ReviewListview;
import com.example.neeraj.spotifystreamer.Classes.DataBaseHelper;
import com.example.neeraj.spotifystreamer.Model.MovieDetailModel;
import com.example.neeraj.spotifystreamer.Model.ReviewModel;
import com.example.neeraj.spotifystreamer.Model.TrailerModel;
import com.example.neeraj.spotifystreamer.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieDetailFragment extends Fragment {
    TextView title;
    ImageView poster;
    TextView releaseDate;
    TextView plotSynopsis;
    TextView vote;
    DataBaseHelper dataBaseHelper;
    List<String> trailerNameList=new ArrayList<>();
    List<String> keyList=new ArrayList<>();
    List<String> reviewList=new ArrayList<>();
    List<String> authorList =new ArrayList<>();



    ListView   listView;
    ListView reviewListview;
    Button button;
    int id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle=new Bundle();
        bundle=getArguments();
     final  MovieDetailModel model;
        model= Parcels.unwrap(bundle.getParcelable("object"));
        View view= inflater.inflate(R.layout.fragment_movie_detail, container, false);
        listView= (ListView) view.findViewById(R.id.list_view);
        reviewListview= (ListView) view.findViewById(R.id.review_listview);
        button= (Button) view.findViewById(R.id.favourite_button);

        title= (TextView) view.findViewById(R.id.title_textview);
        poster= (ImageView) view.findViewById(R.id.poster_imageview);
        vote= (TextView) view.findViewById(R.id.vote_textview);
        releaseDate= (TextView) view.findViewById(R.id.release_date_textview);
        plotSynopsis= (TextView) view.findViewById(R.id.plotSynopsis_textview);
        dataBaseHelper=new DataBaseHelper(getContext());
        title.setText(model.getTitle());        vote.setText(String.valueOf(model.getVote_average()));
        releaseDate.setText(model.getRelease_date());
        plotSynopsis.setText(model.getOverview());
        id=model.getId();
        final SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        final String[] projection = {
                DataBaseHelper.id,
                DataBaseHelper.movieTitle,
                DataBaseHelper.posterPath,
                DataBaseHelper.releaseDate,
                DataBaseHelper.voteAverage,
                DataBaseHelper.overView,
                DataBaseHelper.trailers,
                DataBaseHelper.reviews,
                DataBaseHelper.key,
                DataBaseHelper.author,
                DataBaseHelper.image
        };
        final String selection=DataBaseHelper.id+"=?";
        final String[] selectionArgs = { ""+id};

        final Cursor cursor=db.query(DataBaseHelper.tableName,projection,selection,selectionArgs,null,null,null,null);
        cursor.moveToFirst();
        if(cursor.getCount()==0)
        {
            Picasso.with(getContext()).load(getContext().getSharedPreferences("myPreference", Context.MODE_PRIVATE).getString("baseURLImage",null)+
                    model.getPoster_path()).placeholder(android.R.drawable.btn_default).error(R.drawable.common_full_open_on_phone).into(poster);

            button.setBackgroundResource(R.drawable.fav_unselected);

        }
        else
        {
            button.setBackgroundResource(R.drawable.fav_selected);
            Bitmap bitmap= BitmapFactory.decodeByteArray(cursor.getBlob(10), 0, cursor.getBlob(10).length);
            poster.setImageBitmap(bitmap);

        }
        db.close();
        fetchTrailers();
        fetchReviews();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
                final Cursor cursor=db.query(DataBaseHelper.tableName, projection, selection, selectionArgs, null, null, null, null);
                cursor.moveToFirst();
                if(cursor.getCount()==0)
                {   button.setBackgroundResource(R.drawable.fav_selected);
                    final ContentValues contentValues=new ContentValues();
                    contentValues.put(DataBaseHelper.movieTitle,model.getTitle());
                    contentValues.put(DataBaseHelper.posterPath,model.getPoster_path());
                    contentValues.put(DataBaseHelper.overView,model.getOverview());
                    contentValues.put(DataBaseHelper.releaseDate,model.getRelease_date());
                    contentValues.put(DataBaseHelper.id, id);
                    contentValues.put(DataBaseHelper.voteAverage, model.getVote_average());
                    Gson gson = new Gson();
                    String inputString= gson.toJson(trailerNameList);
                    contentValues.put(DataBaseHelper.trailers,inputString);
                    inputString=gson.toJson(reviewList);
                    contentValues.put(DataBaseHelper.reviews,inputString);
                    inputString=gson.toJson(keyList);
                    contentValues.put(DataBaseHelper.key,inputString);
                    inputString=gson.toJson(authorList);
                    contentValues.put(DataBaseHelper.author,inputString);



                    Target target=new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();

                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] photo = baos.toByteArray();
                            contentValues.put(DataBaseHelper.image,photo);
                            db.insert(DataBaseHelper.tableName, null, contentValues);

                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    };
                    Picasso.with(getContext()).load(getContext().getSharedPreferences("myPreference", Context.MODE_PRIVATE).getString("baseURLImage",null)+
                            model.getPoster_path()).into(target);

                   // db.insertUserDetails(value1, value2, value3, photo, value2);
                }
                else
                {
                    button.setBackgroundResource(R.drawable.fav_unselected);
                    db.delete(DataBaseHelper.tableName, selection, selectionArgs);
                }
                cursor.close();
                db.close();
            }
        });






        return  view;
    }
    void fetchTrailers()
    {   DataBaseHelper dataBaseHelper=new DataBaseHelper(getContext());
        SQLiteDatabase database= dataBaseHelper.getReadableDatabase();
        String[] projection = {
                DataBaseHelper.id,
                DataBaseHelper.movieTitle,
                DataBaseHelper.posterPath,
                DataBaseHelper.releaseDate,
                DataBaseHelper.voteAverage,
                DataBaseHelper.overView,
                DataBaseHelper.trailers,
                DataBaseHelper.reviews,
                DataBaseHelper.key,
                DataBaseHelper.author,
                DataBaseHelper.image
        };
        final String selection=DataBaseHelper.id+"=?";
        final String[] selectionArgs = { ""+id};
        Cursor cursor=database.query(DataBaseHelper.tableName, projection, selection, selectionArgs, null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()!=0) {

            Gson gson2 = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> trailers = gson2.fromJson(cursor.getString(6), type);
            ArrayList<String> key = gson2.fromJson(cursor.getString(8), type);
            keyList=key;

            listView.setAdapter(new CustomListView(getContext(), trailers));
           // setListViewHeightBasedOnChildren(listView);

database.close();

        }
        else
        {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new CustomTypeFactory()).create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getContext().getSharedPreferences("myPreference", Context.MODE_PRIVATE).getString("baseURL", null))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        Api api=retrofit.create(Api.class);

        Call<ArrayList<TrailerModel>> call=api.fetchTrailers("" + id, getContext().getSharedPreferences("myPreference", Context.MODE_PRIVATE).getString("key", null));
        call.enqueue(new Callback<ArrayList<TrailerModel>>() {
            @Override
            public void onResponse(Call<ArrayList<TrailerModel>> call, Response<ArrayList<TrailerModel>> response) {
                 for(int i=0;i<response.body().size();i++)
                 {
                   trailerNameList.add(response.body().get(i).getName());
                     keyList.add(response.body().get(i).getKey());

                 }

                listView.setAdapter(new CustomListView(getContext(), trailerNameList));
               setListViewHeightBasedOnChildren(listView);


            }

            @Override
            public void onFailure(Call<ArrayList<TrailerModel>> call, Throwable t) {

            }
        });

    }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + keyList.get(position))));

            }
        });}
    void fetchReviews()
    {

            DataBaseHelper dataBaseHelper=new DataBaseHelper(getContext());
            SQLiteDatabase database= dataBaseHelper.getReadableDatabase();
            String[] projection = {
                    DataBaseHelper.id,
                    DataBaseHelper.movieTitle,
                    DataBaseHelper.posterPath,
                    DataBaseHelper.releaseDate,
                    DataBaseHelper.voteAverage,
                    DataBaseHelper.overView,
                    DataBaseHelper.trailers,
                    DataBaseHelper.reviews,
                    DataBaseHelper.key,
                    DataBaseHelper.author,
                    DataBaseHelper.image
            };
            final String selection=DataBaseHelper.id+"=?";
            final String[] selectionArgs = { ""+id};
            Cursor cursor=database.query(DataBaseHelper.tableName, projection, selection, selectionArgs, null, null, null, null);
            cursor.moveToFirst();
            if(cursor.getCount()!=0) {

                Gson gson2 = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();
                ArrayList<String> reviews = gson2.fromJson(cursor.getString(7), type);
                ArrayList<String> author = gson2.fromJson(cursor.getString(9), type);

                reviewListview.setAdapter(new ReviewListview(getContext(), author, reviews));
                //setListViewHeightBasedOnChildren(reviewListview);

                database.close();

            }
        else {


            Gson gson = new GsonBuilder().registerTypeAdapterFactory(new CustomTypeFactory()).create();

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getContext().getSharedPreferences("myPreference", Context.MODE_PRIVATE).getString("baseURL", null))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
            Api api = retrofit.create(Api.class);

            Call<ArrayList<ReviewModel>> call = api.fetchReviews("" + id, getContext().getSharedPreferences("myPreference", Context.MODE_PRIVATE).getString("key", null));
            call.enqueue(new Callback<ArrayList<ReviewModel>>() {
                @Override
                public void onResponse(Call<ArrayList<ReviewModel>> call, Response<ArrayList<ReviewModel>> response) {

                    for (int i = 0; i < response.body().size(); i++) {
                        reviewList.add(response.body().get(i).getContent());
                        authorList.add(response.body().get(i).getAuthor());

                    }

                    reviewListview.setAdapter(new ReviewListview(getContext(), authorList, reviewList));
                    setListViewHeightBasedOnChildren(reviewListview);


                }

                @Override
                public void onFailure(Call<ArrayList<ReviewModel>> call, Throwable t) {

                }
            });
        }

    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight +45
                + (listView.getDividerHeight() * (listAdapter.getCount()-1 ));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
