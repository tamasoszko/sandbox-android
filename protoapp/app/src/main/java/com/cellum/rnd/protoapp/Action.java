package com.cellum.rnd.protoapp;

import com.google.inject.Inject;

import roboguice.inject.RoboInjector;

/**
 * Created by oszkotamas on 25/09/15.
 */
public abstract class Action {

    Action(RoboInjector injector) {
        injector.injectMembers(this);
    }

    public abstract void execute();

    public static class Back extends Action {

        @Inject
        Navigation navigation;

        public Back(RoboInjector injector) {
            super(injector);
        }

        @Override
        public void execute() {
            navigation.back();
        }
    }

    public static class Main extends Action {

        @Inject
        Navigation navigation;

        public Main(RoboInjector injector) {
            super(injector);
        }

        @Override
        public void execute() {
            navigation.backToRoot();
        }
    }

    public static class ReplaceScreen extends Action {

        @Inject
        Navigation navigation;

        private final String targetScreenName;

        public ReplaceScreen(RoboInjector injector, String targetScreenName) {
            super(injector);
            this.targetScreenName = targetScreenName;
        }

        @Override
        public void execute() {
            navigation.push(targetScreenName);
        }
    }

    public static class PushScreen extends Action {

        @Inject
        Navigation navigation;

        private final String targetScreenName;

        public PushScreen(RoboInjector injector, String targetScreenName) {
            super(injector);
            this.targetScreenName = targetScreenName;
        }

        @Override
        public void execute() {
            navigation.push(targetScreenName);
        }
    }
}
