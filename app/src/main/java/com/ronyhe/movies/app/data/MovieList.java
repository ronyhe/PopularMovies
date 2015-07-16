package com.ronyhe.movies.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 *  Represents a collection of movies.
 *
 *  Exists for several reasons:
 *   - Encapsulate Generics, leading to cleaner code.
 *   - Make sure ArrayList is used across the application,
 *   which is important for compatibility with MovieArrayAdapter
 *   - Implementing Parcelable easily and in one place.
 */
public final class MovieList extends ArrayList<Movie> implements Parcelable {

    /**
     * Used by the OS as part of the Parcelable interface.
     *
     * DO NOT use to manually create instances.
     * DO NOT rename.
     */
    public static final Creator<MovieList> CREATOR = new Creator<MovieList>() {
        @Override
        public MovieList createFromParcel(Parcel source) {
            MovieList list = new MovieList();
            source.readTypedList(list, Movie.CREATOR);
            return list;
        }

        @Override
        public MovieList[] newArray(int size) {
            return new MovieList[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this);
    }
}
