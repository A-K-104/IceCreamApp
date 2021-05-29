package com.example.icecreamapplication;


import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class came to replace the non function Date.java
 * This class save date in 2 formats dd/mm/yyyy or (int Year, int Month, int Day)
 * implements Serializable --> means that you can pass this class between Intents
 */

public class DateClass implements Serializable {
    private int day;
    private int month;
    private int year;

    /**
     * first option of builder gets
     * @param stringDate  dd/mm/yyyy
     */
    public  DateClass(String stringDate) {
        setDate(stringDate);
        resetToDate();
    }

    /**
     * second option of builder gets (int Year, int Month, int Day)
     * @param year -->the year
     * @param month-->the month
     * @param day  -->the day
     */
    public DateClass(int year, int month, int day) {
        this.day = day;
        this.month = month;
        this.year = year;
        resetToDate();
    }
    /**
     * tired option of builder set by today date
     */
    public DateClass(){
        SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        String date1 = df1.format(Calendar.getInstance().getTime());//gets the current date
        DateClass date = new DateClass(date1);
        this.day = date.getDay();
        this.month = date.getMonth();
        this.year = date.getYear();
    }
    /**
     * the function that can transfer string (dd/mm/yyyy) to date
     * at the fist and second if's we are testing that we are getting string that fit to our format of
     * @param stringDate dd/mm/yyyu
     * than we are updating the value of
     * @this.day by substring until the first '/'
     * @this.month by substring until the second '/'
     * @this.year  by saving the end string
     * in case of failed we are putting 1/1/1900 as def value
     */
    public void setDate(@NotNull String stringDate) {
        if (stringDate.contains("/")) {
            if (stringDate.substring(stringDate.indexOf("/")).contains("/")) {
                this.day = Integer.parseInt(stringDate.substring(0, stringDate.indexOf("/")));
                stringDate = stringDate.substring(stringDate.indexOf("/") + 1);
                this.month = Integer.parseInt(stringDate.substring(0, stringDate.indexOf("/")));
                this.year = Integer.parseInt(stringDate.substring(stringDate.indexOf("/") + 1));
            }
            else {
                this.day =1;
                this.month =1;
                this.year=1900;
            }
        }
        else {
            Log.d("DateClass", "error with date: "+stringDate);
            this.day =1;
            this.month =1;
            this.year=1900;
        }
        resetToDate();
    }

    /**
     * reformat the date if the month passes the 12 (December) it add a year and goes to January
     * Make sure that max day suits the mouth ex: 31 in January
     * every 4 year it make sure that it is allowing 29th in February
     * ex: 32/13/2020-->01/02/2021
     */
    private void resetToDate() {
        while (this.month > 12) {
            this.year++;
            this.month -= 12;
        }
        while (this.day > 31) {//in case that the day is bigger that one month
            if (this.month == 1 || this.month == 3 || this.month == 5 || this.month == 7 ||
                    this.month == 8 || this.month == 10 || this.month == 12) {
                if (this.day > 31) {
                    this.month++;
                    this.day -= 31;
                }
            } else {
                if (this.month == 4 || this.month == 6 || this.month == 9 || this.month == 11) {
                    if (this.day > 30) {
                        this.month++;
                        this.day -= 30;
                    }
                } else {
                    if (this.month == 2)
                        if ((this.year) % 4 == 0) {
                            if (this.day > 29) {
                                this.month++;
                                this.day -= 29;
                            }
                        } else {
                            if (this.day > 28) {
                                this.month++;
                                this.day -= 28;
                            }
                        }
                }
            }
            if (this.month > 12) {
                this.year++;
                this.month -= 12;
            }
        }
        if (this.month == 4 || this.month == 6 || this.month == 9 || this.month == 11) {
            if (this.day > 30) {
                this.month++;
                this.day -= 30;
            }
        } else {
            if (this.month == 2)
                if ((this.year) % 4 == 0) {
                    if (this.day > 29) {
                        this.month++;
                        this.day -= 29;
                    }
                } else {
                    if (this.day > 28) {
                        this.month++;
                        this.day -= 28;
                    }
                }
        }
        if (this.month > 12) {
            this.year++;
            this.month -= 12;
        }
        resetToDateZero();
    }
    /**
     * reformat the date if the day passes the 0  it takes a from the month and add to day according to month
     * Make sure that max day suits the mouth ex: 31 in January
     * every 4 year it make sure that it is allowing 29th in February
     * ex: 0/12/2020-->30/11/2020
     */
    private void resetToDateZero() {
        while (this.day < 0) {

            if (this.month == 1 || this.month == 3 || this.month == 5 || this.month == 7 || this.month == 8 || this.month == 10 || this.month == 12) {
                this.month--;
                this.day = 31;
            } else {
                if (this.month == 4 || this.month == 6 || this.month == 9 || this.month == 11) {
                    this.month--;
                    this.day = 30;
                } else {
                    if (this.month == 2)
                        if ((this.year) % 4 == 0) {
                            this.month--;
                            this.day = 29;
                        } else {
                            this.month--;
                            this.day = 28;
                        }
                }
            }
            if (this.month == 0) {
                this.year--;
                this.month = 12;
            }
        }
    }

    /**
     * takes witch moth you are in and returns the amount of days
     * @param month the moth that you are in
     * @return number of days
     * for ex: if you are in December (12) it will return 365
     */
    private int convertMonthIntoDays(int month){
        int days=0;
        while (month>0) {
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                days += 31;
            } else {
                if (month == 4 || month == 6 || month == 9 || month == 11) {
                    days += 30;
                } else {
                    if (month == 2)
                        days += 28;
                }
            }
            month--;
        }
        return days;
    }

    /**
     * this function compere 2 dates if the date is after the date it will return true
     * @param dateClass the date to compere
     * @return bool
     */
    public Boolean after(@NotNull DateClass dateClass){
        int compere= 365*this.year;
        int current = 365*dateClass.year;
        compere += this.year/4;
        current +=dateClass.year/4;
        compere+= this.day;
        current+= dateClass.day;
        compere+=convertMonthIntoDays(this.month);
        current+=convertMonthIntoDays(dateClass.month);
        if(compere<current)
        {
            return true;
        }
        return false;
    }

    /**
     * Those 2 function are using after function just adding days/mounts depends what the user wants
     * we are decries because we cant increase the current month because we don't want to change the value
     */
    public Boolean afterDay(@NotNull DateClass dateClass,int plusDays){
        return after(new DateClass(dateClass.year,dateClass.month,dateClass.day-plusDays));
    }
    public Boolean afterMonth(@NotNull DateClass dateClass, int plusMonth){
        return after(new DateClass(dateClass.year,dateClass.month-plusMonth,(dateClass.day+1)));
    }
    public String getDate() {
        return day +
                "/" + month +
                "/" + year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
        resetToDate();
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
        resetToDate();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public @NotNull String toString() {
        return "DateClass{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}
