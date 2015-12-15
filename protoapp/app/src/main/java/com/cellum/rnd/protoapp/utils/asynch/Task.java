package com.cellum.rnd.protoapp.utils.asynch;


import android.os.Handler;
import android.os.Looper;

/**
 * Created by oszkotamas on 26/09/15.
 */
public abstract class Task<Result, Param> implements Runnable {

    public interface OnCompletion<Result> {

        void onSuccess(Result result);
        void onError(Exception exception);
    }

    private final Param param;
    private final Handler mainHandler;

    public Task(Param param) {
        this.param = param;
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public abstract Result doInBackground(Param param) throws Exception;
    public abstract void onSuccess(Result result);
    public abstract void onError(Exception exception);

    @Override
    public void run() {
        try {
            final Result result = doInBackground(param);
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onSuccess(result);
                }
            });
        } catch (final Exception e) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onError(e);
                }
            });
        }
    }

}
