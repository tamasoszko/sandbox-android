package no.apps.dnproto.dal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import no.apps.dnproto.proto.ParseService;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by oszi on 13/01/16.
 */
public class SimpleParseArticleProvider implements ArticleProvider {

    private static final String OBJECT_NAME = "Article";

    @Inject
    Retrofit retrofit;

    @Override
    public List<ArticleIndex> getArticleIndexes() throws Exception {
        ParseService parseService = retrofit.create(ParseService.class);
        Response<ArticlesQueryResult> queryResultResponse = parseService.queryObjects(OBJECT_NAME).execute();
        if(queryResultResponse == null
            || queryResultResponse.body() == null
            || queryResultResponse.body().getArticles() == null) {
            return Collections.emptyList();
        }
        List<Article> articles = queryResultResponse.body().getArticles();
        List<ArticleIndex> articleIndexes = new ArrayList<>();
        for (Article article : articles) {
            ArticleIndex ai = new ArticleIndex.Builder()
                .id(article.getId())
                .updatedAt(article.getUpdatedAt())
                .build();
            articleIndexes.add(ai);
        }
        Collections.sort(articleIndexes, new Comparator<ArticleIndex>() {
            @Override
            public int compare(ArticleIndex lhs, ArticleIndex rhs) {
                return lhs.getOrder() - rhs.getOrder();
            }
        });
        return articleIndexes;
    }

    @Override
    public Article getArticle(String id) throws Exception {

        ParseService parseService = retrofit.create(ParseService.class);
        Response<Article> response = parseService.getObject(OBJECT_NAME, id).execute();
        if(response == null || response.body() == null) {
            throw new Exception("Article not found, id=" + id);
        }
        return response.body();
    }
}
