package com.dev.kyle.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity implements GameOverCallBack
{

    private GameView gameView;
    private int screenWidth;
    private int screenHeight;

    private SharedPreferences sharedPreferences;
    public static final String PREF_NAME = "TutTutPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        gameView = new GameView(this, screenWidth, screenHeight);
        setContentView(gameView);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        gameView.resume();
    }

    @Override
    public void gameOver(int score, int level)
    {
        int highScore = getHighScore(score);
        final String gameInfo = String.format("Score: %1$d\nLevel: %2$d\nHigh Score: %3$d\n\nPlay again?",score,level,highScore);

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                builder.setTitle("TutTut didn't make it!");
                builder.setMessage(gameInfo);


                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        restart();
                    }
                });

                builder.setNegativeButton("Quit", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        gotoMainMenu();
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

    }

    private int getHighScore(int currentScore)
    {
        int highScore = sharedPreferences.getInt("high_score", 0);
        if(currentScore > highScore)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("high_score", currentScore);
            editor.commit();
            highScore = currentScore;
        }
        return highScore;
    }

    private void restart()
    {
        Intent intent = getIntent();
        startActivity(intent);
        finish();
    }
    private void gotoMainMenu()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}


interface GameOverCallBack
{
    void gameOver(int score, int level);
}
