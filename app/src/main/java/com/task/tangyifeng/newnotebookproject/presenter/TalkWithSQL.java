package com.task.tangyifeng.newnotebookproject.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.AdapterView;

import com.task.tangyifeng.newnotebookproject.model.model_class.SQLDataBase.AccountDataBase;
import com.task.tangyifeng.newnotebookproject.model.model_class.SQLDataBase.ClockDataBase;
import com.task.tangyifeng.newnotebookproject.model.model_class.SQLDataBase.DataBase;
import com.task.tangyifeng.newnotebookproject.model.model_class.SQLDataBase.NoteDataBase;
import com.task.tangyifeng.newnotebookproject.model.model_class.SQLDataBase.PictureDataBase;

import java.util.List;

/**
 * Created by tangyifeng on 16/7/21.
 */
public class TalkWithSQL {

    Context context;
    DataBase dataBase;

    public TalkWithSQL(Context context, String who){
        this.context = context;
        switch (who){
            case "account":{
                Log.d("TalkWithSQL","talk to account");
                dataBase = new AccountDataBase(context);
                break;
            }
            case "note":{
                Log.d("TalkWithSQL","talk to note");
                dataBase = new NoteDataBase(context);
                break;
            }
            case "picture":{
                Log.d("TalkWithSQL","talk to picture");
                dataBase = new PictureDataBase(context);
                break;
            }
            case "clock":{
                Log.d("TalkWithSQL","talk to clock");
                dataBase = new ClockDataBase(context);
                break;
            }
            default:{
                Log.e("TalkWithSQL","NO SUCH SQL.");
                break;
            }
        }
    }

    public void updateData(Object data){
        dataBase.updateData(data);
    }

    public boolean deleteData(Object data){
        if(dataBase.deleteData(data))
            return true;
        else{
            Log.d("deleteData","no such data");
            return false;
        }
    }

    public List< ? extends Object > queryData(String... conditions){
        return dataBase.queryData(conditions);
    }

    public List< ? extends Object > getAllData(){
        return dataBase.getAllData();
    }

    public List< ? extends Object> getAllEntities(){
        return dataBase.getAllEntities();
    }

    public List< ? extends Object> queryEntities(String... conditions){
        return dataBase.queryEntities(conditions);
    }

    public List< ? extends Object > getAllDels(){
        return dataBase.getAllDels();
    }

    public List< ? extends Object > specialTalkWithNoteForNotebook(String notebookName){
        return ((NoteDataBase) dataBase).getNotebookNoteEntities(notebookName);
    }

    public List< ? extends Object > specialTalkWithNoteForNotSync(){
        return ((NoteDataBase)dataBase).getNotSyncNoteEntities();
    }

    public List< ? extends Object > specialTalkWithNoteForAccount(String account){
        return ((NoteDataBase)dataBase).getAccountNoteEntities(account);
    }

    public int deleteAllDels(){
        return dataBase.deleteAllDel();
    }

    public void setDels(String[] conditions){
        dataBase.setIsDel(conditions);
    }
}
