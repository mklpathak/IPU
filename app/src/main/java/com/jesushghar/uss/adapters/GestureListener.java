package com.jesushghar.uss.adapters;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by PAT on 5/14/2016.
 */
public class GestureListener implements GestureDetector.OnDoubleTapListener {
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
