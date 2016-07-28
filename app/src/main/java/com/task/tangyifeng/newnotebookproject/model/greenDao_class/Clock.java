package com.task.tangyifeng.newnotebookproject.model.greenDao_class;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by tangyifeng on 16/7/26.
 */
@Entity
public class Clock {
    @Id(autoincrement = true)
    private Long id;
    private Long time;
    private Long noteId;
    private Boolean isDel;
    public Long getNoteId() {
        return this.noteId;
    }
    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }
    public Long getTime() {
        return this.time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Boolean getIsDel() {
        return this.isDel;
    }
    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }
    @Generated(hash = 398059001)
    public Clock(Long id, Long time, Long noteId, Boolean isDel) {
        this.id = id;
        this.time = time;
        this.noteId = noteId;
        this.isDel = isDel;
    }
    @Generated(hash = 1588708936)
    public Clock() {
    }
}
