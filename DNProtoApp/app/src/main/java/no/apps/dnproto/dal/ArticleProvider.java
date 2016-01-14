package no.apps.dnproto.dal;

import java.util.List;

import no.apps.dnproto.dal.Article;
import rx.Observable;

/**
 * Created by oszi on 12/01/16.
 */
public interface ArticleProvider {

    List<ArticleIndex> getArticleIndexes() throws Exception;
    Article getArticle(String id) throws Exception;

}
