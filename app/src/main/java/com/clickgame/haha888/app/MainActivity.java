package com.clickgame.haha888.app;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    /*
    Create object of firebaseremoteconfig.
     */
    private FirebaseRemoteConfig firebaseRemoteConfig;

    /*
    creating the String variable for Activity Navigation using FirebaseRemoteConfig.
     */
    private  String text1=" ";

    /*
    creating a shaed preferences variable for performing dialog related operations.
     */
    private SharedPreferences sharedPreferences;

    private static final String TAG = "mytag";

    /*
    Creating string varibales for shared preferences .
     */
    private static  final  String DialogSharedPreferencs="DialogSharedPreferences";

    private static  final String RatedOrNot="Rated";

    private static final String VisitNumber="VisitNumber";


    /* Create a boolean flag to keep track of whether the dialog was shown or not*/

    private boolean dialogShown = false;

    /*
    Creating the sharedpreferences editor object.
     */
    private SharedPreferences.Editor editor;

    /*
    creating the object of the Dialog class for our custom dialog box.
    */

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        initialising the firebaseremoteconfig object and and setting it's setting as well
         */
        firebaseRemoteConfig=FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings=new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(60).build();

        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        firebaseRemoteConfig.setDefaultsAsync(R.xml.defaultvalues);
        FetchDataRemoteConfig();

        /*
        Initializing the shared preferences variable.
         */
        sharedPreferences=getSharedPreferences(DialogSharedPreferencs, Context.MODE_PRIVATE);

        /*
        Initializing the editor object of shared preferences.
         */
        editor=sharedPreferences.edit();
        /*
        Initialising the object of the Dialog class for our custom dialog box.
         */
        dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialogboxlayout);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        /*
        Attaching the objects of the Buttons of Custom Dialog Box to their respective ids.
         */
        Button NotNow=dialog.findViewById(R.id.NotNowButton);
        Button RateIt=dialog.findViewById(R.id.RateItButton);

        /*
        Setting the background for the DialogBox and this will only work in andorid version>=Lillopop
         */
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialogboxupperpart));
        }
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = dpToPixels(360);
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);
        }


        /*
                Creating the onclicklistener for buttons of Custom dialog box.
         */
        RateIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShown=true;
                editor.putBoolean(RatedOrNot,true);
                editor.apply();
                String PackAgeNAme="com.clickgame.haha888.app";
                try {
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+PackAgeNAme));
                    startActivity(intent);
                    dialog.dismiss();

                }
                catch (ActivityNotFoundException e){
                    Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=" + PackAgeNAme));
                    startActivity(intent);
                    dialog.dismiss();

                }
            }
        });
        NotNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                implementconditions();
            }
        });


        /*
        Checking if the user is visiting the app first time if yes,then perform regular navigation.
         */

            if(CheckVisitNumber()){
                /*
                Because FirebaseRemote Config is runnig ascncronously,chances are high that even during frist start of the
                App after installation the user will be taekn directly to third activity that ,to avoid this we call handler method
                and pass intent to startactivity() here,we're not using the implemenations() method;
                 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },1500);
            }
            else if (sharedPreferences.getBoolean(RatedOrNot,false)==false&&sharedPreferences.getInt(VisitNumber,1)>1){
                dialog.show();
            }
            else {
                implementconditions();
            }

    }

    /*
    creating a method that will implement clients need of navigating between
    third and second activity on basis of conditions
     */
    private void implementconditions(){
//        getting random number between 0-9.
        int number=generateRandomNumber();
        Log.d(TAG, "implementconditions: "+number);
//        getting data from firebaseremote config;
        if(text1.equals("ABC")||number==9){
            NavigateToActivity(MainActivity.this, SecondActivity.class,1500);
            Log.d(TAG, "implementconditions: going to second activity");
        }
        else{
            NavigateToActivity(MainActivity.this,FourthActivity.class,1500);
            Log.d(TAG, "implementconditions: going to third activity");
        }
    }

    /*
    This method is used to Navigate from Splash Activity to Other activity
     */
    private  void  NavigateToActivity(final Context context, final Class<? extends Activity> targetActivity, int delayMillis){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context,targetActivity);
                startActivity(intent);
                finish();
            }
        },delayMillis);
    }
    /*
    This method helps to convert dp to pixles.it is used to set the width of our custom dialog box.
     */
    private int dpToPixels(int dpValue) {
        float density = getBaseContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * density);
    }


    /*
    Creating a function to check if user is opening app for the first time.
     */
    private  Boolean CheckVisitNumber(){
        // Check if it's the first time the app is being opened
        int NumberOfVisit=sharedPreferences.getInt(VisitNumber,1);
        if (NumberOfVisit<=1) {
            editor.putInt(VisitNumber,2);
            editor.apply();
            return true;
        }
        return false;
    }
    /*
    creating a fucntion to fetch the data from the firebase remote config.
     */
    public void FetchDataRemoteConfig(){
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(task.isSuccessful()){
                    text1=firebaseRemoteConfig.getString("text1");
                    Log.d(TAG, "onComplete: fetched;"+text1);
                }
            }
        });
    }

    /*
    creating a method to generate Random number between 0-9;
     */
    public  int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(10);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(dialogShown){
            implementconditions();
        }
    }
}