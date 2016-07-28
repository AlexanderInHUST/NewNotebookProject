package com.task.tangyifeng.newnotebookproject.presenter;

import android.icu.util.TimeZone;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by tangyifeng on 16/7/28.
 */
public class TranslateTime {

    //Thu Jul 28 14:13:00 GMT+08:00 2016
    public static String translate(String epoch){
        StringBuilder builder = new StringBuilder();
        String[] time = epoch.split(" ");
        builder.append(time[5] + "-" + getMonth(time[1]) + "-" + time[2] + " " + time[3]);
        return builder.toString();
    }

    private static String getMonth(String month){
        String mon = null;
        switch (month){
            case "Jan":{
                mon = "1";
                break;
            }
            case "Feb":{
                mon = "2";
                break;
            }
            case "Mar":{
                mon = "3";
                break;
            }
            case "Apr":{
                mon = "4";
                break;
            }
            case "May":{
                mon = "5";
                break;
            }
            case "Jun":{
                mon = "6";
                break;
            }
            case "Jul":{
                mon = "7";
                break;
            }
            case "Aug":{
                mon = "8";
                break;
            }
            case "Sep":{
                mon = "9";
                break;
            }
            case "Oct":{
                mon = "10";
                break;
            }
            case "Nov":{
                mon = "11";
                break;
            }
            case "Dec":{
                mon = "12";
                break;
            }
        }
        return mon;
    }

}
