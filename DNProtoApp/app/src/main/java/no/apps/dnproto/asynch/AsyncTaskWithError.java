package no.apps.dnproto.asynch;

import android.os.AsyncTask;

import java.util.concurrent.ExecutorService;

/**
 * Created by oszi on 13/01/16.
 */
public abstract class AsyncTaskWithError<Params, Progress, Result> {

    private final  ExecutorService bgExecutor = ExecutorServices.bgExecutor();

    private final AsyncTask<Params, Progress, AsyncResult<Result>> asyncTask =
        new AsyncTask<Params, Progress, AsyncResult<Result>>() {

            @Override
            protected void onPreExecute() {
                AsyncTaskWithError.this.onPreExecute();
            }

            @Override
            protected AsyncResult<Result> doInBackground(Params... params) {
                try {
                    Result result = AsyncTaskWithError.this.doInBackground(params);
                    return new AsyncResult<Result>(result);
                } catch (Exception e) {
                    return new AsyncResult<Result>(e);
                }
            }

            @Override
            protected void onProgressUpdate(Progress... values) {
                AsyncTaskWithError.this.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(AsyncResult<Result> resultHolder) {
                if(resultHolder.isSuccessful()) {
                    AsyncTaskWithError.this.onSuccess(resultHolder.getValue());
                } else {
                    AsyncTaskWithError.this.onError(resultHolder.getError());
                }
            }
        };

    public abstract Result doInBackground(Params ... params) throws Exception;

    protected void onPreExecute() {
    }

    @SuppressWarnings({"UnusedDeclaration"})
    protected void onProgressUpdate(Progress... values) {
    }

    protected abstract void onSuccess(Result asyncResult);

    @SuppressWarnings({"UnusedDeclaration"})
    protected abstract void onError(Exception exception);

    public final AsyncTaskWithError<Params, Progress, Result> execute(Params... params) {
        asyncTask.executeOnExecutor(bgExecutor, params);
        return this;
    }
}
