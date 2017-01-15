package com.dev.kyle.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by kyle on 1/1/17.
 */
public class Seagull
{
    private int x;
    private int y;
    private int speed;
    private int screenHeight;
    private int screenWidth;
    private int gullWidth;
    private int gullHeight;
    private int maxSpeed = 60;
    private Bitmap bitmap;
    Random generator;

    public Seagull(Context context, int screenX, int screenY)
    {
        screenHeight = screenY;
        screenWidth = screenX;

        gullWidth = 128;
        gullHeight = 128;

        Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.gull);
        bitmap = Bitmap.createScaledBitmap(b, gullWidth, gullHeight, true);
        generator = new Random();
        speed = generator.nextInt(30) + 10;
        x = generator.nextInt(screenWidth-gullWidth);
        y = -gullHeight;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void update()
    {
        y += speed;
        if(y >= screenHeight)
        {
            x = generator.nextInt(screenWidth-gullWidth);
            y = -gullHeight;
            GameView.incrementScore();
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
        return new Rect(x, y, x+gullWidth, y+gullHeight);
    }

    public void levelUp(int dv)
    {
        int newSpeed = speed + dv;
        speed = newSpeed > maxSpeed ? speed : newSpeed;
    }


}
