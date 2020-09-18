package com.redeaoba.api.util;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CustomDateTime implements Serializable {

    private int day;
    private int mounth;
    private int year;
    private int hour;
    private int minute;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMounth() {
        return mounth;
    }

    public void setMounth(int mounth) {
        this.mounth = mounth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

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

    public LocalDateTime toLocalDateTime(){
        return LocalDateTime.of(this.year, this.mounth, this.day, this.hour, this.minute);
    }

    public static CustomDateTime byLocalDateTime(LocalDateTime localDateTime){
        CustomDateTime custom = new CustomDateTime();
        custom.setYear(localDateTime.getYear());
        custom.setMounth(localDateTime.getMonthValue());
        custom.setDay(localDateTime.getDayOfMonth());
        custom.setHour(localDateTime.getHour());
        custom.setMinute(localDateTime.getMinute());

        return custom;
    }
}
