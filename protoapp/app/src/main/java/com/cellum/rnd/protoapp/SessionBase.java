package com.cellum.rnd.protoapp;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.inject.Inject;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;

/**
 * Created by oszkotamas on 26/09/15.
 */
public abstract class SessionBase implements Session {

    protected final Context context;
    protected Session session;
    protected ScreenFlow screenFlow;
    protected RoboInjector injector;

    public SessionBase(Context context) {
        this.context = context;
        injector = RoboGuice.getInjector(context);
        injector.injectMembers(this);
    }

    protected abstract void setup() throws Exception;
    protected abstract BitmapFactory getBitmapFactory();

    @Override
    public ScreenFlow getScreenFlow() {
        return screenFlow;
    }

    @Override
    public Bitmap getBitmap(String bitmapName) {
        return getBitmapFactory().bitmap(bitmapName);
    }
}
