package com.example.ericjohannsen.finalprojectejeh;

import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

/**
 * Created by joha1eri on 3/24/15.
 */
public class EventHandler implements OnGestureListener{
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float xVel, float yVel) {
        return true;
    }

    public void onShowPress(MotionEvent event) { }

    public boolean onDown(MotionEvent event) {
        return true;
    }

    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }

    public void onLongPress(MotionEvent event) { }

    public boolean onFling(MotionEvent event1, MotionEvent event2, float xVel, float yVel) {
        Log.d("XVel:", String.valueOf(xVel));
        Log.d("YVel:", String.valueOf(yVel));
        return true;
    }
}
