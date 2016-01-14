package no.apps.dnproto.dal;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import no.apps.dnproto.proto.ParseService;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by oszi on 12/01/16.
 */
public class ParseRxJavaArticleProvider implements ArticleProvider {

    @Inject
    Retrofit retrofit;

//    @Override
//    public Observable<ArticleIndex> getArticleIndexes() {
//        return downloadArticleIds()
//            .flatMap(new Func1<Response<ArticlesQueryResult>, Observable<Article>>() {
//                @Override
//                public Observable<Article> call(Response<ArticlesQueryResult> resultsResponse) {
//                    if (resultsResponse.body() == null) {
//                        return Observable.empty();
//                    }
//                    List<Article> articles = new ArrayList<>(resultsResponse.body().getArticles());
//                    Collections.sort(articles, new Comparator<Article>() {
//                        @Override
//                        public int compare(Article lhs, Article rhs) {
//                            return rhs.getUpdatedAt().compareTo(lhs.getUpdatedAt());
//                        }
//                    });
//                    return Observable.from(articles);
//                }
//            })
//            .flatMap(new Func1<Article, Observable<ArticleIndex>>() {
//                @Override
//                public Observable<ArticleIndex> call(Article article) {
//                    ArticleIndex ai = new ArticleIndex.Builder()
//                        .id(article.getId())
//                        .updatedAt(article.getUpdatedAt())
//                        .build();
//                    return Observable.just(ai);
//                }
//            })
//            .filter(new Func1<ArticleIndex, Boolean>() {
//                @Override
//                public Boolean call(ArticleIndex articleIndex) {
//                    return !TextUtils.isEmpty(articleIndex.getId());
//                }
//            });
//    }
//
//    @Override
//    public Observable<Article> getArticle(final String id) {
//        return downloadArticle(id)
//            .flatMap(new Func1<Response<Article>, Observable<Article>>() {
//                @Override
//                public Observable<Article> call(Response<Article> articlesQueryResultResponse) {
//                    if (articlesQueryResultResponse == null
//                        || articlesQueryResultResponse.body() == null) {
//                        return Observable.error(new Exception("Article not found, id=" + id));
//                    }
//                    return Observable.just(articlesQueryResultResponse.body());
//                }
//            });
//    }

    private Observable<Response<ArticlesQueryResult>> downloadArticleIds() {
        return Observable.defer(new Func0<Observable<Response<ArticlesQueryResult>>>() {
            @Override
            public Observable<Response<ArticlesQueryResult>> call() {
                ParseService ps = retrofit.create(ParseService.class);
                try {
                    return Observable.just(ps.queryObjects("Article").execute());
                } catch (IOException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    private Observable<Response<Article>> downloadArticle(final String id) {
        return Observable.defer(new Func0<Observable<Response<Article>>>() {
            @Override
            public Observable<Response<Article>> call() {
                ParseService ps = retrofit.create(ParseService.class);
                try {
                    return Observable.just(ps.getObject("Article", id).execute());
                } catch (IOException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @Override
    public List<ArticleIndex> getArticleIndexes() {
        return null;
    }

    @Override
    public Article getArticle(String id) {
        return null;
    }
}
