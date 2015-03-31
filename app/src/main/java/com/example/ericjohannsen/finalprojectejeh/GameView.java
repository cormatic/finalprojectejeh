package com.example.ericjohannsen.finalprojectejeh;

import android.content.Context;
import android.gesture.GestureOverlayView;
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
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private MainActivity activity;
    private float curX = 300;
    private float curY = 800;

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
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.image);
    }

    int counter = 0;

    public void setCurX(float CurX){ curX = CurX; }

    public void setCurY(float CurY){ curY = CurY; }

    protected void Draw(Canvas canvas) {
        counter++;
        canvas.drawColor(Color.BLACK);
        float x = activity.getXVelocity();
        float y = activity.getYVelocity();
        if (counter % 40 == 0)
        {
            //Log.d("TrueX",String.valueOf(activity.getXVelocity()));
            //Log.d("XVEL:", String.valueOf(x));
           // Log.d("TrueY",String.valueOf(activity.getYVelocity()));
            //Log.d("YVEL:", String.valueOf(y));
        }
        curX += x;
        curY += y;
        canvas.drawBitmap(bmp, curX, curY, null);
        if(counter > 10000){counter = 0;}


    }
}
