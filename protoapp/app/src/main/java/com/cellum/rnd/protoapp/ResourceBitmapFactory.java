package com.cellum.rnd.protoapp;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Created by oszkotamas on 23/09/15.
 */
public class ResourceBitmapFactory implements BitmapFactory {

    private Context context;

    @Inject
    public ResourceBitmapFactory(Context context) {
        this.context = context;
    }

    @Override
    public Bitmap bitmap(String name) {
        int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return android.graphics.BitmapFactory.decodeResource(context.getResources(), resId);
    }

}
