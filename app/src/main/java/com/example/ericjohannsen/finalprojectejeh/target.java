package com.example.ericjohannsen.finalprojectejeh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Evan on 4/7/2015.
 */
public class target {

    Canvas canvas;
    Bitmap bitmap;
    int X;
    int Y;
    int Z;
    int R;

    public target(Bitmap bitmap, Canvas canvas)
    {
        this.canvas = canvas;
        this.bitmap = bitmap;
    }

    public void setX(int x) {
        X = x;
    }

    public void setY(int y) {
        Y = y;
    }

    public void setZ(int z) {
        Z = z;
    }

    public void setR(int r) {
        R = r;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getZ() {
        return Z;
    }

    public int getR() {
        return R;
    }

    public void drawBitmap()
    {
        canvas.drawBitmap(bitmap,X,Y,null);
    }


}
