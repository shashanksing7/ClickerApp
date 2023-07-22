package com.clickgame.haha888.app;

import android.os.Handler;
import android.os.Message;

/*
This Thread is used to update the timer in every one second.
 */
public class TimerThread extends  Thread{

    /*
    This Handler object is used to send message to the Main Thread
     */
    private Handler ThreadHandler;

    /*
    This is used to count the time,and is incremented in every one second
     */
    private int time=0;

    /*
    This variable is limit the time to max time set.
     */
    private int stoptime;
    /*
    This Boolean Variable is used to  terminate the background thread in a safe manner.
     */
    private boolean isthreadrunning=true;

    /*
Setting the Variables using the constructor.
 */
    TimerThread(Handler handler,int stoptime){
        ThreadHandler=handler;
        this.stoptime=stoptime;
    }


    @Override
    public void run() {
        /*
        This Blocks keep running unitl someone tries to stop it.
         */
        while(isthreadrunning){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            time++;
            Message message=Message.obtain();
            message.arg1=time;
            if(time<stoptime+1){
                ThreadHandler.sendMessage(message);
            }

        }
    }
    /*
    This method is used to stop the thread un safe manner.
     */

    protected void stopthread(){
        isthreadrunning=false;
    }


}
