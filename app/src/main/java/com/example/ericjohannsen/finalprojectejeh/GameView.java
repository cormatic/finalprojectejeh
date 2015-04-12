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

import java.lang.reflect.Array;
import java.util.ArrayList;


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
    private target t;

    private static float XVelocity = 0;
    private static float YVelocity = 0;
    private static float curX = 300;
    private static float curY = 500;
    private static double counter = 0;
    private static int width;
    private static int height;
    private static boolean enableCounter = false;

    private double curZ = 5;
    private ArrayList<target> tArray = new ArrayList<>();


    public GameView(Context context) {
        super(context);
        activity = (MainActivity) this.getContext();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        curX = width/2 - 10;
        curY = height/1.2f - 10;
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
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
    }

    public void setTargetArray(int numTargets, Canvas canvas)
    {
        bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
        ArrayList<Levels> levels =  activity.getLevels();
        ArrayList<Integer> positions = levels.get(0).getTargetArrayList();
        for(int i=0; i <positions.size()/4; i++)
        {
            t = new target(bmp2,canvas);
            t.setX(positions.get(i*4));
            t.setY(positions.get(i*4+1));
            t.setZ(positions.get(i*4+2));
            t.setR(positions.get(i*4+3));
            tArray.add(t);
        }
    }

    //fling event
    public static void handleFling(MotionEvent event1, MotionEvent event2, float xVel, float yVel)
    {
        Log.d("Width:", String.valueOf(width));
        Log.d("Height", String.valueOf(height));
        //XVelocity = (event2.getX() - event1.getX()) / (event1.getDownTime() / 100000);
        //YVelocity = (event2.getY() - event1.getY()) / (event1.getDownTime() / 100000);
        XVelocity = xVel/300;
        YVelocity = yVel/300;
        Log.d("xVel",String.valueOf(XVelocity));
        Log.d("yVel",String.valueOf(YVelocity));

        enableCounter = true;
    }

    //for now reset with single tap
    public static void handleSingleTapUp(MotionEvent event)
    {
        if (event.getX() > width-(width/8.5) && event.getY() < (height/13)) {
            Log.d("Pause", "Pause Menu");
        }

        XVelocity = 0;
        YVelocity = 0;
        curX = width/2 - 10;
        curY = height/1.2f - 10;
        enableCounter = false;
        counter = 0;
    }

    protected void Draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        //note center of android guy is about (36,36)
        for(int i=0; i<tArray.size(); i++)
        {
            if (gameLoopThread.circleCollisionDetection(curX + 10, curY + 10, curZ, tArray.get(i).getX()+tArray.get(i).getR(), tArray.get(i).getY()+tArray.get(i).getR(),tArray.get(i).getZ(), 10, tArray.get(i).getR())) {
                XVelocity = 0;
                YVelocity = 0;
                enableCounter = false;
                counter = 0;

            }
        }
        curX += XVelocity;
        curY += YVelocity;
        curZ = -.5*Math.pow(counter/100 -3,2) + 9.5;

        //so (150, 150) is center of this circle

        ArrayList<Boolean> zTable = new ArrayList<>();
        for(int i=0; i<tArray.size(); i++)
        {
            int z = tArray.get(i).getZ();
            zTable.add(z<curZ);
        }
        //Targets
        for(int i=0; i<tArray.size(); i++)
        {
            if (zTable.get(i))
                tArray.get(i).drawBitmap();
        }

        //player circle
        canvas.drawBitmap(bmp, curX, curY, null);

        for(int i=0; i<tArray.size(); i++)
        {
            if (!zTable.get(i))
                tArray.get(i).drawBitmap();
        }

        //pause
        canvas.drawBitmap(bitmap, width-(width/8.5f), 0, null);
        if(enableCounter)
        {
            counter++;
        }
        Log.d("curZ",String.valueOf(curZ));
    }
}
