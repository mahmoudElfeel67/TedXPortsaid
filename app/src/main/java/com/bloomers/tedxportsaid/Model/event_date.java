package com.bloomers.tedxportsaid.Model;


import android.support.annotation.Keep;

@Keep
public class event_date {
    private int year ;
    private int day;
    private int hour;
    private int minute;
    private int month;
    private int before_day;
    private int before_year;
    private int before_month;

    public int getYear() {
        return year;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getMonth() {
        return month;
    }

    public int getBefore_day() {
        return before_day;
    }

    public int getBefore_year() {
        return before_year;
    }

    public int getBefore_month() {
        return before_month;
    }
}
