package com.task.tangyifeng.newnotebookproject.model.greenDao_class;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by tangyifeng on 16/7/21.
 */

@Entity
public class Picture {
    @Id(autoincrement = true) private Long id;
    private String url;
    private String picturePath;
    private Boolean isDel;
    public String getPicturePath() {
        return this.picturePath;
    }
    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
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
    public Boolean getIsDel() {
        return this.isDel;
    }
    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }
    @Generated(hash = 442156768)
    public Picture(Long id, String url, String picturePath, Boolean isDel) {
        this.id = id;
        this.url = url;
        this.picturePath = picturePath;
        this.isDel = isDel;
    }
    @Generated(hash = 1602548376)
    public Picture() {
    }
}
