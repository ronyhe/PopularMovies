package com.ronyhe.movies.app.network;


import android.content.Context;
import android.widget.ImageView;
import com.ronyhe.movies.app.R;
import com.ronyhe.movies.app.data.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Static utility method for loading images from the web.
 *
 * The utilities are on top of the Picasso library.
 * See more at: http://square.github.io/picasso/
 */
public final class ImageLoading {

    private ImageLoading() {}  // Non instantiable

    /** In case of failure a default image will be loaded into the target */
    public static void loadMoviePosterIntoTarget(Context context, Movie movie, ImageView target) {
        String url = getUrlBaseFromContext(context) + movie.getPosterId();
        Callback callback = new PicassoImageCallback(context, target);
        Picasso.with(context).load(url).into(target, callback);
    }

    private static String getUrlBaseFromContext(Context context) {
        return context.getString(R.string.url_base_for_movie_posters);
    }

    private static final class PicassoImageCallback implements Callback {

        private final ImageView target;
        private final Context context;

        public PicassoImageCallback(Context context, ImageView target) {
            this.target = target;
            this.context = context;
        }

        @Override
        public void onError() {
            displayDefaultImage();
        }

        private void displayDefaultImage() {
            Picasso.with(context).load(R.drawable.not_available).into(target);
        }

        // There is nothing to do here.
        // The picasso library already loads the image into the view for us.
        @Override
        public void onSuccess() {
        }

    }
}
