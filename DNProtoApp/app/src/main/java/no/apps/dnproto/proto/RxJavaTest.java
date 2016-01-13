package no.apps.dnproto.proto;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.ObjectGraph;
import no.apps.dnproto.Application;
import no.apps.dnproto.asynch.RetryHandler;
import no.apps.dnproto.dagger.ApplicationContext;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by oszi on 11/01/16.
 */
public class RxJavaTest implements AsynchTest {

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    OkHttpClient okHttpClient;

    @Inject
    @Named("Default")
    RetryHandler retryHandler;

    @Override
    public void preform() {
        downloadData()
        .retryWhen(retryHandler)
        .flatMap(new Func1<Response, Observable<String>>() {
            @Override
            public Observable<String> call(Response response) {
                try {
                    return Observable.just(response.body().string());
                } catch (IOException e) {
                    return Observable.error(e);
                }
            }
        })
        .filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String stringObservable) {
                return stringObservable != null;
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(AsynchTest.LOG_TAG, s);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(AsynchTest.LOG_TAG, throwable.getLocalizedMessage(), throwable);
            }
        })
        ;
    }

    private Observable<Response> downloadData() {

        return Observable.defer(new Func0<Observable<Response>>() {
            @Override
            public Observable<Response> call() {
                Request request = new Request.Builder()
                    .addHeader("X-Parse-Application-Id", "zNMGmfiYzq7W4eyD7mpSfixKIvxaZRPjCS38Qh6z")
                    .addHeader("X-Parse-REST-API-Key", "BiPX6d3kDzfinO9Y8kHvNo1rnCPl8PoJW2zUXHYk")
                    .url("https://api.parse.com/1/classes/DHTMeter")
                    .build();
                try {
                    Log.d(AsynchTest.LOG_TAG, "Sending request to" + request.url());
                    Response response = okHttpClient.newCall(request).execute();
                    return Observable.just(response);
                } catch (IOException e) {
                    return Observable.error(e);
                }
            }
        });
    }
}
