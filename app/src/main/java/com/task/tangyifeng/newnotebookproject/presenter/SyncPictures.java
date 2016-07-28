package com.task.tangyifeng.newnotebookproject.presenter;

import android.content.Context;

import com.task.tangyifeng.newnotebookproject.model.leanCloud_class.subAVObject.CloudFile;
import com.task.tangyifeng.newnotebookproject.model.model_class.Note;

/**
 * Created by tangyifeng on 16/7/25.
 */
public class SyncPictures {

    Context context;
    CloudFile cloudFile;

    public SyncPictures(Context context){
        this.context = context;
        cloudFile = new CloudFile(context);
        cloudFile.initialDataBase();
    }

    public void upload(Note note){
        cloudFile.uploadFile(note);
    }

    public void download(Note note){
        cloudFile.downloadFile(note);
    }

}
