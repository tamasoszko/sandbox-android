package com.cellum.rnd.protoapp;

import android.graphics.Point;
import android.graphics.Rect;

import com.google.inject.Inject;

import roboguice.inject.RoboInjector;

/**
 * Created by oszkotamas on 19/09/15.
 */
public class TapEvent extends Event<Point> {

    private final Rect tapRect;

    public TapEvent(RoboInjector injector, Rect tapRect) {
        injector.injectMembers(this);
        this.tapRect = tapRect;
    }

    public Rect getTapRect() {
        return new Rect(tapRect);
    }

    @Override
    public boolean handle(Point point) {
        if(tapRect.contains(point.x, point.y)) {
            for(Action action : actions) {
                action.execute();
            }
            return true;
        }
        return false;
    }
}
