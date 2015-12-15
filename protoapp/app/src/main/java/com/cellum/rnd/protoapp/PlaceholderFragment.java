package com.cellum.rnd.protoapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.inject.Inject;

import java.util.List;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends RoboFragment {

    @InjectView(R.id.placeholder)
    private ImageView placeHolderImg;

    @InjectView(R.id.overlay)
    private OverlayView overlayView;

    @Inject
    private SessionManager sessionManager;

    private float density;
    private float scale;
    private int viewWidth;
    private int displayWidth;

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(getClass().getName(), "tag=" + getTag());
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        density = displayMetrics.density;
        displayWidth = displayMetrics.widthPixels;
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Screen screen = sessionManager.getSession().getScreenFlow().forName(getTag());
        Bitmap b = sessionManager.getSession().getBitmap(getTag());
//        int resId = getActivity().getResources().getIdentifier(getTag(), "drawable", getActivity().getPackageName());
        placeHolderImg.setImageBitmap(b);
        final ScrollHandler scrollHandler = new ScrollHandler(0);

        android.graphics.BitmapFactory.Options dimensions = new android.graphics.BitmapFactory.Options();
        dimensions.inJustDecodeBounds = true;

        Drawable drawable = placeHolderImg.getDrawable();
        viewWidth = drawable.getIntrinsicWidth();
        scale = (float)viewWidth/(float)displayWidth;


        placeHolderImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (screen != null) {
                    if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        scrollHandler.init((int) motionEvent.getY());
                        if (screen.onTap(new Point(fromScreen(toDp((int) motionEvent.getX()))
                                , fromScreen(toDp((int) motionEvent.getY()))))) {
                            vibrate();
                        } else {
                            overlayView.setVisibility(View.VISIBLE);
                        }
                    } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
                        overlayView.setVisibility(View.INVISIBLE);
                    } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE) {
                        int scrollBy = scrollHandler.movedTo((int) motionEvent.getY());
                        overlayView.scrollBy(0, fromScreen(toDp(scrollBy)));
                        placeHolderImg.scrollBy(0, fromScreen(toDp(scrollBy)));
                    }
                }
                return false;
            }
        });
        placeHolderImg.setLongClickable(true);

        overlayView.clearRects();
        List<Event<?>> events = screen.getEvents();
        for(Event<?> event : events) {
            if(event instanceof TapEvent) {
                TapEvent tapEvent = (TapEvent) event;
                Rect tapRect = tapEvent.getTapRect();
                overlayView.addRect(new Rect(toScreen(fromDp(tapRect.left))
                        , toScreen(fromDp(tapRect.top))
                        , toScreen(fromDp(tapRect.right))
                        , toScreen(fromDp(tapRect.bottom))));
            }
        }
        overlayView.setVisibility(View.INVISIBLE);
    }

    private int toScreen(int point) {
        return (int)(point / scale);
    }

    private int fromScreen(int point) {
        return (int)(point * scale);
    }

    private int toDp(int point) {
        return (int)(point / density );
    }

    private int fromDp(int point) {
        return (int)(point * density );
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(50);
    }

    private static class ScrollHandler {

        private final int maxDiff;
        private int diff;
        private int lastPos;

        public ScrollHandler(int maxDiff) {
            this.maxDiff = maxDiff;
        }

        public void init(int pos) {
            lastPos = pos;
        }

        public int movedTo(int pos) {
            int currDiff = lastPos - pos;
            currDiff = Math.max(currDiff, currDiff - (diff + currDiff));
            currDiff = Math.min(currDiff, currDiff - (diff + currDiff- maxDiff));
            diff += currDiff;
            lastPos = pos;
            return currDiff;
        }
    }
}
