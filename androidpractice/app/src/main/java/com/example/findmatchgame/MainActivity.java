package com.example.findmatchgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView startgame;
    private RadioGroup difficulty;
    private RadioButton chosendifficulty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);

        startgame=findViewById(R.id.start);
        difficulty=findViewById(R.id.difficultyradio);
        startgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosendifficulty=findViewById(difficulty.getCheckedRadioButtonId());
                if(chosendifficulty==null){
                    chosendifficulty=findViewById(R.id.Kolay);
                }
                Intent GameScreen= new Intent(getApplicationContext(), com.example.findmatchgame.GameScreen.class);
                GameScreen.putExtra("difficulty",chosendifficulty.getText());
                startActivity(GameScreen);
            }
        });
    }
}
