package com.example.neeraj.spotifystreamer.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.neeraj.spotifystreamer.Fragment.MovieDetailFragment;
import com.example.neeraj.spotifystreamer.Fragment.MoviePosterLIstFragment;
import com.example.neeraj.spotifystreamer.Model.MovieDetailModel;
import com.example.neeraj.spotifystreamer.R;

import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity implements MoviePosterLIstFragment.OnItemSelectedListener {
    boolean mIsDualPane=false;
    boolean firstTimeGridview=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=getSharedPreferences("myPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("key","d4a6c95a8a7b0576f22425e1a518e28b");
        editor.putString("baseURL","http://api.themoviedb.org/");
        editor.putString("baseURLImage", "http://image.tmdb.org/t/p/w185");
        editor.commit();

        setContentView(R.layout.activity_main);
        View view=findViewById(R.id.fragment_detail_container);
          if(view != null)
        {
            mIsDualPane=true;
        }


    }

    @Override
    public void onItemSeLected(int position,MovieDetailModel movieDetailModel) {
        if(mIsDualPane)
        {android.support.v4.app.FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            Bundle bundle=new Bundle();
            bundle.putParcelable("object", Parcels.wrap(movieDetailModel));
            Fragment detailFragment=new MovieDetailFragment();
            detailFragment.setArguments(bundle);
            transaction.replace(R.id.fragment_detail_container,detailFragment,null).commit();
        }
        else
        {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("object", Parcels.wrap(movieDetailModel));
            startActivity(intent);
        }

    }
}


