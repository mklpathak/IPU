package com.jesushghar.uss.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by PAT on 5/14/2016.
 */
public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    RecyclerView.OnItemTouchListener listener;
    GestureDetector gestureDetector;

    public RecyclerTouchListener(Context context, RecyclerView.OnItemTouchListener onItemTouchListener)  {
        listener = onItemTouchListener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
