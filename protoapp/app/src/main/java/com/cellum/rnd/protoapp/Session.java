package com.cellum.rnd.protoapp;

import android.graphics.Bitmap;

/**
 * Created by oszkotamas on 26/09/15.
 */
public interface Session {
    ScreenFlow getScreenFlow();
    Bitmap getBitmap(String bitmapName);

}
