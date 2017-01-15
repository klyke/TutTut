package com.dev.kyle.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by kyle on 1/4/17.
 */
public class Boom
{
    private Bitmap bitmap;
    private int boomWidth;
    private int boomHeight;

    public Boom(Context context, int screenW, int screenH)
    {
        boomWidth = 256;
        boomHeight = 256;
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.boom);
        bitmap = Bitmap.createScaledBitmap(b, boomWidth, boomHeight, true);
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public int getBoomWidth()
    {
        return boomWidth;
    }

    public int getBoomHeight()
    {
        return boomHeight;
    }
}
