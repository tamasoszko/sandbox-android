package no.apps.dnproto.asynch;

/**
 * Created by oszi on 14/01/16.
 */
public interface ProgressObserver<T> {
    void onProgress(T obj);
}
