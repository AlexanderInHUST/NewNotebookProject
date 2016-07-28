package com.task.tangyifeng.newnotebookproject;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.task.tangyifeng.newnotebookproject.presenter.InitialCloudClass;

/**
 * Created by tangyifeng on 16/7/22.
 */
public class LeanCloud extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        InitialCloudClass.initial(this);
        AVOSCloud.initialize(this,"4oEw1go5NsyktiMF6Tz7W4z9-gzGzoHsz","KKsdAcmgWJC3fjzmg7aOdN6S");
    }

}
