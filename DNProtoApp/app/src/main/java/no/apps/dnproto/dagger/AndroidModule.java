package no.apps.dnproto.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.apps.dnproto.Application;

@Module(
    library = true
)
public class AndroidModule {
   private final Application application;

    public AndroidModule(Application application) {
        this.application = application;
    }


    @Provides
    @Singleton
    @ApplicationContext
    Context provideApplicationContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }
}
