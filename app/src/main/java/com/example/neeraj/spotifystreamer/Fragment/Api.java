package com.example.neeraj.spotifystreamer.Fragment;

import com.example.neeraj.spotifystreamer.Model.MovieDetailModel;
import com.example.neeraj.spotifystreamer.Model.ReviewModel;
import com.example.neeraj.spotifystreamer.Model.TrailerModel;

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

    @GET("3/movie/{id}/videos")
    Call<ArrayList<TrailerModel>> fetchTrailers(@Path("id") String id,@Query("api_key") String key);

    @GET("3/movie/{id}/reviews")
    Call<ArrayList<ReviewModel>> fetchReviews(@Path("id") String id,@Query("api_key") String key);

}
