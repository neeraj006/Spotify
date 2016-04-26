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
    double vote_average;

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

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

    public double getVote_average() {
        return vote_average;
    }
}
