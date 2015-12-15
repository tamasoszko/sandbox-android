package com.cellum.rnd.protoapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.inject.Inject;

/**
 * Created by oszkotamas on 19/09/15.
 */
@SuppressWarnings("ResourceType")
public class NavigationImpl implements Navigation {

    @Inject
    private SessionManager sessionManager;
    private FragmentManager fragmentManager;

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public boolean back() {
        Log.d(getClass().getName(), String.format("backstack entry count=%d", fragmentManager.getBackStackEntryCount()));
        if(fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
            return true;
        }
        return false;
    }

    @Override
    public void backToRoot() {
        int rootFragment = fragmentManager.getBackStackEntryAt(1).getId();
        fragmentManager.popBackStack(rootFragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void push(String screenName) {
        Screen screen = screenForName(screenName);
        if(screen != null) {
            final FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out, R.animator.fade_in, R.animator.fade_out)
                    .replace(R.id.frgament_placeholder, new PlaceholderFragment(), screen.getName())
                    .addToBackStack("")
                    .commit();
        }
    }

    @Override
    public void replace(String screenName) {
        Screen screen = screenForName(screenName);
        if(screen != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frgament_placeholder, new PlaceholderFragment(), screen.getName())
                    .commit();
        }
    }

    Screen screenForName(String screenName) {
        return sessionManager.getSession().getScreenFlow().forName(screenName);
    }

}
