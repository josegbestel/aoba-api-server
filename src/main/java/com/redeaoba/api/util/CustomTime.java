package com.redeaoba.api.util;

import java.io.Serializable;
import java.time.LocalTime;

public class CustomTime implements Serializable {

    private int hour;
    private int minute;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public LocalTime toLocalTime(){
        return LocalTime.of(this.hour, this.minute);
    }

    static public CustomTime byLocalTime(LocalTime localTime){
        CustomTime time = new CustomTime();
        time.setHour(localTime.getHour());
        time.setMinute(localTime.getMinute());

        return time;
    }
}
