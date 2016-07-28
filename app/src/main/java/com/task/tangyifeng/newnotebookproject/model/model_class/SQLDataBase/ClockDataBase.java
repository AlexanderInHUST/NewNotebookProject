package com.task.tangyifeng.newnotebookproject.model.model_class.SQLDataBase;

import android.content.Context;

import com.task.tangyifeng.newnotebookproject.model.greenDao_class.Clock;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.ClockDao;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.DaoMaster;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangyifeng on 16/7/26.
 */
public class ClockDataBase extends DataBase {

    Context context;
    DaoMaster clockMaster;
    DaoMaster.DevOpenHelper clockHelper;
    DaoSession clockSession;
    ClockDao clockDao;
    QueryBuilder<Clock> clockBuilder;

    public ClockDataBase(Context context){
        setContext(context);
        openADataBase();
        clockSession = clockMaster.newSession();
        clockDao = clockSession.getClockDao();
    }

    @Override
    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public void openADataBase(){
        clockHelper = new DaoMaster.DevOpenHelper(context, "note_book_project_db");
        clockMaster = new DaoMaster(clockHelper.getWritableDatabase());
    }

    @Override
    public List<Clock> getAllData(){
        return clockDao.loadAll();
    }

    @Override
    public List<Clock> getAllEntities(){
        return getAllData();
    }

    @Override
    public List<Clock> queryEntities(String[] noteId){
        return queryData(noteId);
    }


    @Override
    public List<Clock> queryData(String[] noteId){
        clockBuilder = clockSession.queryBuilder(Clock.class);
        clockBuilder.where(ClockDao.Properties.NoteId.eq(Long.parseLong(noteId[0])));
        return clockBuilder.list();
    }

    @Override
    public void updateData(Object object){
        Clock clock = (Clock) object;
        if(queryData(new String[]{Long.toString(clock.getNoteId())}).isEmpty()){
            clockDao.insert(clock);
        }else{
            clockDao.update(queryData(new String[]{Long.toString(clock.getNoteId())}).get(0));
        }
    }

    @Override
    public boolean deleteData(Object object){
        Clock clock = (Clock) object;
        List<Clock> clocks = queryData(new String[]{Long.toString(clock.getNoteId())});
        if(clocks.isEmpty()){
            return false;
        }else{
            Clock delClock = clocks.get(0);
            clockDao.delete(delClock);
            return true;
        }
    }

    @Override
    public void setIsDel(String[] noteId){
        List<Clock> list = queryData(noteId);
        if(list.isEmpty())
            return;
        Clock clock = new Clock(list.get(0).getId(), list.get(0).getTime(), list.get(0).getNoteId(), true);
        clockDao.update(clock);
    }

    @Override
    public List<Clock> getAllDels(){
        clockBuilder = clockSession.queryBuilder(Clock.class);
        clockBuilder.where(ClockDao.Properties.IsDel.eq(true));
        return clockBuilder.list();
    }

    @Override
    public int deleteAllDel(){
        List<Clock> dels;
        int count = 0;
        dels = getAllDels();
        for(Clock clock : dels){
            deleteData(clock);
            count ++;
        }
        return count;
    }

}