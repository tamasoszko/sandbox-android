package com.cellum.rnd.protoapp;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by oszkotamas on 19/09/15.
 */
public class Screen {

    public static class Builder {
        private String name;
        private List<Event<?>> events = new ArrayList<Event<?>>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder event(Event<?> event) {
            if(event != null) {
                this.events.add(event);
            }
            return this;
        }

        public Screen build() {
            return new Screen(this);
        }
    }

    private final String name;
    private final List<Event<?>> events;

    private Screen(Builder builder) {
        name = builder.name;
        events = builder.events;
    }

    public String getName() {
        return name;
    }

    public boolean onTap(Point point) {
        Log.d(getClass().getName(), String.format("Tap: x=%d, y=%d", point.x, point.y));
        for(Event event : events) {
            if(event.handle(point)) {
                return true;
            }
        }
        return false;
    }

    public List<Event<?>> getEvents() {
        return Collections.unmodifiableList(events);
    }
}
