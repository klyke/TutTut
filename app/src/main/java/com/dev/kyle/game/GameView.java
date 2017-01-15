package com.dev.kyle.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kyle on 12/30/16.
 */
public class GameView extends SurfaceView implements Runnable
{
    volatile boolean playing;
    private Thread gameThread = null;

    private Player player;
    private ArrayList<Seagull> seagulls;
    private ArrayList<Hammer> hammers;
    private Beach beach;
    private Boom boom;
    private static double nextLevelFactor;
    private final int initialNumObstacles = 3;
    private final int hammerStartLevel = 10;
    private boolean hammerAdded;

    Random rnd;

    private int screenWidth;
    private int screenHeight;

    private static int score;
    private static int level;
    private static boolean levelUp = false;
    private boolean updateMotion;
    private Paint obstaclePaint;
    private Paint textPaint;
    private Paint sliderPaint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Context context;

    private GameOverCallBack gameOverCallBack;

    public GameView(Context cont, int screenX, int screenY)
    {
        super(cont);
        score = 0;
        level = 1;
        nextLevelFactor = 2.0/3.0;
        hammerAdded = false;
        context = cont;
        updateMotion = true;
        gameOverCallBack = (GameOverCallBack) context;
        screenWidth = screenX;
        screenHeight = screenY;
        player = new Player(context);
        beach = new Beach(context, screenWidth, screenHeight);
        //hammer = new Hammer(context, screenWidth, screenHeight);
        boom = new Boom(context, screenWidth, screenHeight);
        surfaceHolder = getHolder();

        sliderPaint = new Paint();
        sliderPaint.setColor(ContextCompat.getColor(context, R.color.fingerSlider));
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(64);
        obstaclePaint = new Paint();
        obstaclePaint.setStyle(Paint.Style.FILL);
        obstaclePaint.setColor(Color.rgb(255, 0, 0));

        seagulls = new ArrayList<>();
        for(int i = 0; i< initialNumObstacles; i++)
        {
            seagulls.add(new Seagull(context, screenWidth, screenHeight));
        }

        hammers = new ArrayList<>();
        rnd = new Random();
    }

    @Override
    public void run()
    {
        while(playing)
        {
            update();
            draw();
            control();
        }
    }

    private void update()
    {
        if(updateMotion)
        {
            player.update(-1);

            for (int i = 0; i < seagulls.size(); i++)
            {
                seagulls.get(i).update();
                if (Rect.intersects(player.getRect(), seagulls.get(i).getRect()))
                {
                    if(bitmapsOverlap(player.getBitmap(), seagulls.get(i).getBitmap(), player.getRect(), seagulls.get(i).getRect()))
                    {
                        draw();
                        gameOver();
                    }
                }
            }

            if(hammerAdded)
            {
                for(int i = 0; i<hammers.size(); i++)
                {
                    hammers.get(i).update();
                    if (Rect.intersects(player.getRect(), hammers.get(i).getRect()))
                    {
                        player.setBitmap(boom.getBitmap());
                        int boomSize = boom.getBoomHeight();
                        player.setXY(player.getX() - (boomSize / 4), player.getY() - (boomSize / 4));
                        draw();
                        gameOver();
                    }
                }
            }
        }
    }

    private void draw()
    {
        if(surfaceHolder.getSurface().isValid())
        {
            if(levelUp)
            {
                nextLevel();
                levelUp = false;
            }

            canvas = surfaceHolder.lockCanvas();

            //Draw background
            canvas.drawBitmap(beach.getBitmap(), 0,0,obstaclePaint);

            //Draw finger slider
            canvas.drawRect(0, screenHeight - Player.lowerMargin, screenWidth, screenHeight, sliderPaint);

            //Draw TutTut
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    obstaclePaint);

            //Draw Hammers
            if(hammerAdded)
            {
                for(int i = 0; i<hammers.size(); i++)
                {
                    Hammer h = hammers.get(i);
                    canvas.drawBitmap(h.getBitmap(updateMotion),
                            h.getX(),
                            h.getY(),
                            obstaclePaint);
                }
            }

            //Draw Gulls
            for(int i = 0; i< seagulls.size(); i++)
            {
                Seagull s = seagulls.get(i);
                canvas.drawBitmap(
                        s.getBitmap(),
                        s.getX(),
                        s.getY(),
                        obstaclePaint);
            }

            canvas.drawText(Integer.toString(score), 32, 64, textPaint);
            canvas.drawText(Integer.toString(level), 32, 135, textPaint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control()
    {
        try
        {
            gameThread.sleep(17);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void pause()
    {
        playing = false;
        try
        {
            gameThread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void resume()
    {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private Rect getIntersection(Rect r1, Rect r2)
    {
        int interL = Math.max(r1.left, r2.left);
        int interR = Math.min(r1.right, r2.right);

        if(interR <= interL) //no intersection
        {
            return null;
        }
        else
        {
            int interT = Math.max(r1.top, r2.top);
            int interB = Math.min(r1.bottom, r2.bottom);
            if(interB <= interT) //no intersection
            {
                return null;
            }
            else //intersection
            {
                return new Rect(interL, interT, interR, interB);
            }
        }
    }

    private boolean bitmapsOverlap(Bitmap b1, Bitmap b2, Rect r1, Rect r2)
    {

        Rect inters = getIntersection(r1, r2);
        for (int x = inters.left; x < inters.right; x++)
        {
            for (int y = inters.top; y < inters.bottom; y++)
            {
                if((b1.getPixel(x - r1.left, y - r1.top) != Color.TRANSPARENT)
                        && b2.getPixel(x - r2.left, y - r2.top) != Color.TRANSPARENT)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private void gameOver()
    {
        updateMotion = false;
        gameOverCallBack.gameOver(score, level);
    }

    public static void incrementScore()
    {
        score += Math.max(1, (Math.floor(level*0.5))); //score proportional to level
        //if(score % 25 == 0)
        int levelCriteria = (int)Math.floor(nextLevelFactor*Math.sqrt(score));
        if(levelCriteria > level)
        {
            levelUp = true;
        }
    }

    private void nextLevel()
    {
        //gull added each level until level 10, then added every three levels
        if(level < 10 || level%3 == 0)
        {
            seagulls.add(new Seagull(context, screenWidth, screenHeight));
        }


        for(int i = 0; i< seagulls.size(); i++)
        {
            seagulls.get(i).levelUp(3);
        }

        obstaclePaint.setARGB(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
        level++;
        if(level >= hammerStartLevel)
        {
            hammerAdded = true;
        }
        if(level%5 == 0 && hammerAdded)
        {
            hammers.add(new Hammer(context, screenWidth, screenHeight));
        }
    }

    private int oldX;
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                oldX = (int)motionEvent.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if(updateMotion)
                {
                    int newX = (int)motionEvent.getX();
                    int dx = newX - oldX;
                    player.changeX(dx);
                    oldX = newX;
                }
                break;
            default:
                break;
        }
        return true;
    }

//    private boolean touchOnTutTut;
//    @Override
//    public boolean onTouchEvent(MotionEvent motionEvent)
//    {
//        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
//        {
//            case MotionEvent.ACTION_DOWN:
//                touchOnTutTut = player.getRect().contains((int)motionEvent.getX(), (int)motionEvent.getY());
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if(updateMotion && touchOnTutTut)
//                {
//                    int x = (int)motionEvent.getX();
//                    player.update(x);
//                }
//                break;
//            default:
//                break;
//        }
//        return true;
//    }
}
