package com.task.tangyifeng.newnotebookproject.model.model_class;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.task.tangyifeng.newnotebookproject.model.leanCloud_class.subAVObject.CloudPicture;
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
public class Picture implements  Editable, Downloadable, Uploadable, Deleteable {

    private String url;
    private String picturePath;
    private CloudPicture self;
    private List<CloudPicture> downloadData;
    ArrayList<Picture> pictures;
    private Context context;

    public Picture(){

    }

    public Picture(String url, String picturePath){
        setUrl(url);
        setPicturePath(picturePath);
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setPicturePath(String picturePath){
        this.picturePath = picturePath;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public String getUrl(){
        return url;
    }

    public String getPicturePath(){
        return picturePath;
    }

    public Context getContext(){
        return context;
    }

    @Override
    public void uploadSelfFirst(){
        new CloudPicture().initialUpload(this.getPicturePath(), this);
    }

    @Override
    public void uploadSelfSecond(){
        self.put("url",this.getUrl());
        self.put("picturePath",this.getPicturePath());
        new CloudPicture().upload(self, this);
    }

    @Override
    public void downloadSelfFirst(){
        if(downloadData == null) {
            CloudPicture cloudPicture = new CloudPicture();
            cloudPicture.setContext(this.getContext());
            cloudPicture.initialDownLoad(this);
        }else{
            new GetResultOfDownload(context).showResult(pictures);
        }
    }

    @Override
    public void downloadSelfSecond(){
        pictures = new ArrayList<>();
        if(downloadData == null){
            Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
            return;
        }
        for(CloudPicture cloudPicture : downloadData){
            Picture pic = new Picture(cloudPicture.getString("url"), cloudPicture.getString("picturePath"));
            pictures.add(pic);
        }
        downloadSelfFirst();
    }

    @Override
    public void deleteDelsFirst(){
        List<com.task.tangyifeng.newnotebookproject.model.greenDao_class.Picture> pics = (List<com.task.tangyifeng.newnotebookproject.model.greenDao_class.Picture>) new TalkWithSQL(context, "picture").getAllDels();
        new CloudPicture().initialDeleteDels(pics);
    }

    public void initialSelfForUploading(String objectId){
        self = new CloudPicture();
        if(!TextUtils.isEmpty(objectId)){
            self.setObjectId(objectId);
        }
        uploadSelfSecond();
    }

    public void initialSelfForDownloading(List<CloudPicture> downloadData){
        this.downloadData = downloadData;
        downloadSelfSecond();
    }

    public void setSelfObjectId(String objectId){
        self.setObjectId(objectId);
    }


}
