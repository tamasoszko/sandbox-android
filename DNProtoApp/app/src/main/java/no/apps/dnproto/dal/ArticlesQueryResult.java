package no.apps.dnproto.dal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import no.apps.dnproto.dal.Article;

/**
 * Created by oszi on 13/01/16.
 */
public class ArticlesQueryResult {
    @SerializedName("results")
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }
}
