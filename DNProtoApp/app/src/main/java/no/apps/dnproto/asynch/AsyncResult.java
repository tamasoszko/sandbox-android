package no.apps.dnproto.asynch;

/**
 * Created by oszi on 13/01/16.
 */
public final class  AsyncResult<T> {

    private final T value;
    private final Exception error;

    public AsyncResult(T value) {
        this.value = value;
        this.error = null;
    }

    public AsyncResult(Exception error) {
        this.value = null;
        this.error = error;
    }

    public T getValue() {
        return value;
    }

    public Exception getError() {
        return error;
    }

    public boolean isSuccessful() {
        return error == null;
    }
}
