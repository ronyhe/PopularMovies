package com.ronyhe.movies.app.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import com.ronyhe.movies.app.R;
import com.ronyhe.movies.app.data.Movie;
import com.ronyhe.movies.app.data.MovieList;
import com.ronyhe.movies.app.data.MovieSorting;
import com.ronyhe.movies.app.network.PopularMoviesNetworkRequest;
import com.ronyhe.movies.app.network.AsyncRequests.AsyncRequestCallback;
import com.ronyhe.movies.app.network.AsyncRequests.AsyncResponse;

/**
 * Presents a grid with the most popular movies.
 * Enables sorting the movies by popularity or by average user rating.
 */
public final class MoviesGridFragment extends Fragment {

    private static final String MOVIES_KEY_FOR_BUNDLE = MoviesGridFragment.class.getName() + ".movies";

    private MovieArrayAdapter movieAdapterForTheGrid;
    private View loadingAnimation;

    // Used by the OS
    public MoviesGridFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        loadingAnimation = rootView.findViewById(R.id.progressBar);

        GridView movieGrid = (GridView) rootView.findViewById(R.id.movies_container);
        attachClickListenerToGrid(movieGrid);

        movieAdapterForTheGrid = getMovieAdapterForTheGrid();
        movieGrid.setAdapter(movieAdapterForTheGrid);

        populateMovieGrid(savedInstanceState);
        setHasOptionsMenu(true);

        return rootView;
    }


    private void populateMovieGrid(Bundle savedInstanceState) {
        boolean isResuming = savedInstanceState != null;
        if (isResuming) {
            populateMovieGridFromBundle(savedInstanceState);
            hideLoadingAnimation();
        } else {
            populateMovieGridFromNetworkAndThenHideLoadingAnimation();
        }
    }

    private void populateMovieGridFromNetworkAndThenHideLoadingAnimation() {
        final PopularMoviesNetworkRequest request = new PopularMoviesNetworkRequest(getActivity());
        request.send(new AsyncRequestCallback<MovieList>() {
            @Override
            public void onResponseReceived(AsyncResponse<MovieList> response) {
                if (response.wasRequestSuccessful()) {
                    MovieList movies = response.getResult();
                    sortMovies(movies);
                    replaceAllMoviesInAdapter(movies);
                } else {
                    showFailedNetworkRequestDialog();
                }
                hideLoadingAnimation();
            }
        });
    }

    private void populateMovieGridFromBundle(Bundle savedInstanceState) {
        MovieList movies = savedInstanceState.getParcelable(MOVIES_KEY_FOR_BUNDLE);
        sortMovies(movies);
        replaceAllMoviesInAdapter(movies);
    }

    private void showFailedNetworkRequestDialog() {
        Runnable retry = new Runnable() {
            @Override
            public void run() {
                populateMovieGridFromNetworkAndThenHideLoadingAnimation();
            }
        };

        AlertDialog failedRequestDialog =
                FailedNetworkRequestDialog.getInstance(getActivity(), retry);

        failedRequestDialog.show();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIES_KEY_FOR_BUNDLE, movieAdapterForTheGrid.getMovies());
    }

    @Override
    public void onResume() {
        super.onResume();
        resortMoviesInCaseUserReturnedFromSettingsMenu();
    }

    private void resortMoviesInCaseUserReturnedFromSettingsMenu() {
        showLoadingAnimation();
        MovieList movies = movieAdapterForTheGrid.getMovies();
        sortMovies(movies);
        replaceAllMoviesInAdapter(movies);
        hideLoadingAnimation();
    }

    private void replaceAllMoviesInAdapter(MovieList newMovies) {
        movieAdapterForTheGrid.clear();
        movieAdapterForTheGrid.addAll(newMovies);
        movieAdapterForTheGrid.notifyDataSetChanged();
    }

    private void sortMovies(MovieList movies) {
        MovieSorting.sortMoviesByUserPreference(getActivity(), movies);
    }

    private void attachClickListenerToGrid(GridView movieGrid) {
        movieGrid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie selectedMovie = movieAdapterForTheGrid.getItem(position);
                Intent details = MovieDetailsFragment.getDetailsIntent(getActivity(), selectedMovie);
                getActivity().startActivity(details);
            }
        });
    }

    private MovieArrayAdapter getMovieAdapterForTheGrid() {
        MovieArrayAdapter adapter = new MovieArrayAdapter(getActivity());
        adapter.setNotifyOnChange(false);
        return adapter;
    }

    private void hideLoadingAnimation() {
        loadingAnimation.setVisibility(View.GONE);
    }

    private void showLoadingAnimation() {
        loadingAnimation.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_grid_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            launchSettingsActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchSettingsActivity() {
        Intent settings = new Intent(getActivity(), MovieActivities.SettingsActivity.class);
        startActivity(settings);
    }

}
