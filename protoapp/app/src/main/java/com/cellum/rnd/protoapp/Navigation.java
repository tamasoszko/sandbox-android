package com.cellum.rnd.protoapp;

/**
 * Created by oszkotamas on 19/09/15.
 */
public interface Navigation {

    boolean back();
    void backToRoot();
    void push(String screenName);
    void replace(String screenName);
}
