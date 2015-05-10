package com.example.ericjohannsen.finalprojectejeh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by joha1eri on 3/17/15.
 */
public class GameView extends SurfaceView{
    private Bitmap player;
    private Bitmap bitmap;
    private Bitmap menu;
    private Bitmap arrow;
    private Bitmap defMenu;
    private Bitmap vicMenu;

    private GameLoopThread gameLoopThread;
    private MainActivity activity;

    private float WindX = 0;
    private float WindY = 0;
    private float XVelocity = 0;
    private float YVelocity = 0;
    private float curX = 300;
    private float curY = 500;
    private float menuXVelocity = 0;
    private float menuYVelocity = 0;

    private double counter = 0;
    private double menuCounter = 0;
    private double curZ = 0;

    private int width;
    private int height;
    private int flingCount = 0;
    private int score = 0;
    private int gameLevel = 0;
    int smileLocation = -1;

    private boolean enableCounter = false;
    private boolean menuOpen = false;
    private boolean enableWind = false;
    private boolean tapReset = true;
    private boolean disableFling = false;
    private boolean defeatMenu = false;
    private boolean victoryMenu = false;

    String wind = "";
    MediaPlayer soundEffects;
    private ArrayList<target> tArray = new ArrayList<>();

    public GameView(Context context) {
        super(context);
        activity = (MainActivity) this.getContext();
        //get height and width of screen
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        gameLevel = activity.getGameLevel();

        //set initial position of player
        curX = width/2 - 10;
        curY = height/1.2f - 10;

        //set initial wind
        Wind();

        //game loop thread start
        gameLoopThread = new GameLoopThread(this);
        SurfaceHolder holder = getHolder();
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
                if(gameLoopThread.getState() == Thread.State.NEW)
                    gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });

        //load images into memory here
        player = BitmapFactory.decodeResource(getResources(), R.drawable.smallcircle);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
        menu = BitmapFactory.decodeResource(getResources(), R.drawable.menu);
        arrow = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
        defMenu = BitmapFactory.decodeResource(getResources(), R.drawable.defeat);
        vicMenu = BitmapFactory.decodeResource(getResources(), R.drawable.victory);
    }

    //set all our target positions for specific level
    public void setTargetArray(Canvas canvas, int smileOrFrown)
    {
        target t;
        tArray.clear();
        Bitmap bmp2;
        if(smileOrFrown == 0)
        {
            bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
            Log.d("regular circle", "reg");
        }
        else if(smileOrFrown == 1)
        {
            bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.smilecircle);
            Log.d("smile circle", "smile");
        }
        else
        {
            bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.frowncircle);
            Log.d("frowncircle", "frown");
        }

        ArrayList<Levels> levels =  activity.getLevels();
        ArrayList<Integer> positions = levels.get(gameLevel).getTargetArrayList();
        for(int i=0; i <positions.size()/4; i++)
        {
            t = new target(bmp2,canvas);
            t.setX(positions.get(i*4));
            t.setY(positions.get(i*4+1));
            t.setZ(positions.get(i*4+2));
            t.setR(positions.get(i*4+3));
            t.setBitmap();
            tArray.add(t);
        }
    }

    //fling event
    public  void handleFling(float xVel, float yVel)
    {
        //make sure nothing in game can be done when menu is open
        if(!menuOpen && !disableFling)
        {
            XVelocity = xVel / 300;
            YVelocity = yVel / 300;
            enableWind = true;
            enableCounter = true;
            disableFling = true;
            flingCount++;
        }
    }

    //generate wind
    private  void Wind()
    {
        Random rand = new Random();
        WindX = (rand.nextFloat() - .5f)*2;
        WindY = (rand.nextFloat() - .5f)*2;
    }

    private void menuPause()
    {
        menuXVelocity = XVelocity;
        menuYVelocity = YVelocity;
        menuCounter = counter;
        XVelocity = 0;
        YVelocity = 0;
        enableWind = false;
        menuOpen = true;
    }
    private void menuResume()
    {
        XVelocity = menuXVelocity;
        YVelocity = menuYVelocity;
        counter = menuCounter;
        menuOpen = false;
        if(disableFling)
        {
            enableWind = true;
        }
    }
    private void menuRestart()
    {
        XVelocity = 0;
        YVelocity = 0;
        curX = width / 2 - 10;
        curY = height / 1.2f - 10;
        enableCounter = false;
        enableWind = false;
        counter = 0;
        player = BitmapFactory.decodeResource(getResources(), R.drawable.smallcircle);
        menuOpen = false;
        disableFling = false;
        flingCount = 0;
        score = 0;
        defeatMenu = false;
        victoryMenu = false;
        Wind();
    }
    private void menuQuit()
    {
        activity.onBackPressed();
    }

    private void tempReset()
    {
        XVelocity = 0;
        YVelocity = 0;
        curX = width / 2 - 10;
        curY = height / 1.2f - 10;
        enableCounter = false;
        counter = 0;
        player = BitmapFactory.decodeResource(getResources(), R.drawable.smallcircle);
        Wind();
        enableWind = false;
        tapReset = true;
        disableFling = false;
        smileLocation = -2;
        if(flingCount == 5)
        {
            endGame();
        }
    }

    public void victoryMenu(MotionEvent event)
    {
        //quit
        if (event.getY() > height / 2 + 60 + 50)
        {
            menuQuit();
        }
        //restart
        else if (event.getY() > height / 2 + 60 - 50)
        {
            menuRestart();
        }
        //nextLevel
        else if(gameLevel<5)
        {
            gameLevel++;
            View view = null;
            activity.setGameLevel(gameLevel);
            activity.startGame(view);
        }
    }

    public void defeatMenu(MotionEvent event)
    {
        //quit
        if (event.getY() > height / 2 + 60 - 50)
        {
            menuQuit();
        }
        //restart
        else
        {
            menuRestart();
        }
    }

    //for now reset with single tap
    public  void handleSingleTapUp(MotionEvent event)
    {
        //open the menu and pause the game
        if (event.getX() > width-(width/8.5) && event.getY() < (height/13) && !menuOpen) {menuPause();}
        //if menu open and pause button pressed again, resume game
        else if (event.getX() > width-(width/8.5) && event.getY() < (height/13) && menuOpen){menuResume();}

        else if(victoryMenu && event.getX() > width/2-100 && event.getX() < width/2+100 && event.getY() >height/2-150+60 && event.getY() < height/2+150+60)
        {
            victoryMenu(event);
        }
        else if(defeatMenu && event.getX() > width/2-100 && event.getX() < width/2+100 && event.getY() >height/2-150+60 && event.getY() < height/2+100+60)
        {
            defeatMenu(event);
        }

        //menu open find 3 events in menu
        else if(menuOpen && event.getX() > width/2-100 && event.getX() < width/2+100 && event.getY() >height/2-150 && event.getY() < height/2+150)
        {
            //quit
            if (event.getY() > height / 2 + 50)
            {
                menuQuit();
            }
            //restart
            else if (event.getY() > height / 2 - 50)
            {
                menuRestart();
            }
            //resume
            else
            {
                menuResume();
            }
        }

        //reset player by 1 tap down
        else if(!menuOpen){tempReset();}
    }

    private int computeScore(double distance,int R)
    {
        if (distance <= R/5)
            return 10;
        else if (distance <= (2*R)/5)
            return 8;
        else if (distance <= (3*R)/5)
            return 6;
        else if (distance <= (4*R)/5)
            return 4;
        else if (distance <= R)
            return 2;
        else
            return 1;
    }

    private String computeWind(double x,double y)
    {
        double degree = (Math.atan(y/x))*(180/Math.PI);
        //southWest
        if(x<0 && degree<0)
            degree+= 270;

        //northEast
        else if(x>0 && degree<0)
            degree+=90;

        //northWest
        else if(x<0 && degree>0)
            degree+= 90;

        //southEast
        else
            degree+=270;

        //matrix for wind arrow turn calculation
        Matrix matrix = new Matrix();
        arrow = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
        matrix.postRotate(((float) -degree+90));
        arrow = Bitmap.createBitmap(arrow , 0 , 0 , arrow.getWidth(), arrow.getHeight(), matrix, true);
        double mag = Math.pow((x*x)+(y*y),.5);

        return String.valueOf(Math.round(mag*15));

    }

    private void endGame()
    {
        if(score>4)
        {
            soundEffects = MediaPlayer.create(activity,R.raw.win);
            soundEffects.start();
            victoryMenu = true;
        }

        else
        {
            defeatMenu = true;
        }
    }

    private void collisionDetection(int scale)
    {
        for(int i=0; i<tArray.size(); i++)
        {
            if (gameLoopThread.circleCollisionDetection(curX + scale/2, curY + scale/2, curZ, tArray.get(i).getX()+tArray.get(i).getR(), tArray.get(i).getY()+tArray.get(i).getR(),tArray.get(i).getZ(), scale/2, tArray.get(i).getR())) {
                double distance = Math.pow(Math.pow((curX+scale/2) - (tArray.get(i).getX()+tArray.get(i).getR()),2) + Math.pow((curY+scale/2) - (tArray.get(i).getY()+tArray.get(i).getR()),2),.5);
                score += computeScore(distance,tArray.get(i).getR());
                smileLocation = 1;
                XVelocity = 0;
                YVelocity = 0;
                counter = 0;
                enableCounter = false;
                enableWind = false;
                //amount of flings you get
                if(flingCount == 5)
                {
                    endGame();
                }
            }
        }
    }

    private void screenBounds()
    {
        if(curX < -10 || curX > width + 10 || curY< -10 || curY > height + 10)
        {
            tempReset();
            smileLocation = -3;
        }
    }

    protected void Draw(Canvas canvas)
    {
        canvas.drawColor(Color.LTGRAY);

        //scale needed for collision detection and scaling circles
        int scale = 20 - (Math.abs(Math.round(height / 1.2f - 10 - curY))/20);
        if(scale<5)
            scale = 5;

        //circle collision detection here
        collisionDetection(scale);
        screenBounds();

        //move player circle
        curX += XVelocity;
        curY += YVelocity;
        curZ = -2 * Math.pow(counter / 100 - 2, 2) + 12;

        if (enableWind)
        {
            curX += WindX;
            curY += WindY;
        }
        if(enableCounter)
        {
            counter++;
        }
        //scale player
        player = Bitmap.createScaledBitmap(player,scale, scale,true);
        if(counter>325)
        {
            canvas.drawBitmap(player, curX, curY, null);
        }

        //regular circle, smile or frown
        if(smileLocation > 0)
        {
            setTargetArray(canvas,1);
            smileLocation = -1;
        }
        else if (smileLocation == -2)
        {
            setTargetArray(canvas,0);
            smileLocation = -1;
        }
        else if(smileLocation == -3)
        {
            setTargetArray(canvas,2);
            smileLocation = -1;
        }
        //Draw all targets///////////////////////////////////////////////////////////////////////////////
        for(int i=0; i<tArray.size(); i++)
        {
            tArray.get(i).drawBitmap();
        }

        //draw player
        if(counter<=325)
        {
            canvas.drawBitmap(player, curX, curY, null);
        }

        //draw pause button
        canvas.drawBitmap(bitmap, width-(width/8.5f), 0, null);

        if(tapReset)
        {
            wind = computeWind(WindX, WindY);
            tapReset = false;
        }

        Paint paint = new Paint(Color.BLACK);
        paint.setTextSize(30);
        //draw wind text and arrow picture
        canvas.drawText(String.valueOf(score),10,30,paint);
        canvas.drawText(wind, 50, height - 40, paint);
        canvas.drawBitmap(arrow,15,height - 70,null);

        //draw pause menu
        if(menuOpen)
        {
            canvas.drawBitmap(menu,width/2-100,height/2-150,null);
        }
        //draw defeat menu
        if(defeatMenu)
        {
            smileLocation = -3;
            canvas.drawBitmap(defMenu,width/2-100,height/2-150,null);
        }
        //draw victory menu
        if(victoryMenu)
        {
            smileLocation = -2;
            canvas.drawBitmap(vicMenu,width/2-100,height/2-150,null);
        }
    }

}
