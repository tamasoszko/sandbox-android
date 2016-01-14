package no.apps.dnproto.asynch;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by oszi on 13/01/16.
 */

public abstract class BackgroundTask<Result> implements Callable<Result> {

    private final Callback<Result> callback;
    private final ExecutorService executorService;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());


    public BackgroundTask(Callback<Result> callback) {
        this.callback = callback;
        this.executorService = Executors.newCachedThreadPool();
    }

    public BackgroundTask(Callback<Result> callback, ExecutorService executorService) {
        this.callback = callback;
        this.executorService = executorService;
    }

    protected void submit() {

        Future<?> future = executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final Result result = call();
                    onMainThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(result);
                        }
                    });
                } catch (final Exception e) {
                    onMainThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }
            }
        });
    }

    protected void onMainThread(Runnable runnable) {
        mainHandler.post(runnable);
    }

}
