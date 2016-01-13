package no.apps.dnproto.asynch;

/**
 * Created by oszi on 08/01/16.
 */
public interface Callback<T> {
    void onSuccess(T obj);
    void onError(Exception error);
    void onCancel();
}
