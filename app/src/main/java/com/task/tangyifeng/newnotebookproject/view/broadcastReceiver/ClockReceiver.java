package com.task.tangyifeng.newnotebookproject.view.broadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.task.tangyifeng.newnotebookproject.R;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.Clock;
import com.task.tangyifeng.newnotebookproject.model.model_class.Note;
import com.task.tangyifeng.newnotebookproject.presenter.TalkWithSQL;
import com.task.tangyifeng.newnotebookproject.view.activity.EditActivity;

import java.util.Calendar;
import java.util.List;

/**
 * Created by tangyifeng on 16/7/27.
 */
public class ClockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        Log.d("onReceive","got " + Calendar.getInstance().getTimeInMillis());
        Bundle bundle = intent.getExtras();
        Intent noteIntent = new Intent(context, EditActivity.class);
        noteIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, noteIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("一篇笔记有提醒")
                .setContentText("点击查看笔记内容")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("一篇笔记有提醒")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        Notification notification = builder.build();
        manager.notify(0, notification);
        new TalkWithSQL(context, "clock").setDels(new String[]{Long.toString(bundle.getLong("noteId"))});
        new TalkWithSQL(context, "clock").deleteAllDels();
        Note note = (Note) new TalkWithSQL(context, "note").queryEntities(new String[]{Long.toString(bundle.getLong("noteId"))}).get(0);
        note.setIsAlarm(false);
        new TalkWithSQL(context, "note").updateData(note);
    }

}
