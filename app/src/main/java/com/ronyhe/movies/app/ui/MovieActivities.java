package com.ronyhe.movies.app.ui;


import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import com.ronyhe.movies.app.R;

/**
 * The activities used in the app.
 *
 * These are pretty much blank templates because we use Fragments for our main functionality.
 * Notice however, that PopularMoviesActivity which is the main activity preforms the initializing
 * of user preferences here.
 */
public final class MovieActivities {
    private MovieActivities() {}

    public static final class PopularMoviesActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_popular_movies);
            initializeUserPreferences();
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new MoviesGridFragment())
                        .commit();
            }
        }

        @SuppressWarnings("ConstantConditions")  // The "redundant" variable facilitates clarity
        private void initializeUserPreferences() {
            boolean resetToDefaultPreferencesEveryTime = false;
            PreferenceManager.setDefaultValues(this, R.xml.preferences, resetToDefaultPreferencesEveryTime);
        }


    }

    public static final class MovieDetailsActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_movie_details);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new MovieDetailsFragment())
                        .commit();
            }
        }

    }

    public static final class SettingsActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            displayTheFragmentAsTheMainContent();
        }

        private void displayTheFragmentAsTheMainContent() {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();
        }

        public static final class SettingsFragment extends PreferenceFragment {

            public SettingsFragment() {}

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.preferences);
            }
        }

    }
}
