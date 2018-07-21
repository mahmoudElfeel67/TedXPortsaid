package com.bloomers.tedxportsaid.Model;


import android.support.annotation.Keep;

@Keep
public class Schedule {

    private String date;
    private String session_name;
    private String background;

    public String getDate() {
        return date;
    }

    public String getSession_name() {
        return session_name;
    }

    public String getBackground() {
        return background;
    }
}
