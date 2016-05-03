package com.example.neeraj.spotifystreamer.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.neeraj.spotifystreamer.Adapters.CustomTypeFactory;
import com.example.neeraj.spotifystreamer.Adapters.GridviewAdapter;
import com.example.neeraj.spotifystreamer.Classes.DataBaseHelper;
import com.example.neeraj.spotifystreamer.Model.MovieDetailModel;
import com.example.neeraj.spotifystreamer.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MoviePosterLIstFragment extends Fragment {

    String preference = "top_rated";
    List<MovieDetailModel> posterPathList = new ArrayList<MovieDetailModel>();
    OnItemSelectedListener inter=null;
    GridviewAdapter gridviewAdapter;
    boolean flag = true;
    ProgressDialog progressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       inter= (OnItemSelectedListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_poster_list, container, false);
        setHasOptionsMenu(true);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage(" Loading :) ");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(flag);

        GridView gridView = (GridView) view.findViewById(R.id.grid_view);
        gridviewAdapter=new GridviewAdapter(posterPathList,getContext());
        gridView.setAdapter(gridviewAdapter);
       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               inter.onItemSeLected(position, posterPathList.get(position));

           }
       });
        if(savedInstanceState!=null)
        {
            preference= Parcels.unwrap(savedInstanceState.getParcelable("preference"));
            ArrayList<MovieDetailModel> temp=Parcels.unwrap(savedInstanceState.getParcelable("list"));
            posterPathList.addAll(temp);
            gridviewAdapter.notifyDataSetChanged();
        }
        else {

            fetchData();
        }


        return view;

    }
    public interface OnItemSelectedListener
    {
        void onItemSeLected(int position,MovieDetailModel movieDetailModel);

    }
    void fetchData()
    {
        progressDialog.show();
        posterPathList.clear();

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
        Call<ArrayList<MovieDetailModel>> call=api.fetchMovieDetails(preference, getContext().getSharedPreferences("myPreference", Context.MODE_PRIVATE).getString("key", null));
        call.enqueue(new Callback<ArrayList<MovieDetailModel>>() {
            @Override
            public void onResponse(Call<ArrayList<MovieDetailModel>> call, Response<ArrayList<MovieDetailModel>> response) {
                for(int i=0;i<response.body().size();i++)
                {
                    posterPathList.add(response.body().get(i));

                    gridviewAdapter.notifyDataSetChanged();
               }
                if(!(getActivity().findViewById(R.id.fragment_detail_container)==null)) {

                    inter.onItemSeLected(0, posterPathList.get(0));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<MovieDetailModel>> call, Throwable t) {
            progressDialog.dismiss();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.popular_item&&preference!="popular")
        {  preference="popular";
            fetchData();

        }
       else if(item.getItemId()==R.id.top_rated_item&&preference!="top_rated")
        {  preference="top_rated";
            fetchData();

        }
        else if(item.getItemId()==R.id.favourite_item)
        {   preference="favourite";
            posterPathList.clear();
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
            Cursor cursor=database.query(DataBaseHelper.tableName,projection,null,null,null,null,null,null);
            cursor.moveToFirst();
            try {
                while (cursor.moveToNext()) {
                    MovieDetailModel movieDetailModel=new MovieDetailModel();
                    movieDetailModel.setId(cursor.getInt(0));
                    movieDetailModel.setTitle(cursor.getString(1));
                    movieDetailModel.setPoster_path(cursor.getString(2));
                    movieDetailModel.setRelease_date(cursor.getString(3));
                    movieDetailModel.setVote_average(cursor.getDouble(4));
                    movieDetailModel.setOverview(cursor.getString(5));



                    movieDetailModel.setImage(cursor.getBlob(6));
                    posterPathList.add(movieDetailModel);
                }
            } finally {
                gridviewAdapter.notifyDataSetChanged();
                if(posterPathList.size()>0) {
                    if(getActivity().findViewById(R.id.fragment_detail_container)!=null) {
                        inter.onItemSeLected(0, posterPathList.get(0));
                    }
                }
                else
                {
                    Toast toast=Toast.makeText(getContext(),"NO Favourite Movie to Show",Toast.LENGTH_SHORT);
                    toast.show();
                }
                cursor.close();
                database.close();
            }


        }
        else
        {
            Toast toast=Toast.makeText(getContext(),"Already Done",Toast.LENGTH_SHORT);
            toast.show();
        }
        return  true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu,menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("list",Parcels.wrap(posterPathList));
        outState.putParcelable("preference",Parcels.wrap(preference));
        super.onSaveInstanceState(outState);
    }
}

