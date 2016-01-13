package no.apps.dnproto;

import dagger.ObjectGraph;
import no.apps.dnproto.dagger.AndroidModule;
import no.apps.dnproto.dagger.ApplicationModules;

/**
 * Created by oszi on 07/01/16.
 */
public class Application extends android.app.Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        objectGraph = ObjectGraph.create(modules());
    }

    private Object[] modules() {
        return new Object[]{
                new AndroidModule(this),
                new ApplicationModules(this)
        };
    }

    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }
}
