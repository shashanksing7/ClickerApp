package com.clickgame.haha888.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;

public class ThirdActivity extends AppCompatActivity {
    /*
    Creating the objects of View Widgets.
     */
    private ImageView Star1,Star2,Star3,Star4,Star5;
    /*
    <**********************>
     */
   private  TextView HighScore,CurrentScore,AverageCSP,message;
   /*
   <**********************>
    */
    private AppCompatImageButton FaceBook,Twitter;


    /*
    Creating Variable to Store the  Current Score of User.
     */
    private  int NumberOfClicks=0;
    private double AverageCPS=0;

    /*
    Creating Variables to store the name of our Shared preferences and Users HighScore.
     */
    private static  final String GameSharedPreferences="GameSharedPreferences";
    private static  final  String UserHighScore="HighScore";

    /*
        creating a Shared preferences object to find users HighScore and compare with cureentone
        to update it accordingly.
      */
    private SharedPreferences sharedPreferences;

    /*
    creating sharedPreferences editor object to save new High score.
     */
    SharedPreferences.Editor editor;

//    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        /*
        Getting Data from intent.
         */
        Intent intent=getIntent();
        if(intent!=null){
            NumberOfClicks=intent.getIntExtra("PlayerScore",0);
            AverageCPS=intent.getIntExtra("CPS",0);
        }

        /*
        Initializing the SharedPreferences object to find users HighScore and compare with cureentone
        to update it accordingly.
         */
        sharedPreferences=getSharedPreferences(GameSharedPreferences, Context.MODE_PRIVATE);

        /*
        Intiallising shared preferences object.
         */
        editor=sharedPreferences.edit();

        /*
        Attaching the objects to their Respective ID's.
         */
        Star1=findViewById(R.id.star1);
        Star2=findViewById(R.id.star2);
        Star3=findViewById(R.id.star3);
        Star4=findViewById(R.id.star4);
        Star5=findViewById(R.id.star5);

        /*
        <****************>
         */
        HighScore=findViewById(R.id.Highscore);
        CurrentScore=findViewById(R.id.currentscore);
        AverageCSP=findViewById(R.id.AVerageCSP);
        message=findViewById(R.id.message);

        /*
        <****************>
         */
        FaceBook=findViewById(R.id.facebookbutton);
        Twitter=findViewById(R.id.twitterbutton);

        /*
        Calling a method to Check for Users High Score and Design UI Accordingly;
         */
        CheckScore(NumberOfClicks);

        /*
        Adding on click Listener to both buttons.
         */
        FaceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://www.facebook.com/";
                opentURL(url);
            }
        });

        Twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://twitter.com/home";
                opentURL(url);
            }
        });
        /*
              <******************>
         */


    }

    private void CheckScore(int Score) {
        /*
        Getting users HighScore of user
         */
        int SavedScore=sharedPreferences.getInt(UserHighScore,0);
        /*
        comparing the Current Score and High Score.
         */
        if(SavedScore<Score){
            /*
            Saving the New High Score
             */
            editor.putInt(UserHighScore,Score);
            editor.apply();
            HighScore.setText("New HighScore");
            CurrentScore.setText(String.valueOf(Score));
            String UserScore= String.format("%.2f",AverageCPS);
            AverageCSP.setText("Average CSP:"+UserScore);
            message.setText("Master !");

        }
        else {
            HighScore.setText("HighScore:"+String.valueOf(SavedScore));
            CurrentScore.setText(String.valueOf(Score));
            String UserScore= String.format("%.2f",AverageCPS);
            AverageCSP.setText("Average CSP:"+UserScore);
            if (Score<100){
                message.setText("Noob !");
                Star2.setImageAlpha(80);
                Star3.setImageAlpha(80);
                Star4.setImageAlpha(80);
                Star5.setImageAlpha(80);
            } else if (Score<250) {
                message.setText("Learner !");
                Star3.setImageAlpha(80);
                Star4.setImageAlpha(80);
                Star5.setImageAlpha(80);
            } else if (Score<450) {
                message.setText("Achiever !");
                Star4.setImageAlpha(80);
                Star5.setImageAlpha(80);
            }
            else if(Score<550){
                message.setText("Pro !");
                Star5.setImageAlpha(80);
            } else if (Score<700)
            {
                message.setText("Master !");

            }
        }


    }

    /*
    This method opens twitter.com  and facebook.com in browser.
 */
    private void opentURL(String url) {
        /*
        Creating a URI object.
         */
        Uri uri=Uri.parse(url);
        /*
        Creating Intent.
         */
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);

        /*
        <***********************>
         */
    }
    @Override
    public void onBackPressed() {
        // When the back button is pressed, go back to SecondActivity
        Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
        startActivity(intent);
        finish(); // Finish ThirdActivity so that it's removed from the back stack
    }

}