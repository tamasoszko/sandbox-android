package no.apps.dnproto.asynch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by oszi on 13/01/16.
 */
public class ExecutorServices {
    private static final ExecutorService bgExecutor = Executors.newCachedThreadPool();

    public static ExecutorService bgExecutor() {
        return bgExecutor;
    }
}
