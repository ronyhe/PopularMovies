package com.ronyhe.movies.app.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.ronyhe.movies.app.R;

import java.util.Collections;
import java.util.Comparator;

/**
 * Static utility methods for sorting Movie objects.
 *
 * Maintainers should see the getRelevantComparator() method when adding or removing sorting methods.
 */
public final class MovieSorting {

    private MovieSorting() {}  // Non instantiable

    public static void sortMoviesByUserPreference(Context context, MovieList movies) {
        String currentSetting = getCurrentSortMethod(context);
        Comparator<Movie> comparator = getRelevantComparator(context, currentSetting);
        comparator = Collections.reverseOrder(comparator);
        Collections.sort(movies, comparator);
    }

    private static String getCurrentSortMethod(Context context) {
        String sortingPreferenceKey = context.getString(R.string.pref_sort_key);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(sortingPreferenceKey, "");
    }

    private static Comparator<Movie> getRelevantComparator(Context context, String currentSortSetting) {
        String ratingPreferenceValue = context.getString(R.string.pref_sort_rating_value);
        String popularityPreferenceValue = context.getString(R.string.pref_sort_popularity_value);

        if (currentSortSetting.equals(ratingPreferenceValue)) {
            return RATING_COMPARATOR;
        } else if (currentSortSetting.equals(popularityPreferenceValue)) {
            return POPULARITY_COMPARATOR;
        } else {
            // This situation shouldn't occur. In a production app I would log it somehow.
            // Possibly using a framework that reports back to me such as Crashlytics.
            // https://try.crashlytics.com/
            return DEFAULT_COMPARATOR;
        }
    }

    private static final Comparator<Movie> POPULARITY_COMPARATOR = new Comparator<Movie>() {
        @Override
        public int compare(Movie lhs, Movie rhs) {
            return Double.compare(lhs.getPopularity(), rhs.getPopularity());
        }
    };

    private static final Comparator<Movie> RATING_COMPARATOR = new Comparator<Movie>() {
        @Override
        public int compare(Movie lhs, Movie rhs) {
            return Double.compare(lhs.getRating(), rhs.getRating());
        }
    };

    private static final Comparator<Movie> DEFAULT_COMPARATOR = POPULARITY_COMPARATOR;
}
