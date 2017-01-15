package com.dev.kyle.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by kyle on 1/4/17.
 */
public class Beach
{
    private Bitmap bitmap;

    public Beach(Context context, int screenW, int screenH)
    {
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.beach);
        bitmap = Bitmap.createScaledBitmap(b, screenW, screenH, true);
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }
}
