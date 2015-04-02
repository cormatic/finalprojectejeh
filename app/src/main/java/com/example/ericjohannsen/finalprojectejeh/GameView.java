package com.example.ericjohannsen.finalprojectejeh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private MainActivity activity;

    private static float XVelocity = 0;
    private static float YVelocity = 0;
    private static float curX = 200;
    private static float curY = 300;

    public GameView(Context context) {
        super(context);
        activity = (MainActivity) this.getContext();
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
    }

    //fling event
    public static void handleFling(MotionEvent event1, MotionEvent event2, float xVel, float yVel)
    {
        XVelocity = (event2.getX() - event1.getX()) / (event1.getDownTime() / 500000);
        YVelocity = (event2.getY() - event1.getY()) / (event1.getDownTime() / 500000);
    }

    //for now reset with single tap
    public static void handleSingleTapUp(MotionEvent event)
    {
        XVelocity = 0;
        YVelocity = 0;
        curX = 200;
        curY = 300;
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
    }



}
