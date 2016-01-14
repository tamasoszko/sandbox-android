package no.apps.dnproto.asynch;

/**
 * Created by oszi on 13/01/16.
 */
public interface Callback<Result> {

    void onSuccess(Result obj);
    void onError(Exception error);

}
