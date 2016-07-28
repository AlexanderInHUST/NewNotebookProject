package com.task.tangyifeng.newnotebookproject.presenter;

import android.content.Context;

import com.task.tangyifeng.newnotebookproject.model.model_interface.Deleteable;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Downloadable;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Uploadable;

import java.util.ArrayList;

/**
 * Created by tangyifeng on 16/7/18.
 */
public class SyncWithCloud {

    Uploadable uploadable;
    Downloadable downloadable;
    Deleteable deleteable;

    public SyncWithCloud(Uploadable uploadable){
        this.uploadable = uploadable;
    }

    public SyncWithCloud(Deleteable deleteable, Context context){
        this.deleteable = deleteable;
        deleteable.setContext(context);
    }

    public SyncWithCloud(Downloadable downloadable, Context context){
        this.downloadable = downloadable;
        downloadable.setContext(context);
    }

    public void upload(){
        uploadable.uploadSelfFirst();
    }

    public void download(){
        downloadable.downloadSelfFirst();
    }

    public void delete(){
        deleteable.deleteDelsFirst();
    }

}
