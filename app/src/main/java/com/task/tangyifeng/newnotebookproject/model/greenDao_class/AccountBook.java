package com.task.tangyifeng.newnotebookproject.model.greenDao_class;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by tangyifeng on 16/7/21.
 */

@Entity
public class AccountBook {
    @Id(autoincrement = true)
    private Long id;
    private String account;
    private String password;
    private Boolean isDel;
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
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
    @Generated(hash = 1231004188)
    public AccountBook(Long id, String account, String password, Boolean isDel) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.isDel = isDel;
    }
    @Generated(hash = 1809087649)
    public AccountBook() {
    }
}
