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
}
