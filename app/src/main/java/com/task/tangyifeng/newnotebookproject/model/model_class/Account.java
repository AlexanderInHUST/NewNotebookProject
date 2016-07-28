package com.task.tangyifeng.newnotebookproject.model.model_class;

import android.content.Context;
import android.text.TextUtils;

import com.task.tangyifeng.newnotebookproject.model.greenDao_class.AccountBook;
import com.task.tangyifeng.newnotebookproject.model.leanCloud_class.subAVObject.CloudAccount;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Deleteable;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Downloadable;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Editable;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Uploadable;
import com.task.tangyifeng.newnotebookproject.presenter.GetResultOfDownload;
import com.task.tangyifeng.newnotebookproject.presenter.TalkWithSQL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangyifeng on 16/7/18.
 */
public class Account implements Editable, Downloadable, Uploadable, Deleteable {

    private String account;
    private String password;
    private CloudAccount self;
    private List<CloudAccount> downloadData;
    ArrayList<Account> accounts;
    private Context context;

    public Account(){

    }

    public Account(String account, String password){
        setAccount(account);
        setPassword(password);
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    public void setAccount(String account){
        this.account = account;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getAccount(){
        return account;
    }

    public String getPassword(){
        return password;
    }

    public Context getContext(){
        return context;
    }

    @Override
    public void uploadSelfFirst(){
        new CloudAccount().initialUpload(this.getAccount(), this);
    }

    @Override
    public void uploadSelfSecond(){
        self.put("account",this.getAccount());
        self.put("password",this.getPassword());
        new CloudAccount().upload(self, this);
    }

    @Override
    public void downloadSelfFirst(){
        if(downloadData == null) {
            CloudAccount cloudAccount = new CloudAccount();
            cloudAccount.setContext(this.getContext());
            cloudAccount.initialDownLoad(this);
        }else{
            new GetResultOfDownload(context).showResult(accounts);
        }
    }

    @Override
    public void downloadSelfSecond(){
        accounts = new ArrayList<>();
        for(CloudAccount cloudAccount : downloadData){
            Account acc = new Account(cloudAccount.getString("account"), cloudAccount.getString("password"));
            accounts.add(acc);
        }
        downloadSelfFirst();
    }

    @Override
    public void deleteDelsFirst(){
        List<AccountBook> accountBooks = (List<AccountBook>) new TalkWithSQL(context, "account").getAllDels();
        new CloudAccount().initialDeleteDels(accountBooks);
    }

    public void initialSelfForUploading(String objectId){
        self = new CloudAccount();
        if(!TextUtils.isEmpty(objectId)){
            self.setObjectId(objectId);
        }
        uploadSelfSecond();
    }

    public void initialSelfForDownloading(List<CloudAccount> downloadData){
        this.downloadData = downloadData;
        downloadSelfSecond();
    }

    public void setSelfObjectId(String objectId){
        self.setObjectId(objectId);
    }

}
