package com.task.tangyifeng.newnotebookproject.presenter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.task.tangyifeng.newnotebookproject.model.greenDao_class.Clock;

import java.util.ArrayList;

/**
 * Created by tangyifeng on 16/7/28.
 */
public class TurnOnTheClocks {

    AlarmManager alarmManager;
    Context context;

    public TurnOnTheClocks(Context context){
        this.context = context;
    }

    public void turnOn() {
        ArrayList<Clock> clocks = (ArrayList<Clock>) new TalkWithSQL(context, "clock").getAllData();
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for (int i = 0; i < clocks.size(); i++) {
            Intent broadcastIntent = new Intent("com.task.tangyifeng.newnotebookproject.CLOCK");
            Bundle bundle = new Bundle();
            bundle.putLong("noteId", clocks.get(i).getNoteId());
            broadcastIntent.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, broadcastIntent, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, clocks.get(i).getTime(), pendingIntent);
            Log.d("clockService", "start " + i + " " + clocks.get(i).getTime());
        }

    }
}
