package com.ronyhe.movies.app.network;


import android.os.AsyncTask;

/** Static utility abstractions for async operations */
public final class AsyncRequests {

    private AsyncRequests() {}  // Non instantiable

    public interface AsyncRequest<ResultType> {
        void send(AsyncRequestCallback<ResultType> callback);
    }

    public interface AsyncRequestCallback<ResultType> {
        void onResponseReceived(AsyncResponse<ResultType> response);
    }

    /**
     * Contract:
     *
     * If wasRequestSuccessful() returns true:
     *  getThrownException() is undefined.
     *
     * If wasRequestSuccessful returns false:
     *  getThrownException() returns an exception that describes the failure.
     *  getResult is undefined.
     */
    public interface AsyncResponse<ResultType> {
        boolean wasRequestSuccessful();
        ResultType getResult();
        Exception getThrownException();
    }

    public static <ResultType> AsyncResponse<ResultType> newFailedResponse(final Exception e) {
        return new AsyncResponse<ResultType>() {
            @Override
            public boolean wasRequestSuccessful() {
                return false;
            }

            @Override
            public ResultType getResult() {
                return null;
            }

            @Override
            public Exception getThrownException() {
                return e;
            }
        };
    }

    public static <ResultType> AsyncResponse<ResultType> newSuccessfulResponse(final ResultType result) {
        return new AsyncResponse<ResultType>() {
            @Override
            public boolean wasRequestSuccessful() {
                return true;
            }

            @Override
            public ResultType getResult() {
                return result;
            }

            @Override
            public Exception getThrownException() {
                return null;
            }
        };
    }

}

