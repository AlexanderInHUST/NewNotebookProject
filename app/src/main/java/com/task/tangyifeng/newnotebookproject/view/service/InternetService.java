package com.task.tangyifeng.newnotebookproject.view.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.task.tangyifeng.newnotebookproject.model.model_class.Account;
import com.task.tangyifeng.newnotebookproject.model.model_class.Note;
import com.task.tangyifeng.newnotebookproject.model.model_class.Picture;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Deleteable;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Downloadable;
import com.task.tangyifeng.newnotebookproject.presenter.SyncPictures;
import com.task.tangyifeng.newnotebookproject.presenter.SyncWithCloud;
import com.task.tangyifeng.newnotebookproject.presenter.TalkWithSQL;
import com.task.tangyifeng.newnotebookproject.view.activity.EditActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tangyifeng on 16/7/22.
 */
public class InternetService extends Service {

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId){
        new SyncWithCloud((Deleteable) new Note(), InternetService.this).delete();
        new SyncWithCloud((Deleteable) new Picture(), InternetService.this).delete();
        new SyncWithCloud((Deleteable) new Account(), InternetService.this).delete();
        new TalkWithSQL(InternetService.this, "note").deleteAllDels();
        new TalkWithSQL(InternetService.this, "picture").deleteAllDels();
        new TalkWithSQL(InternetService.this, "account").deleteAllDels();
        new SyncWithCloud((Downloadable) new Note(), InternetService.this).download();
        new SyncWithCloud((Downloadable) new Picture(), InternetService.this).download();
        new SyncWithCloud((Downloadable) new Account(), InternetService.this).download();
        for(Note note : (List<Note>) new TalkWithSQL(InternetService.this, "note").specialTalkWithNoteForNotSync()){
            note.setIsSync(true);
            new SyncWithCloud(note).upload();
            new SyncPictures(InternetService.this).upload(note);
            new TalkWithSQL(InternetService.this, "note").updateData(note);
        }
        for(Picture picture : (List<Picture>) new TalkWithSQL(InternetService.this, "picture").getAllEntities()){
            new SyncWithCloud(picture).upload();
        }
        for(Account account : (List<Account>) new TalkWithSQL(InternetService.this, "account").getAllEntities()){
            new SyncWithCloud(account).upload();
        }
        return super.onStartCommand(intent, flag, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
