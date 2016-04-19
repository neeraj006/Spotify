package com.example.neeraj.spotifystreamer.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neeraj.spotifystreamer.Fragment.MovieDetailFragment;
import com.example.neeraj.spotifystreamer.R;

public class DetailActivity extends AppCompatActivity {
    TextView title;
    ImageView poster;
    TextView releaseDate;
    TextView plotSynopsis;
    TextView vote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle=new Bundle();
        bundle.putParcelable("object",getIntent().getParcelableExtra("object"));
        Fragment fragment=new MovieDetailFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragment).commit();

    }

}
