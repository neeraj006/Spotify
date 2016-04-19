package com.example.neeraj.spotifystreamer.Model;

import org.parceler.Parcel;

/**
 * Created by Neeraj on 14-04-2016.
 */
@Parcel
public class MovieDetailModel {
    String poster_path;
    String overview;
    String release_date;
    int id;
    String title;
    long vote_average;

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getVote_average() {
        return vote_average;
    }
}
