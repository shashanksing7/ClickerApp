package com.clickgame.haha888.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class FourthActivity extends AppCompatActivity {

        /*
    Creating the objects of View Widgets.
     */
    private AppCompatImageButton FaceBook,Twitter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        /*
        <****************>
         */
        FaceBook=findViewById(R.id.facebookbutton);
        Twitter=findViewById(R.id.twitterbutton);


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

    }
    @Override
    public void onBackPressed() {
        // When the back button is pressed, go back to SecondActivity
        Intent intent = new Intent(FourthActivity.this, SecondActivity.class);
        startActivity(intent);
        finish(); // Finish ThirdActivity so that it's removed from the back stack
    }
}