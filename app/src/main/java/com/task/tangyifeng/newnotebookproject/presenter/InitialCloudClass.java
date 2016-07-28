package com.task.tangyifeng.newnotebookproject.presenter;

import android.content.Context;

import com.avos.avoscloud.AVObject;
import com.task.tangyifeng.newnotebookproject.model.leanCloud_class.subAVObject.CloudAccount;
import com.task.tangyifeng.newnotebookproject.model.leanCloud_class.subAVObject.CloudNote;
import com.task.tangyifeng.newnotebookproject.model.leanCloud_class.subAVObject.CloudPicture;

/**
 * Created by tangyifeng on 16/7/22.
 */
public class InitialCloudClass {

    public static void initial(Context context){
        AVObject.registerSubclass(CloudAccount.class);
        AVObject.registerSubclass(CloudNote.class);
        AVObject.registerSubclass(CloudPicture.class);

    }

}
