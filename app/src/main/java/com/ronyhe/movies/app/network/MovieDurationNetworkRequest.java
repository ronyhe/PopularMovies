package com.ronyhe.movies.app.network;


import android.content.Context;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ronyhe.movies.app.R;
import com.ronyhe.movies.app.data.Movie;
import com.ronyhe.movies.app.data.MovieDeserialization;
import com.ronyhe.movies.app.network.AsyncRequests.AsyncRequest;
import com.ronyhe.movies.app.network.AsyncRequests.AsyncRequestCallback;
import com.ronyhe.movies.app.network.AsyncRequests.AsyncResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The design of the API we use prevents us from getting the most popular movies and their durations
 * in the same request. This class represents a network request to get the missing information.
 *
 * The networking utilizes the Volley framework.
 * See more at: https://developer.android.com/training/volley/simple.html
 */
public final class MovieDurationNetworkRequest implements AsyncRequest <Integer> {

    // The recommended pattern for Volley's RequestQueue is to obtain it from a singleton for performance reasons.
    // Since this app requires very little networking, we ignore this recommendation in favor of simplicity.
    // See more at: https://developer.android.com/training/volley/requestqueue.html#singleton
    private final RequestQueue networkRequestQueue;
    private final Movie movie;
    private final Context context;

    private AsyncRequestCallback<Integer> callback;

    public MovieDurationNetworkRequest(Context context, Movie movie) {
        this.movie = movie;
        this.context = context;
        networkRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void send(AsyncRequestCallback<Integer> callback) {
        this.callback = callback;
        JsonObjectRequest volleyRequest = getVolleyRequest();
        networkRequestQueue.add(volleyRequest);
    }

    private JsonObjectRequest getVolleyRequest() {
        int method = Method.GET;
        String url = getUrl();
        Listener<JSONObject> listener = getVolleyListener();
        ErrorListener errorListener = getVolleyErrorListener();
        return new JsonObjectRequest(method, url, null, listener, errorListener);

    }

    private ErrorListener getVolleyErrorListener() {
        return new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AsyncResponse<Integer> response = AsyncRequests.newFailedResponse(volleyError);
                callback.onResponseReceived(response);
            }
        };
    }

    private Listener<JSONObject> getVolleyListener() {
        return new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                AsyncResponse<Integer> response;
                try {
                    int duration = MovieDeserialization.getMovieDurationFromJson(jsonObject);
                    response = AsyncRequests.newSuccessfulResponse(duration);
                } catch (JSONException e) {
                    response = AsyncRequests.newFailedResponse(e);
                }

                callback.onResponseReceived(response);
            }
        };
    }

    private String getUrl() {
        // For more on the url structure, see the API docs at https://www.themoviedb.org/documentation/api
        // And the corresponding string resources.
        String base = context.getString(R.string.url_base_for_single_movie);
        String apiKey = getApiKey();
        String urlParameterForApiKey = context.getString(R.string.url_parameter_for_api_key_with_equals_sign);
        String movieId = movie.getId();
        return base + movieId + "?" + urlParameterForApiKey + apiKey;
    }

    private String getApiKey() {
        return context.getResources().getString(R.string.movies_api_key);
    }
}
