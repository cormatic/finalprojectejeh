package com.example.ericjohannsen.finalprojectejeh;

import android.app.Activity;
import android.content.DialogInterface;
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

import java.util.logging.Filter;


public class MainActivity extends Activity implements View.OnClickListener{
    private GameView gameView;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gestureDetector = new GestureDetector(getApplicationContext(), new EventHandler(this));
        gestureListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

    }

    @Override
    public void onClick(View v) {

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
        //setting the touch listeners to the gameView, required
        gameView.setOnClickListener(this);
        gameView.setOnTouchListener(gestureListener);
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
}