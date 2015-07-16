package com.ronyhe.movies.app.ui;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import com.ronyhe.movies.app.R;

/**
 * Utility for presenting a dialog to the use in case of a failed network request.
 *
 * The dialog allows:
 * Retrying, supplied by client code.
 * Closing the app: implemented in this class.
 */
final class FailedNetworkRequestDialog {

    /** @param retry is the Runnable to be executed if the user wishes to retry the failed network request. */
    public static AlertDialog getInstance(Context context, Runnable retry) {
        if (context == null) {
            throw new NullPointerException("context cannot be null");
        }
        if (retry == null) {
            throw new NullPointerException("retry cannot be null");
        }

        return new FailedNetworkRequestDialog(context, retry).getDialog();
    }

    private final Context context;
    private final Runnable retry;

    private final String title;
    private final String message;
    private final String retryButtonText;
    private final String closeAppButtonText;

    private FailedNetworkRequestDialog(Context context, Runnable retry) {
        this.context = context;
        this.retry = retry;

        title = context.getString(R.string.network_fail_dialog_title);
        message = context.getString(R.string.network_fail_dialog_message);
        retryButtonText = context.getString(R.string.network_fail_dialog_retry_button_text);
        closeAppButtonText = context.getString(R.string.network_fail_dialog_close_app_button_text);
    }

    private AlertDialog getDialog() {
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle(title).setMessage(message)
                .setPositiveButton(retryButtonText, retryListener)
                .setNegativeButton(closeAppButtonText, closeAppListener);

        return builder.create();
    }

    private void closeApplication() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private final OnClickListener closeAppListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            closeApplication();
        }
    };

    // Simply wraps the retry Runnable in an OnClickListener, which is the required type for AlertDialog.
    private final OnClickListener retryListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            retry.run();
        }
    };

}
