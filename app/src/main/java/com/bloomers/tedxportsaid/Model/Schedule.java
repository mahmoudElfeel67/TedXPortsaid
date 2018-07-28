package com.bloomers.tedxportsaid.Model;


import android.support.annotation.Keep;

@Keep
public class Schedule {

    private String date;
    private String session_name;

    public String getDate() {
        return date;
    }

    public String getSession_name() {
        return session_name;
    }

    public Schedule() {
    }

    public Schedule(String date, String session_name) {

        this.date = date;
        this.session_name = session_name;
    }
}
