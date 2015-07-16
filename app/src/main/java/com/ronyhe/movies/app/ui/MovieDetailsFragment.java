package com.ronyhe.movies.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ronyhe.movies.app.R;
import com.ronyhe.movies.app.data.Movie;
import com.ronyhe.movies.app.network.AsyncRequests.AsyncRequestCallback;
import com.ronyhe.movies.app.network.AsyncRequests.AsyncResponse;
import com.ronyhe.movies.app.network.ImageLoading;
import com.ronyhe.movies.app.network.MovieDurationNetworkRequest;

/**
 * Presents the details of the supplied Movie.
 *
 * This includes release year, duration, average user rating and plt synopsis.
 * Note that all attributes except duration are received in the Movie object that's provided as an extra.
 * The movie's duration however is requested from the network.
 */
public final class MovieDetailsFragment extends Fragment {

    private static final String MOVIE_KEY = MovieDetailsFragment.class.getName() + ".movie";
    private static final String DURATION_KEY = MovieDetailsFragment.class.getName() + ".duration";
    private static final int MOVIE_DURATION_REQUEST_FAILED_FLAG = -1;

    public static Intent getDetailsIntent(Context context, Movie movie) {
        Intent launchIntent = new Intent(context, MovieActivities.MovieDetailsActivity.class);
        launchIntent.putExtra(MOVIE_KEY, movie);
        return launchIntent;
    }

    public MovieDetailsFragment() {
    }

    private Movie movie;
    private int movieDuration;

    private View loadingAnimation;

    private ImageView image;

    private TextView title;
    private TextView year;
    private TextView plot;

    private TextView rating;
    private TextView duration;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        findViewsForFields(rootView);
        extractMovieAndPopulateViews(savedInstanceState);

        return rootView;
    }

    private void extractMovieAndPopulateViews(Bundle savedInstanceState) {
        boolean isResuming = savedInstanceState != null;
        if (isResuming) {
            extractMovieAndDurationFromSavedInstanceState(savedInstanceState);
            populateViewsInFields();
            hideLoadingAnimation();
        } else {
            extractMovieFromIntent();
            getMovieDurationFromNetworkAndThenPopulateViews();
        }
    }

    private void getMovieDurationFromNetworkAndThenPopulateViews() {
        final MovieDurationNetworkRequest request = new MovieDurationNetworkRequest(getActivity(), movie);
        request.send(new AsyncRequestCallback<Integer>() {
            @Override
            public void onResponseReceived(AsyncResponse<Integer> response) {
                if (response.wasRequestSuccessful()) {
                    movieDuration = response.getResult();
                    populateViewsInFields();
                    hideLoadingAnimation();
                } else {
                    movieDuration = MOVIE_DURATION_REQUEST_FAILED_FLAG;
                    hideLoadingAnimation();
                    populateViewsInFields();
                }
            }
        });
    }

    private void extractMovieFromIntent() {
        movie = getActivity().getIntent().getParcelableExtra(MOVIE_KEY);
    }

    private void extractMovieAndDurationFromSavedInstanceState(Bundle savedInstanceState) {
        movie = savedInstanceState.getParcelable(MOVIE_KEY);
        movieDuration = savedInstanceState.getInt(DURATION_KEY);
    }

    private void findViewsForFields(View rootView) {
        loadingAnimation = rootView.findViewById(R.id.progressBar);
        image = (ImageView) rootView.findViewById(R.id.details_image);

        title = (TextView) rootView.findViewById(R.id.details_title);
        year = (TextView) rootView.findViewById(R.id.details_year);
        duration = (TextView) rootView.findViewById(R.id.details_duration);
        rating = (TextView) rootView.findViewById(R.id.details_rating);
        plot = (TextView) rootView.findViewById(R.id.details_plot);
    }

    private void populateViewsInFields() {
        loadPosterIntoImage();

        title.setText(movie.getTitle());
        year.setText(movie.getReleaseYear());
        plot.setText(movie.getPlot());

        rating.setText(getRatingText());
        duration.setText(getDurationText());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_KEY, movie);
        outState.putInt(DURATION_KEY, movieDuration);
    }



    private void loadPosterIntoImage() {
        ImageLoading.loadMoviePosterIntoTarget(getActivity(), movie, image);
    }

    private String getRatingText() {
        String slashTen = getActivity().getString(R.string.slash_ten);
        return movie.getRating() + slashTen;
    }

    private String getDurationText() {
        if (movieDuration == MOVIE_DURATION_REQUEST_FAILED_FLAG) {
            return "";
        } else {
            String minutesText = getActivity().getString(R.string.minutes_abbreviation);
            return movieDuration + minutesText;
        }
    }

    private void hideLoadingAnimation() {
        loadingAnimation.setVisibility(View.GONE);
    }
}
