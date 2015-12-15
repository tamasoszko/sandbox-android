package com.cellum.rnd.protoapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oszkotamas on 19/09/15.
 */
public abstract class Event<T> {

    protected final List<Action> actions = new ArrayList<>();

    abstract boolean handle(T event);

    public Event<T> on(Action action) {
        actions.add(action);
        return this;
    }
}
