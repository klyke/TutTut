package com.dev.kyle.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by kyle on 12/30/16.
 */
public class Player
{
    private Bitmap bitmap;
    private int x;
    private int y;
    private int playerWidth;
    private int playerHeight;
    private int screenWidth;
    private int screenHeight;
    public static int lowerMargin = 128;


    public Player(Context context)
    {
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        playerWidth = 112;
        playerHeight = 144;

        Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.tuttut);
        bitmap = Bitmap.createScaledBitmap(b, playerWidth, playerHeight, true);
        x = (screenWidth - bitmap.getWidth())/2;
        y = screenHeight - bitmap.getHeight() - lowerMargin;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }
    public void setBitmap(Bitmap b)
    {
        bitmap = b;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setXY(int _x, int _y)
    {
        x = _x;
        y = _y;
    }

    public void changeX(int dx)
    {
        int new_x = x + dx;
        if(new_x + playerWidth > screenWidth)
        {
            x = screenWidth - playerWidth;
        }
        else if(new_x < 0)
        {
            x = 0;
        }
        else
        {
            x = new_x;
        }
    }

    public void update(int x_pos)
    {
        if(x_pos == -1)
        {
            return;
        }
        int new_x = x_pos - (playerWidth /2); //left side

        if(new_x + playerWidth > screenWidth)
        {
            x = screenWidth - playerWidth;
        }
        else if(new_x < 0)
        {
            x = 0;
        }
        else
        {
            x = new_x;
        }
    }

    public Rect getRect()
    {
        return new Rect(x, y, x+playerWidth, y+playerHeight);
    }
}
