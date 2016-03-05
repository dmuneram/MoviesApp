package com.munera.android.moviesapp.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Diego Munera on 28/02/16.
 */
public class MovieInfo implements Parcelable {
    public Long id;
    public String posterPath;
    public String overview;
    public String release_date;
    public String original_title;
    public Double vote_average;

    public MovieInfo(Parcel in) {
        this.id = in.readLong();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
        this.original_title = in.readString();
        this.vote_average = in.readDouble();
    }

    public MovieInfo(Long id, String posterPath, String overview, String release_date, String original_title, Double vote_average) {
        this.id = id;
        this.posterPath = posterPath;
        this.overview = overview;
        this.release_date = release_date;
        this.original_title = original_title;
        this.vote_average = vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(original_title);
        dest.writeDouble(vote_average);
    }

    public final static Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel parcel) {
            return new MovieInfo(parcel);
        }

        @Override
        public MovieInfo[] newArray(int i) {
            return new MovieInfo[i];
        }

    };
}
