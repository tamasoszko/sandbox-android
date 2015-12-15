package com.cellum.rnd.protoapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oszkotamas on 21/09/15.
 */
public class OverlayView extends View {

    private List<Rect> rects = new ArrayList<>();
    private Paint paint;

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(2);
        paint.setAlpha(192);

    }

    public void clearRects() {
        rects.clear();
    }

    public void addRect(Rect rect) {
        rects.add(rect);
    }

    @Override
    protected void onDraw (Canvas canvas) {
        for(Rect rect : rects) {
            canvas.drawRect(rect, paint);
        }
    }

}
