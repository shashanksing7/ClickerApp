package com.clickgame.haha888.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "mytag";


    /*
        creating a Boolean Variable to Check if BackGround Thread Has Started ot not.
     */
    private Boolean ThreadStarted=false;

    /*
    This Variable will be Used to calculate Average CPS
     */
    private double updatedCPS=0;

        /*
    This Variable is used to aclculate CPS;
     */
    private int PreviousClickCount=0;

    /*
    Creating the object of the Background Thread classs.
     */
     private  TimerThread timerThread;

       /*
     Creating a variable to keep track of Click Count
      */
     private int clickCount = 0;

    /*
    creating a Boolean Variable to start and stop the CPS's BackGround THread.
     */
    private Boolean CPSThreadRunning=true;

    /*
    creating a variable that will store the time
     */
    private int time=0;

    /*
    creating a variable that stores the maximum time allowed i.e 1 minutes.
     */
    private int MaximumTime=60;

    /*
    Creating The Object of Text View to update our text's.
     */
    private TextView NumberOfClicks,CPS,TapToStart;
    private ProgressBar TimeLeftBar;

    /*

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        /*
        Attaching The View Widget's objects to their Respective ID's.
         */
        NumberOfClicks=findViewById(R.id.numbersclick);
        CPS=findViewById(R.id.cps);
        TapToStart=findViewById(R.id.Taptostarttext);
        TimeLeftBar=findViewById(R.id.progressBar);
        /*
           -------******-----------
         */
        /*
        Hiding The NumberClicks Initially.
         */
//        NumberOfClicks.setVisibility(View.GONE);

        /*
        Initialising the object of the background thread.
         */
        timerThread=new TimerThread(handler,MaximumTime);

    }

    /*
        creating the Handler to post events from background thread to UI Thread.
     */
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            time=msg.arg1;
            TimeLeftBar.setProgress(time);
            if(time==MaximumTime){
                navigateToFinalActivity();
            }
            return false;
        }
    });
    /*
    This Activity will be Used to Navigate to Final Activity.
     */
    private void navigateToFinalActivity() {
        /*
            creating an intent to navigate to another activity
            and send data.
         */
        Intent intent=new Intent(SecondActivity.this, ThirdActivity.class);
        intent.putExtra("PlayerScore",Integer.parseInt(NumberOfClicks.getText().toString()));
        intent.putExtra("CPS",(Integer.parseInt(NumberOfClicks.getText().toString()))/60);
        startActivity(intent);
        finish();


    }

    /*
        This Method Will be executed When ever the user Clicks on the Screen.
     */
    public void onScreenclick(View view){
        /*
        ShowingThe NumberClicks Initially.
         */
        NumberOfClicks.setVisibility(View.VISIBLE);
        /*
            Hiding the Tap to start text when the users start's playing the game
         */
        TapToStart.setVisibility(View.GONE);

        /*
        starting the background thread.
         */
        if(ThreadStarted==false){
            timerThread.start();
            startCPSBackGroundThread();
        }
        ThreadStarted=true;

        /*
        Updating the click count.
         */
        clickCount=Integer.parseInt(NumberOfClicks.getText().toString())+1;
        NumberOfClicks.setText(String.valueOf(clickCount));

    }

    /*
    Creating a method that will start a background thread .
    The thread is going to calculate the CPS and update it.
     */
    private void startCPSBackGroundThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(CPSThreadRunning){
                    double Clicks= Double.parseDouble(NumberOfClicks.getText().toString());
                    double cpstime=(double) time;
                    if(cpstime!=0&&time<=MaximumTime){
                        double update=Clicks/cpstime;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String formattedNumber = String.format("%.2f",update);
                                CPS.setText(formattedNumber);
                            }
                        });

                    }

//                    int CurrentClicksCount=Integer.parseInt(NumberOfClicks.getText().toString());
//                    int CurrentCPS=CurrentClicksCount-PreviousClickCount;
//                    PreviousClickCount=CurrentClicksCount;
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            CPS.setText(String.valueOf(CurrentCPS));
//                        }
//                    });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);


                    }
                }
            }
        }).start();
    }


    /*
        creating a method to stop the CPS's bcakground thread.
     */
    private  void  stopCPSBackGroundThread(){
       CPSThreadRunning=false;
    }

    /*
        Destroying the Background thread in onstop(),to avoid memoryleak.
     */
    @Override
    protected void onStop() {
        super.onStop();
        timerThread.stopthread();
        stopCPSBackGroundThread();
    }

}