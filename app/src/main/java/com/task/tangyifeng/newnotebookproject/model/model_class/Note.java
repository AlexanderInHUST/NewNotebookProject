package com.task.tangyifeng.newnotebookproject.model.model_class;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.task.tangyifeng.newnotebookproject.model.leanCloud_class.subAVObject.CloudAccount;
import com.task.tangyifeng.newnotebookproject.model.leanCloud_class.subAVObject.CloudNote;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Deleteable;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Downloadable;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Editable;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Uploadable;
import com.task.tangyifeng.newnotebookproject.presenter.GetResultOfDownload;
import com.task.tangyifeng.newnotebookproject.presenter.TalkWithSQL;
import com.task.tangyifeng.newnotebookproject.presenter.ToHtmlOrSpanned;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tangyifeng on 16/7/18.
 */
public class Note implements Editable, Uploadable, Downloadable, Deleteable {

    private Long id;
    private String title;
    private Boolean isAlarm;
    private Boolean isSync;
    private String notebookName;
    private String richTextHtml;
    private String account;
    private CloudNote self;
    private List<CloudNote> downloadData;
    ArrayList<Note> notes;
    private Context context;

    public Note(){ }

    public Note(String title, String richTextHtml, Boolean isAlarm, Boolean isSync){
        Random rand = new Random();
        setTitle(title);
        setRichTextHtml(richTextHtml);
        setIsAlarm(isAlarm);
        setIsSync(isSync);
        setNotebookName("默认记事本");
        setId(SystemClock.currentThreadTimeMillis() + rand.nextLong());
    }

    @Override
    public void setContext(Context context){
        this.context = context;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setRichTextHtml(String richTextHtml){
        this.richTextHtml = richTextHtml;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setIsAlarm(Boolean isAlarm){
        this.isAlarm = isAlarm;
    }

    public void setIsSync(Boolean isSync){
        this.isSync = isSync;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setNotebookName(String notebookName){
        this.notebookName = notebookName;
    }

    public Context getContext(){
        return context;
    }

    public String getTitle(){
        return title;
    }

    public String getRichTextHtml(){
        return richTextHtml;
    }

    public String getAccount(){
        return account;
    }

    public String getContent(){
        return new ToHtmlOrSpanned().toSpannableStringBuilder(richTextHtml).toString();
    }

    public Boolean getIsAlarm(){
        return isAlarm;
    }

    public Boolean getIsSync(){
        return isSync;
    }

    public Long getId(){
        return id;
    }

    public String getNotebookName(){
        return notebookName;
    }

    @Override
    public void uploadSelfFirst(){
        new CloudNote().initialUpload(this.getId(), this);
    }

    @Override
    public void uploadSelfSecond(){
        this.setIsSync(true);
        self.put("noteId",this.getId());
        self.put("notebookName",this.getNotebookName());
        self.put("title",this.getTitle());
        self.put("richTextHtml",this.getRichTextHtml());
        self.put("isAlarm",this.getIsAlarm());
        self.put("account",this.getAccount());
        new CloudNote().upload(self, this);
    }

    @Override
    public void downloadSelfFirst(){
        if(downloadData == null) {
            CloudNote cloudNote = new CloudNote();
            cloudNote.setContext(this.getContext());
            cloudNote.initialDownLoad(this);
        }else{
            new GetResultOfDownload(context).showResult(notes);
        }
    }

    @Override
    public void downloadSelfSecond(){
        notes = new ArrayList<>();
        if(downloadData == null)
            return;
        for(CloudNote cloudNote : downloadData){
            Note note = new Note(cloudNote.getString("title"), cloudNote.getString("richTextHtml"), cloudNote.getBoolean("isAlarm"), true);
            note.setId(cloudNote.getLong("noteId"));
            note.setNotebookName(cloudNote.getString("notebookName"));
            note.setAccount(cloudNote.getString("account"));
            notes.add(note);
        }
        downloadSelfFirst();
    }

    @Override
    public void deleteDelsFirst(){
        List<com.task.tangyifeng.newnotebookproject.model.greenDao_class.Note> notez = (List<com.task.tangyifeng.newnotebookproject.model.greenDao_class.Note>) new TalkWithSQL(context, "note").getAllDels();
        new CloudNote().initialDeleteDels(notez);
    }

    public void initialSelfForUploading(String objectId){
        self = new CloudNote();
        if(!TextUtils.isEmpty(objectId)){
            self.setObjectId(objectId);
        }
        uploadSelfSecond();
    }

    public void initialSelfForDownloading(List<CloudNote> downloadData){
        this.downloadData = downloadData;
        downloadSelfSecond();
    }

    public void setSelfObjectId(String objectId){
        self.setObjectId(objectId);
    }

}
