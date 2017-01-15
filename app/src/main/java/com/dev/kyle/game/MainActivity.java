package com.dev.kyle.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    protected ImageButton buttonPlay, buttonScore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPlay = (ImageButton)findViewById(R.id.buttonPlay);
        buttonScore = (ImageButton)findViewById(R.id.buttonScore);

        buttonPlay.setOnClickListener(this);

        buttonScore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                makeToast("Scores!");
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        finish();
        return;
    }

    private void makeToast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View view)
    {
        startActivity(new Intent(this, GameActivity.class));
    }
}
