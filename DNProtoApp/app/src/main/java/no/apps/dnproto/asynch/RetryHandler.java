package no.apps.dnproto.asynch;

import android.content.Context;

import java.lang.annotation.Retention;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Qualifier;

import no.apps.dnproto.Article;
import no.apps.dnproto.dagger.ApplicationContext;
import rx.Observable;
import rx.functions.Func1;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Created by oszi on 11/01/16.
 */
public class RetryHandler implements Func1<Observable<? extends Throwable>, Observable<?>>{

    private int retry;
    private int delay;

    private RetryHandler(Builder builder) {
        retry = builder.retry;
        delay = builder.delay;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> observable) {
        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                if(retry > 0) {
                    retry--;
                    return Observable.timer(delay, TimeUnit.MILLISECONDS);
                }
                return Observable.error(throwable);
            }
        });
    }

    public static class Builder {
        private int retry = 1;
        private int delay = 0;

        private Builder() {}

        public RetryHandler build() {
            return new RetryHandler(this);
        }

        public Builder retry(int retry) {
            this.retry  = retry;
            return this;
        }
        public Builder delay(int delay) {
            this.delay = delay;
            return this;
        }
    }
}
