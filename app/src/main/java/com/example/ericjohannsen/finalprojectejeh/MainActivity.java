package com.example.ericjohannsen.finalprojectejeh;

import android.gesture.GestureOverlayView;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;


public class MainActivity extends ActionBarActivity implements OnGestureListener{

    private GameView gameView;
    private GestureDetector gestureDetector;
    private float XVelocity;
    private float YVelocity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gestureDetector = new GestureDetector(getApplicationContext(), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startGame(View view) {
        gameView = new GameView(this);
        setContentView(gameView);
    }

    public void openInGameMenu() {
        //pause the game and open up a menu
    }

    @Override
    public void onBackPressed() {
        if (gameView != null) {
            setContentView(R.layout.activity_main);
            gameView = null;
        }
        else {
            super.onBackPressed();
        }
    }

    public float getXVelocity() {
        return XVelocity;
    }

    public float getYVelocity() {
        return YVelocity;
    }



// <editor-fold desc="Gesture Overrides">
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
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
        XVelocity = 0;
        YVelocity = 0;
        gameView.setCurX(200);
        gameView.setCurY(600);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d("onLongPress", "Long Press");
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float xVel, float yVel) {
        XVelocity = (event2.getX() - event1.getX()) / (event1.getDownTime() / 160000);
        YVelocity = (event2.getY() - event1.getY()) / (event1.getDownTime() / 160000);
        Log.d("Time", String.valueOf(event1.getDownTime() / 160000));
        return false;
    }
//</editor-fold>
}