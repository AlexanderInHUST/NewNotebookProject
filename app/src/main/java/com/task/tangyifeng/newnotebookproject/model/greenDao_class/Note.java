package com.task.tangyifeng.newnotebookproject.model.greenDao_class;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by tangyifeng on 16/7/21.
 */

@Entity
public class Note {
    @Id(autoincrement = false)
    private Long id;
    private String title;
    private String html;
    private String notebookName;
    private String account;
    private boolean isSync;
    private boolean isAlarm;
    private Boolean isDel;
    public boolean getIsAlarm() {
        return this.isAlarm;
    }
    public void setIsAlarm(boolean isAlarm) {
        this.isAlarm = isAlarm;
    }
    public boolean getIsSync() {
        return this.isSync;
    }
    public void setIsSync(boolean isSync) {
        this.isSync = isSync;
    }
    public String getHtml() {
        return this.html;
    }
    public void setHtml(String html) {
        this.html = html;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNotebookName() {
        return this.notebookName;
    }
    public void setNotebookName(String notebookName) {
        this.notebookName = notebookName;
    }
    public Boolean getIsDel() {
        return this.isDel;
    }
    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    @Generated(hash = 1239987826)
    public Note(Long id, String title, String html, String notebookName, String account,
            boolean isSync, boolean isAlarm, Boolean isDel) {
        this.id = id;
        this.title = title;
        this.html = html;
        this.notebookName = notebookName;
        this.account = account;
        this.isSync = isSync;
        this.isAlarm = isAlarm;
        this.isDel = isDel;
    }
    @Generated(hash = 1272611929)
    public Note() {
    }
}
