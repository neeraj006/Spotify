package com.example.neeraj.spotifystreamer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Neeraj on 28-03-2016.
 */
public class MoviesData {
    String title;
    String releaseDate;
    String posterPath;

    Double voteAverage;
    String plotSynopsis;
    MoviesData(JSONObject jsonObject) throws JSONException {
       title=jsonObject.getString("title");
        releaseDate=jsonObject.getString("release_date");
        posterPath=jsonObject.getString("poster_path");
        plotSynopsis=jsonObject.getString("overview");
        voteAverage=  jsonObject.getDouble("vote_average");

    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }



    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }
}
