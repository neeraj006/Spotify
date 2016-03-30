package com.example.neeraj.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    JSONObject movieDataObject;
    TextView title;
    ImageView poster;
    TextView releaseDate;
    TextView plotSynopsis;
    TextView vote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        try {
             movieDataObject=new JSONObject(getIntent().getStringExtra("jsonString"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
         title= (TextView) findViewById(R.id.title_textview);
        poster= (ImageView) findViewById(R.id.poster_imageview);
        vote= (TextView) findViewById(R.id.vote_textview);
        releaseDate= (TextView) findViewById(R.id.release_date_textview);
        plotSynopsis= (TextView) findViewById(R.id.plotSynopsis_textview);


        try {
            Picasso.with(DetailActivity.this).load(getIntent().getStringExtra("baseURLImage") + new MoviesData(movieDataObject).getPosterPath()).into(poster);

            title.setText(new MoviesData(movieDataObject).getTitle());
            vote.setText(new MoviesData(movieDataObject).getVoteAverage().toString());
            releaseDate.setText(new MoviesData(movieDataObject).getReleaseDate());
            plotSynopsis.setText(new MoviesData(movieDataObject).getPlotSynopsis());


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
