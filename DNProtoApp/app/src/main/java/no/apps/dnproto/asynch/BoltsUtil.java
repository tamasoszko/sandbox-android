package no.apps.dnproto.asynch;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by oszi on 08/01/16.
 */
public class BoltsUtil {

    public static <T> void completeOnMainThread(Task<T> task, final Callback<T> callback) {
        if(callback == null) {
            throw new IllegalArgumentException("Argument 'callback' is null");
        }
        if(task == null) {
            throw new RuntimeException("Argument 'task' is null");
        }
        task.continueWith(new Continuation<T, Void>() {
            @Override
            public Void then(Task<T> task) throws Exception {
                if(!task.isCompleted()) {
                    throw new IllegalStateException("Task must be completed");
                }
                if(task.isCancelled()) {
                } else if(task.isFaulted()) {
                    callback.onError(task.getError());
                } else {
                    callback.onSuccess(task.getResult());
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }
}
