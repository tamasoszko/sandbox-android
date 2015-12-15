package com.cellum.rnd.protoapp;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.inject.Singleton;

import java.net.URI;

/**
 * Created by oszkotamas on 26/09/15.
 */

public interface SessionManager {

    public interface OnLoadSessionListener {
        void onSuccess();
        void onError(Throwable exception);
    }

    void createSession(Uri uri, OnLoadSessionListener listener);
    Session getSession();
}
