package com.dev.kyle.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by kyle on 1/4/17.
 */
public class Hammer
{
    private Bitmap bitmap;
    private int hammerWidth;
    private int hammerHeight;
    private int screenWidth;
    private int screenHeight;
    private int degree;
    private int deltaDegree;
    private int speed;
    private int x;
    private int y;
    private Matrix rotationMatrix;
    private Random generator;


    public Hammer(Context context, int screenW, int screenH)
    {
        screenWidth = screenW;
        screenHeight = screenH;
        hammerWidth = 128;
        hammerHeight = 160;
        degree = 0;
        deltaDegree = 5;

        rotationMatrix = new Matrix();
        generator = new Random();
        x = generator.nextInt(screenW - hammerWidth);
        speed = generator.nextInt(35) + 40;
        y = -hammerHeight;
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.hammer);
        bitmap = Bitmap.createScaledBitmap(b, hammerWidth, hammerHeight, true);
    }

    public Bitmap getBitmap(boolean rotate)
    {
        if(rotate)
        {
            degree = (degree + deltaDegree) % 360;
        }
            rotationMatrix.setRotate(degree);
            Bitmap b = Bitmap.createScaledBitmap(bitmap, hammerWidth, hammerHeight, true);
            return Bitmap.createBitmap(b,
                    0, 0, bitmap.getWidth(), bitmap.getHeight(), rotationMatrix, true);
    }

    public void update()
    {
        y += speed;
        if(y >= screenHeight)
        {
            y = -hammerHeight;
            x = generator.nextInt(screenWidth - hammerWidth);
            speed = generator.nextInt(35) + 40;
        }
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public Rect getRect()
    {
        return new Rect(x, y, x+hammerWidth, y+hammerHeight);
    }
}
