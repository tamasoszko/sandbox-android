package no.apps.dnproto.proto;

import no.apps.dnproto.dal.Article;
import no.apps.dnproto.dal.ArticlesQueryResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by oszi on 11/01/16.
 */
public interface ParseService {

    @GET("classes/{objectName}")
    Call<ArticlesQueryResult> queryObjects(@Path("objectName") String objectName);

    @GET("classes/{objectName}/{objectId}")
    Call<Article> getObject(@Path("objectName") String objectName, @Path("objectId") String objectId);

}
