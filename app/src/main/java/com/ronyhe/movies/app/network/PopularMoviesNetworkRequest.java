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
import com.ronyhe.movies.app.data.MovieList;
import com.ronyhe.movies.app.data.MovieDeserialization;
import com.ronyhe.movies.app.network.AsyncRequests.AsyncRequest;
import com.ronyhe.movies.app.network.AsyncRequests.AsyncRequestCallback;
import com.ronyhe.movies.app.network.AsyncRequests.AsyncResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a network request to the API that returns a MovieList containing the most popular movies.
 *
 * The networking utilizes the Volley framework.
 * See more at: https://developer.android.com/training/volley/simple.html
 */
public final class PopularMoviesNetworkRequest implements AsyncRequest<MovieList> {

    // The recommended pattern for Volley's RequestQueue is to obtain it from a singleton for performance reasons.
    // Since this app requires very little networking, we ignore this recommendation in favor of simplicity.
    // See more at: https://developer.android.com/training/volley/requestqueue.html#singleton
    private final RequestQueue networkRequestQueue;
    private final Context context;

    private AsyncRequestCallback<MovieList> callback;

    public PopularMoviesNetworkRequest(Context context) {
        this.context = context;
        networkRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void send(AsyncRequestCallback<MovieList> callback) {
        this.callback = callback;
        JsonObjectRequest request = getVolleyRequest();
        networkRequestQueue.add(request);
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
                AsyncResponse<MovieList> response = AsyncRequests.newFailedResponse(volleyError);
                callback.onResponseReceived(response);
            }
        };
    }

    private Listener<JSONObject> getVolleyListener() {
        return new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                AsyncResponse<MovieList> response;
                try {
                    MovieList movies = MovieDeserialization.convertJsonToMovieList(jsonObject);
                    response = AsyncRequests.newSuccessfulResponse(movies);
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
        String urlBase = context.getString(R.string.url_base_for_popular_movies);

        String apiKey = context.getString(R.string.movies_api_key);
        String urlParameterForApiKey = context.getString(R.string.url_parameter_for_api_key_with_equals_sign);
        String apiKeyParam = urlParameterForApiKey + apiKey;

        return urlBase + "&" + apiKeyParam;
    }

}
