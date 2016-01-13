package no.apps.dnproto.proto;

import android.util.Log;

import java.io.IOException;

import javax.inject.Inject;

import no.apps.dnproto.dal.Article;
import no.apps.dnproto.dal.ArticlesQueryResult;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by oszi on 11/01/16.
 */
public class RetrofitTest implements AsynchTest {

    @Inject
    Retrofit retrofit;

    @Override
    public void preform() {

//        queryData()
//        .flatMap(new Func1<Response<ArticlesQueryResult>, Observable<Article>>() {
//            @Override
//            public Observable<Article> call(Response<ArticlesQueryResult> resultsResponse) {
//                if (resultsResponse.body() == null) {
//                    return Observable.empty();
//                }
//                return Observable.from(resultsResponse.body().getArticles());
//            }
//        })
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(
//            new Action1<Article>() {
//               @Override
//               public void call(Article result) {
//                   Log.d(LOG_TAG, result.toString());
//               }
//            },
//            new Action1<Throwable>() {
//                @Override
//                public void call(Throwable throwable) {
//                    Log.e(LOG_TAG, throwable.getLocalizedMessage(), throwable);
//                }
//            },
//            new Action0() {
//                @Override
//                public void call() {
//                    Log.d(LOG_TAG, "Done");
//                }
//            });
    }

//    private Observable<Response<ArticlesQueryResult>> queryData() {
//        return Observable.defer(new Func0<Observable<Response<ArticlesQueryResult>>>() {
//            @Override
//            public Observable<Response<ArticlesQueryResult>> call() {
//                ParseService ps = retrofit.create(ParseService.class);
//                try {
//                    return Observable.just(ps.queryObject("Article").execute());
//                } catch (IOException e) {
//                    return Observable.error(e);
//                }
//            }
//        });
//    }
}
