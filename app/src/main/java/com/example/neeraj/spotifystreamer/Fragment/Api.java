package com.example.neeraj.spotifystreamer.Fragment;

import com.example.neeraj.spotifystreamer.Model.MovieDetailModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Neeraj on 14-04-2016.
 */
public interface Api
{

    @GET("3/movie/{preference}")
    Call<ArrayList<MovieDetailModel>> fetchMovieDetails(@Path("preference") String preference,@Query("api_key") String key);
}
