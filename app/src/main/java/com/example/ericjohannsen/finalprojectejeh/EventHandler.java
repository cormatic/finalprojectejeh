package com.example.ericjohannsen.finalprojectejeh;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by joha1eri on 3/24/15.
 */
public class EventHandler extends GestureDetector.SimpleOnGestureListener{

    public EventHandler(Context context){

    }

    public boolean onTouchEvent(MotionEvent event) {
        return onTouchEvent(event);
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float xVel, float yVel) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d("onShowPress", "ShowPress");
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d("onDown", "Down");
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d("onSingleTapUp", "SingleTapUp");
        GameView.handleSingleTapUp(event);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d("onLongPress", "Long Press");
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float xVel, float yVel) {
        Log.d("onFling", "Fling");
        GameView.handleFling(event1,event2,xVel,yVel);
        return true;
    }

}
