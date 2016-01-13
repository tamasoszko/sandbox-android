package no.apps.dnproto.dal;

import no.apps.dnproto.dal.Article;
import rx.Observable;

/**
 * Created by oszi on 12/01/16.
 */
public interface ArticleProvider {

    Observable<ArticleIndex> getArticleIndexes();
    Observable<Article> getArticle(String id);

}
