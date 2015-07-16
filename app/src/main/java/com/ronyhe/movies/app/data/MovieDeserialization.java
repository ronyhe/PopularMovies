package com.ronyhe.movies.app.data;


import com.ronyhe.movies.app.data.Movie.Params;
import com.ronyhe.movies.app.utils.JsonIterable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Static utility methods to aid in converting Movies from the https://www.themoviedb.org/
 * to Movie objects.
 */
public final class MovieDeserialization {

    private MovieDeserialization() {}  // Non instantiable

    /** @param root is the response from the API without any previous processing. */
    public static MovieList convertJsonToMovieList(JSONObject root) throws JSONException {
        MovieList movieList = new MovieList();

        JSONArray movieJsonObjects = root.getJSONArray(JsonKeys.MOVIES_ARRAY);
        Iterable<JSONObject> iterable = new JsonIterable(movieJsonObjects);

        for (JSONObject obj : iterable) {
            Movie movie = convertJsonToMovie(obj);
            movieList.add(movie);
        }

        return movieList;
    }

    /** @param root is a JSONObject for a single movie */
    private static Movie convertJsonToMovie(JSONObject root) throws JSONException {
        Params params = new Params();

        params.id = root.getString(JsonKeys.ID);
        params.title = root.getString(JsonKeys.TITLE);
        params.plot = root.getString(JsonKeys.PLOT);
        params.releaseYear = root.getString(JsonKeys.RELEASE_DATE).substring(0, 4);
        params.posterId = root.getString(JsonKeys.POSTER);
        params.rating = root.getDouble(JsonKeys.RATING);
        params.popularity = root.getDouble(JsonKeys.POPULARITY);

        return new Movie(params);
    }

    /** @param root is the response from the API without any previous processing. */
    public static int getMovieDurationFromJson(JSONObject root) throws JSONException {
        return root.getInt(JsonKeys.DURATION);
    }

    private static final class JsonKeys {
        private JsonKeys() {}  // Non instantiable

        private static final String MOVIES_ARRAY = "results";

        private static final String ID = "id";
        private static final String TITLE = "original_title";
        private static final String PLOT = "overview";
        private static final String RELEASE_DATE = "release_date";
        private static final String POSTER = "poster_path";
        private static final String RATING = "vote_average";
        private static final String POPULARITY = "popularity";

        private static final String DURATION = "runtime";

    }

}
