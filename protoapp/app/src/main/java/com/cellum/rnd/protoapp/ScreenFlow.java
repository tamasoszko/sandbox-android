package com.cellum.rnd.protoapp;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oszkotamas on 19/09/15.
 */
public class ScreenFlow {


    private final Map<String, Screen> screens = new HashMap<String, Screen>();

    public Screen forName(String name) {
        return screens.get(name);
    }

    public void reset() {
        screens.clear();
    }

    public ScreenFlow add(Screen screen) {
        String name = screen.getName();
        if(!TextUtils.isEmpty(name)) {
            screens.put(name, screen);
        }
        return this;
    }

}
