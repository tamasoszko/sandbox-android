package no.apps.dnproto.asynch;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by oszi on 14/01/16.
 */
public class Async {

    private static final ExecutorService bgExecutor = Executors.newCachedThreadPool();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void runOnMainThread(Runnable runnable) {
        mainHandler.post(runnable);
    }

    public static void runInBackground(Runnable runnable) {
        runWithExecutor(runnable, bgExecutor);
    }

    public static void runWithExecutor(Runnable runnable, ExecutorService executorService) {
        executorService.submit(runnable);
    }

}
