package com.task.tangyifeng.newnotebookproject.view.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.task.tangyifeng.newnotebookproject.presenter.TranslateTime;
import com.task.tangyifeng.newnotebookproject.presenter.TurnOnTheClocks;

/**
 * Created by tangyifeng on 16/7/27.
 */
public class TurnOnReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        new TurnOnTheClocks(context).turnOn();
    }

}
