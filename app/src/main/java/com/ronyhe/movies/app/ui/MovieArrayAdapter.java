package com.ronyhe.movies.app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.ronyhe.movies.app.R;
import com.ronyhe.movies.app.data.Movie;
import com.ronyhe.movies.app.data.MovieList;
import com.ronyhe.movies.app.network.ImageLoading;

/** An ArrayAdapter used to populate the movie grid */
final class MovieArrayAdapter extends ArrayAdapter<Movie> {

    public MovieArrayAdapter(Context context) {
        super(context, R.layout.grid_item_movie);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
        }

        Movie movie = getItem(position);
        ImageView image = (ImageView) convertView.findViewById(R.id.grid_item_image);

        ImageLoading.loadMoviePosterIntoTarget(getContext(), movie, image);

        return convertView;
    }

    public MovieList getMovies() {
        MovieList movies = new MovieList();
        for (int i = 0, n = getCount(); i < n; i++) {
            movies.add(getItem(i));
        }

        return movies;
    }

}
