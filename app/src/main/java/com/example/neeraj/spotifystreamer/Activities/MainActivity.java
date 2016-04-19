package com.example.neeraj.spotifystreamer.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.neeraj.spotifystreamer.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=getSharedPreferences("myPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("key","d4a6c95a8a7b0576f22425e1a518e28b");
        editor.putString("baseURL","http://api.themoviedb.org/");
        editor.putString("baseURLImage","http://image.tmdb.org/t/p/w185");
        editor.commit();
        SQLiteDatabase mydatabase = openOrCreateDatabase("favourite",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS movieDetails(movieTitle VARCHAR,posterPath VARCHAR,overview VARCHAR," +
                "releaseDate VARCHAR,id INTEGER,voteAverage REAL);");


        setContentView(R.layout.activity_main);


    }
}


