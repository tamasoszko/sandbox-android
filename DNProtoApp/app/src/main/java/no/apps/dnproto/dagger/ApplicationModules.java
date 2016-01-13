package no.apps.dnproto.dagger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.apps.dnproto.Application;
import no.apps.dnproto.MainActivity;
import no.apps.dnproto.MainFragmentAdapter;
import no.apps.dnproto.asynch.RetryHandler;
import no.apps.dnproto.dal.ArticleProvider;
import no.apps.dnproto.proto.AsynchTest;
import no.apps.dnproto.proto.BoltsTest;
import no.apps.dnproto.dal.ParseArticleProvider;
import no.apps.dnproto.proto.RetrofitTest;
import no.apps.dnproto.proto.RxJavaTest;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

@Module(includes = AndroidModule.class,
    injects = {
        MainActivity.class
        , MainFragmentAdapter.class
        , RxJavaTest.class
        , BoltsTest.class
        , RetrofitTest.class
        , RetryHandler.class
        , OkHttpClient.class
        , Gson.class
        , Retrofit.class
        , ParseArticleProvider.class
    },
    library = true
)
public class ApplicationModules {

    private final Application application;

    public ApplicationModules(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @Named("RxJavaTest")
    AsynchTest provideAsynchTest() {
        return application.getObjectGraph().inject(new RxJavaTest());
    }

    @Provides
    @Singleton
    @Named("BoltsTest")
    AsynchTest provideBoltsTest() {
        return application.getObjectGraph().inject(new BoltsTest());
    }

    @Provides
    @Singleton
    @Named("RetrofitTest")
    AsynchTest provideRetrofitTest() {
        return application.getObjectGraph().inject(new RetrofitTest());
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                        .header("X-Parse-Application-Id", "WpVMqIST15IL5ATn2hKyEnDWCAThMXwVvcWVE2FW")
                        .header("X-Parse-REST-API-Key", "0CZyp5yObrrCd5Ax6YlGDaYt5Cq0o7vj1av4qqdg")
                        .build();
                    return chain.proceed(request);
                }
            })
            .build();
        return okHttpClient;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl("https://api.parse.com/1/")
            .build();
    }

    @Provides
    @Named("Default")
    RetryHandler provideRetryHandler() {
        return application.getObjectGraph().inject(RetryHandler.builder()
            .delay(1000)
            .retry(3)
            .build());
    }

    @Provides
    @Named("Parse")
    ArticleProvider provideArticleProvider() {
        return application.getObjectGraph().inject(new ParseArticleProvider());
    }
}
