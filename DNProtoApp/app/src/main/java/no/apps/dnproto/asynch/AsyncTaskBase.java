package no.apps.dnproto.asynch;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

/**
 * Created by oszi on 13/01/16.
 */
public abstract class AsyncTaskBase<Params, Progress, Result> {

    private final  ExecutorService bgExecutor = ExecutorServices.bgExecutor();

    private final AsyncTask<Params, Progress, AsyncResult<Result>> asyncTask =
        new AsyncTask<Params, Progress, AsyncResult<Result>>() {

            @Override
            protected void onPreExecute() {
                AsyncTaskBase.this.onPreExecute();
            }

            @Override
            protected AsyncResult<Result> doInBackground(Params... params) {
                try {
                    Result result = AsyncTaskBase.this.doInBackground(params);
                    return new AsyncResult<Result>(result);
                } catch (Exception e) {
                    return new AsyncResult<Result>(e);
                }
            }

            @Override
            protected void onProgressUpdate(Progress... values) {
                AsyncTaskBase.this.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(AsyncResult<Result> resultHolder) {
                AsyncTaskBase.this.onPostExecute(resultHolder);
            }
        };

    public abstract Result doInBackground(Params ... params) throws Exception;

    protected void onPreExecute() {
    }

    @SuppressWarnings({"UnusedDeclaration"})
    protected void onProgressUpdate(Progress... values) {
    }

    @SuppressWarnings({"UnusedDeclaration"})
    protected void onPostExecute(AsyncResult<Result> asyncResult) {
    }

    public final AsyncTaskBase<Params, Progress, Result> execute(Params... params) {
        asyncTask.executeOnExecutor(bgExecutor, params);
        return this;
    }
}
