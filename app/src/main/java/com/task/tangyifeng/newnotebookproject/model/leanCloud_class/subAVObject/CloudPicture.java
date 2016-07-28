package com.task.tangyifeng.newnotebookproject.model.leanCloud_class.subAVObject;

import android.content.Context;
import android.os.CpuUsageInfo;
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
import com.task.tangyifeng.newnotebookproject.model.model_class.Picture;
import com.task.tangyifeng.newnotebookproject.presenter.TalkWithSQL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangyifeng on 16/7/22.
 */

@AVClassName("Picture")
public class CloudPicture extends AVObject{

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

    public void initialUpload(String picturePath, final Picture picture){
        AVQuery<CloudPicture> avQuery = new AVQuery<>("Picture");
        avQuery.whereEqualTo("picturePath", picturePath);
        avQuery.getFirstInBackground(new GetCallback<CloudPicture>() {
            @Override
            public void done(CloudPicture cloudPicture, AVException e) {
                if(cloudPicture != null)
                    picture.initialSelfForUploading(cloudPicture.getObjectId());
                else
                    picture.initialSelfForUploading(null);
            }
        });
    }

    public void initialDownLoad(final Picture picture){
        AVQuery<CloudPicture> aq;
        AVQuery<CloudPicture> avQuery = new AVQuery<>("Picture");
        ArrayList<AVQuery<CloudPicture>> conditions = new ArrayList<>();
        ArrayList<com.task.tangyifeng.newnotebookproject.model.greenDao_class.Picture> list = (ArrayList<com.task.tangyifeng.newnotebookproject.model.greenDao_class.Picture>) new TalkWithSQL(context, "picture").getAllData();
        for(com.task.tangyifeng.newnotebookproject.model.greenDao_class.Picture pic : list){
            AVQuery<CloudPicture> condition = new AVQuery<>("Picture");
            condition.whereEqualTo("picturePath", pic.getPicturePath());
            conditions.add(condition);
            Log.d("add condition",condition.toString());
        }
        if(conditions.isEmpty()){
            avQuery.whereExists("picturePath");
        }else {
            aq = AVQuery.or(conditions);
            avQuery.whereDoesNotMatchKeyInQuery("picturePath","picturePath", aq);
            Log.d("avQuery",avQuery.toString());
        }
        avQuery.findInBackground(new FindCallback<CloudPicture>() {
            @Override
            public void done(List<CloudPicture> list, AVException e) {
                picture.initialSelfForDownloading(list);
            }
        });
    }

    public void initialDeleteDels(List<com.task.tangyifeng.newnotebookproject.model.greenDao_class.Picture> list){
        AVQuery<CloudPicture> avQuery = new AVQuery<>("Picture");
        for(com.task.tangyifeng.newnotebookproject.model.greenDao_class.Picture pic : list){
            avQuery.whereEqualTo("picturePath", pic.getPicturePath());
            avQuery.getFirstInBackground(new GetCallback<CloudPicture>() {
                @Override
                public void done(CloudPicture cloudPicture, AVException e) {
                    if(cloudPicture != null)
                        cloudPicture.deleteInBackground();
                }
            });
        }
    }

    public void upload(final CloudPicture cloudPicture, final Picture picture){
        cloudPicture.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                picture.setSelfObjectId(cloudPicture.getObjectId());
            }
        });
    }

}
