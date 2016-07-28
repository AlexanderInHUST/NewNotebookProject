package com.task.tangyifeng.newnotebookproject.model.leanCloud_class.subAVObject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.AccountBook;
import com.task.tangyifeng.newnotebookproject.model.model_class.Account;
import com.task.tangyifeng.newnotebookproject.presenter.TalkWithSQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tangyifeng on 16/7/22.
 */

@AVClassName("Account")
public class CloudAccount extends AVObject{

    Context context;

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

    public void initialUpload(String name, final Account account){
        AVQuery<CloudAccount> avQuery = new AVQuery<>("Account");
        avQuery.whereEqualTo("account", name);
        avQuery.getFirstInBackground(new GetCallback<CloudAccount>() {
            @Override
            public void done(CloudAccount cloudAccount, AVException e) {
                if(cloudAccount != null)
                    account.initialSelfForUploading(cloudAccount.getObjectId());
                else
                    account.initialSelfForUploading(null);
            }
        });
    }

    public void initialDownLoad(final Account account){
        AVQuery<CloudAccount> aq;
        AVQuery<CloudAccount> avQuery = new AVQuery<>("Account");
        ArrayList<AVQuery<CloudAccount>> conditions = new ArrayList<>();
        ArrayList<AccountBook> list = (ArrayList<AccountBook>) new TalkWithSQL(context, "account").getAllData();
        for(AccountBook accountBook : list){
            AVQuery<CloudAccount> condition = new AVQuery<>("Account");
            condition.whereEqualTo("account", accountBook.getAccount());
            conditions.add(condition);
            Log.d("add condition",condition.toString());
        }
        if(conditions.isEmpty()){
            avQuery.whereExists("account");
        }else {
            aq = AVQuery.or(conditions);
            avQuery.whereDoesNotMatchKeyInQuery("account","account", aq);
            Log.d("avQuery",avQuery.toString());
        }
        avQuery.findInBackground(new FindCallback<CloudAccount>() {
            @Override
            public void done(List<CloudAccount> list, AVException e) {
                account.initialSelfForDownloading(list);
            }
        });
    }

    public void initialDeleteDels(List<AccountBook> list){
        AVQuery<CloudAccount> avQuery = new AVQuery<>("Account");
        for(AccountBook accountBook : list){
            avQuery.whereEqualTo("account", accountBook.getAccount());
            avQuery.getFirstInBackground(new GetCallback<CloudAccount>() {
                @Override
                public void done(CloudAccount cloudAccount, AVException e) {
                    if(cloudAccount != null)
                        cloudAccount.deleteInBackground();
                }
            });
        }
    }

    public void upload(final CloudAccount cloudAccount, final Account account){
        cloudAccount.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                account.setSelfObjectId(cloudAccount.getObjectId());
            }
        });
    }

}
