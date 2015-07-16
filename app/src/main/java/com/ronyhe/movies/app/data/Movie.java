package com.ronyhe.movies.app.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a movie.
 *
 * Note that this class is not portable across APIs
 * It's designed with the API from https://www.themoviedb.org/ in mind.
 * The getId() and getPosterId() methods, for example, are tightly related to
 * this specific API.
 */
public final class Movie implements Parcelable {

    private final Params params;

    public Movie(Params params) {
        this.params = defensivelyCopyParams(params);
    }

    public String getId() {
        return params.id;
    }

    public String getTitle() {
        return params.title;
    }

    public String getPosterId() {
        return params.posterId;
    }

    public String getPlot() {
        return params.plot;
    }

    public double getRating() {
        return params.rating;
    }

    public double getPopularity() {
        return params.popularity;
    }

    public String getReleaseYear() {
        return params.releaseYear;
    }

    private static Params defensivelyCopyParams(Params in) {
        Params out = new Params();

        out.id = in.id;
        out.title = in.title;
        out.posterId = in.posterId;
        out.plot = in.plot;
        out.releaseYear = in.releaseYear;

        out.rating = in.rating;
        out.popularity = in.popularity;

        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(params.id);
        dest.writeString(params.title);
        dest.writeString(params.plot);
        dest.writeString(params.releaseYear);
        dest.writeString(params.posterId);

        dest.writeDouble(params.rating);
        dest.writeDouble(params.popularity);
    }

    /**
     * Used by the OS as part of the Parcelable interface.
     *
     * DO NOT use to manually create instances.
     * DO NOT rename.
     */
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            Params params = new Params();

            params.id = source.readString();
            params.title = source.readString();
            params.plot = source.readString();
            params.releaseYear = source.readString();
            params.posterId = source.readString();

            params.rating = source.readDouble();
            params.popularity = source.readDouble();

            return new Movie(params);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /**
     * Parameters needed for the Movie constructor.
     * Meant to aid in producing more readable code.
     */
    public static final class Params {

        public String id;
        public String title;
        public String posterId;
        public String plot;
        public String releaseYear;

        public double rating;
        public double popularity;
    }

}
