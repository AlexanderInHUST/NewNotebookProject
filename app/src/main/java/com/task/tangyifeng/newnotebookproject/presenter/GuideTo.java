package com.task.tangyifeng.newnotebookproject.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by tangyifeng on 16/7/18.
 */
public class GuideTo {

    Activity from;

    Intent intent;

    public GuideTo(Activity from, Class< ? extends Context> to, Bundle data){
        this.from = from;
        intent = new Intent(from, to);
        if(data != null) {
            intent.putExtras(data);
            Log.d("bundle", data.toString());
        }
    }

    public void guideToActivity(){
        Log.d("guide from" ,"" + from);
        from.startActivity(intent);
    }

    public void guideToService(){
        Log.d("guide from" ,"" + from);
        from.startService(intent);
    }

}
