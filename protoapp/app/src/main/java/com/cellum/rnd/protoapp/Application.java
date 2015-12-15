package com.cellum.rnd.protoapp;

import android.content.Context;

import com.google.inject.Binder;
import com.google.inject.Provider;
import com.google.inject.name.Names;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import roboguice.RoboGuice;


/**
 * Created by oszkotamas on 19/09/15.
 */
public class Application extends android.app.Application {

    private class ExecutorServiceProvider implements Provider<ExecutorService> {

        private final ExecutorService executorService = Executors.newCachedThreadPool();
        @Override
        public ExecutorService get() {
            return executorService;
        }
    }

    private class Module implements com.google.inject.Module {

        private final Context context;

        public Module(Application application) {
            this.context = application;
        }

        @Override
        public void configure(Binder binder) {

            binder.bind(ExecutorService.class).toProvider(new ExecutorServiceProvider());
            binder.bind(SessionManager.class).to(SessionManagerImpl.class);
            binder.bind(Navigation.class).toInstance(new NavigationImpl());
            binder.bind(BitmapFactory.class).annotatedWith(Names.named("Resource")).to(ResourceBitmapFactory.class);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RoboGuice.overrideApplicationInjector(this, RoboGuice.newDefaultRoboModule(this), new Module(this));
    }

}
