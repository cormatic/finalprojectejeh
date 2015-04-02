package com.example.ericjohannsen.finalprojectejeh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by joha1eri on 3/17/15.
 */
public class GameView extends SurfaceView{
    private Bitmap bmp;
    private Bitmap bmp2;
    private Bitmap bitmap;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private MainActivity activity;

    private static float XVelocity = 0;
    private static float YVelocity = 0;
    private static float curX = 300;
    private static float curY = 500;
    private static int width;
    private static int height;

    public GameView(Context context) {
        super(context);
        activity = (MainActivity) this.getContext();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    }
                    catch (InterruptedException e) { }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
        //load images into memory here
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.smallcircle);
        bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
    }

    //fling event
    public static void handleFling(MotionEvent event1, MotionEvent event2, float xVel, float yVel)
    {
        Log.d("Width:", String.valueOf(width));
        Log.d("Height", String.valueOf(height));
        XVelocity = (event2.getX() - event1.getX()) / (event1.getDownTime() / 16000);
        YVelocity = (event2.getY() - event1.getY()) / (event1.getDownTime() / 16000);
    }

    //for now reset with single tap
    public static void handleSingleTapUp(MotionEvent event)
    {
        if (event.getX() > width-(width/8.5) && event.getY() < (height/13)) {
            Log.d("Pause", "Pause Menu");
        }

        XVelocity = 0;
        YVelocity = 0;
        curX = 300;
        curY = 500;
    }

    protected void Draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        //note center of android guy is about (36,36)
        if(gameLoopThread.circleCollisionDetection(curX+10,curY+10,150,150,10,50))
        {
            XVelocity = 0;
            YVelocity = 0;
        }

        curX += XVelocity;
        curY += YVelocity;
        //so (150, 150) is center of this circle
        canvas.drawBitmap(bmp2, 100, 100, null);
        canvas.drawBitmap(bmp, curX, curY, null);
        canvas.drawBitmap(bitmap, width-80, 0, null);
    }
}
