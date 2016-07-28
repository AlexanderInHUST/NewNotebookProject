package com.task.tangyifeng.newnotebookproject.model.leanCloud_class.subAVObject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.task.tangyifeng.newnotebookproject.model.model_class.Note;
import com.task.tangyifeng.newnotebookproject.presenter.TalkWithSQL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangyifeng on 16/7/22.
 */

@AVClassName("Note")
public class CloudNote extends AVObject{

    private Context context;

    public static final Parcelable.Creator<Object> CREATOR= new Creator<Object>(){
        @Override
        public Object createFromParcel(Parcel source){
            return null;
        }
        @Override
        public Object[] newArray(int size){
            return null;
        }
    };

    public void setContext(Context context){
        this.context = context;
    }

    public void initialUpload(Long noteId, final Note note){
        AVQuery<CloudNote> avQuery = new AVQuery<>("Note");
        avQuery.whereEqualTo("noteId", noteId);
        avQuery.getFirstInBackground(new GetCallback<CloudNote>() {
            @Override
            public void done(CloudNote cloudNote, AVException e) {
                if(e != null)
                    e.printStackTrace();
                if(cloudNote != null)
                    note.initialSelfForUploading(cloudNote.getObjectId());
                else
                    note.initialSelfForUploading(null);
            }
        });
    }

    public void initialDownLoad(final Note note){
        AVQuery<CloudNote> aq;
        AVQuery<CloudNote> avQuery = new AVQuery<>("Note");
        ArrayList<AVQuery<CloudNote>> conditions = new ArrayList<>();
        ArrayList<com.task.tangyifeng.newnotebookproject.model.greenDao_class.Note> list = (ArrayList<com.task.tangyifeng.newnotebookproject.model.greenDao_class.Note>) new TalkWithSQL(context, "note").getAllData();
        for(com.task.tangyifeng.newnotebookproject.model.greenDao_class.Note n : list){
            AVQuery<CloudNote> condition = new AVQuery<>("Note");
            condition.whereEqualTo("noteId", n.getId());
            conditions.add(condition);
            Log.d("add condition",condition.toString());
        }
        if(conditions.isEmpty()){
            avQuery.whereExists("noteId");
        }else {
            aq = AVQuery.or(conditions);
            avQuery.whereDoesNotMatchKeyInQuery("noteId","noteId", aq);
            Log.d("avQuery",avQuery.toString());
        }
        avQuery.findInBackground(new FindCallback<CloudNote>() {
            @Override
            public void done(List<CloudNote> list, AVException e) {
                note.initialSelfForDownloading(list);
            }
        });
    }

    public void initialDeleteDels(List<com.task.tangyifeng.newnotebookproject.model.greenDao_class.Note> list){
        AVQuery<CloudNote> avQuery = new AVQuery<>("Note");
        for(com.task.tangyifeng.newnotebookproject.model.greenDao_class.Note note : list){
            avQuery.whereEqualTo("noteId", note.getId());
            avQuery.getFirstInBackground(new GetCallback<CloudNote>() {
                @Override
                public void done(CloudNote cloudNote, AVException e) {
                    if(cloudNote != null)
                      cloudNote.deleteInBackground();
                }
            });
        }
    }

    public void upload(final CloudNote cloudNote, final Note note){
        cloudNote.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e != null)
                    e.printStackTrace();
                note.setSelfObjectId(cloudNote.getObjectId());
                Log.d("upload note done","" + note.getId());
            }
        });
    }

}
