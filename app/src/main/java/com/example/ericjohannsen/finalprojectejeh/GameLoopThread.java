package com.example.ericjohannsen.finalprojectejeh;

import android.graphics.Canvas;

/**
 * Created by joha1eri on 3/17/15.
 */
public class GameLoopThread extends Thread {
    private GameView view;
    private boolean running = false;



    public GameLoopThread(GameView gameView) {
        view = gameView;
    }

    public void setRunning(boolean run) { running = run; }

    @Override
    public void run() {
        while (running) {
            Canvas c = null;
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.Draw(c);
                }
            }
            finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
        }
    }

    //formula for two circle collison is (x2-x1)^2 + (y1-y2)^2 <= (r1+r2)^2
    //!!!Note these values need to be centers of the circles
    public boolean circleCollisionDetection(float X1, float Y1, int X2, int Y2, int R1, int R2)
    {
        if((Math.pow((X2 - X1),2) + Math.pow((Y1 - Y2),2)) <= Math.pow((R1+R2),2))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //note  that circleX and circleY need to be center of the circle
    public boolean pointCollisionDetection(float pointX, float pointY, int circleX, int circleY, int R)
    {
        if((Math.pow((pointX - circleX),2) + Math.pow((pointY - circleY),2)) <= Math.pow(R,2))
        {
            return true;
        }
        else
        {
            return false;
        }
    }



}
