package com.task.tangyifeng.newnotebookproject.model.model_class.SQLDataBase;

import android.content.Context;

import com.task.tangyifeng.newnotebookproject.model.model_interface.Downloadable;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Uploadable;

import java.util.List;

/**
 * Created by tangyifeng on 16/7/21.
 */
public abstract class DataBase{
    public abstract void openADataBase();
    public abstract void setContext(Context context);
    public abstract void updateData(Object data);
    public abstract boolean deleteData(Object data);
    public abstract void setIsDel(String... conditions);
    public abstract List< ? extends Object > getAllDels();
    public abstract int deleteAllDel();
    public abstract List< ? extends Object > queryData(String... conditions);
    public abstract List< ? extends Object > queryEntities(String... conditions);
    public abstract List< ? extends Object > getAllData();
    public abstract List< ? extends Object > getAllEntities();
}
