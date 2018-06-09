package com.bloomers.tedxportsaid.Manager;


import android.app.Activity;
import android.widget.TextView;

import java.util.Random;
public class RandomThreadManager {

    private Thread expiryThreadExpiryCheck;
    private boolean stop = false;
    private int count = 0;
    private int increase = 0;
    int i1;

    private static RandomThreadManager mInstacne = new RandomThreadManager();

    public static RandomThreadManager getInstance() {
        return mInstacne;
    }

    public void addNumberChanger(final TextView number_picker, final Activity activity, final onDone onDone, final int limit) {
        increase = 0;
        stop = false;
        count = 3000/120;

        if(expiryThreadExpiryCheck!=null){
            return;
        }
        stop =false;
        expiryThreadExpiryCheck =  new Thread(new Runnable() {

            @Override
            public void run() {
                while (!stop){
                    try {
                        increase++;

                        if (count==increase){
                            stopRandom();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onDone.onDone(i1);

                                }
                            });

                        }
                        Thread.sleep(120);
                        int min = 0;
                        int max = limit-1;



                        Random r = new Random();
                        i1 = r.nextInt(max - min + 1) + min;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                number_picker.setText(String.valueOf(i1));

                            }
                        });



                    } catch (InterruptedException e) {

                    }
                }


            }
        });
        expiryThreadExpiryCheck.start();
    }


    public interface onDone{
        void onDone(int random);
    }

    public void stopRandom(){
        if (expiryThreadExpiryCheck!=null){
            stop = true;
            try {
                expiryThreadExpiryCheck.interrupt();
                expiryThreadExpiryCheck=  null;
            }catch (Exception e){
            }
        }
    }
}
