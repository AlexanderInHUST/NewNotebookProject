package com.task.tangyifeng.newnotebookproject.model.model_class.SQLDataBase;

import android.content.Context;

import com.task.tangyifeng.newnotebookproject.model.greenDao_class.DaoMaster;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.DaoSession;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.Note;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.NoteDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangyifeng on 16/7/21.
 */
public class NoteDataBase extends DataBase {

    Context context;
    DaoMaster noteDaoMaster;
    DaoMaster.DevOpenHelper noteHelper;
    DaoSession noteSession;
    NoteDao noteDao;
    QueryBuilder<Note> noteBuilder;

    public NoteDataBase(Context context){
        setContext(context);
        openADataBase();
        noteSession = noteDaoMaster.newSession();
        noteDao = noteSession.getNoteDao();
    }

    @Override
    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public void openADataBase(){
        noteHelper = new DaoMaster.DevOpenHelper(context, "note_book_project_db");
        noteDaoMaster = new DaoMaster(noteHelper.getWritableDatabase());
    }

    @Override
    public List<Note> getAllData(){
        return noteDao.loadAll();
    }

    @Override
    public List<com.task.tangyifeng.newnotebookproject.model.model_class.Note> getAllEntities(){
        List<Note> notes = getAllData();
        ArrayList<com.task.tangyifeng.newnotebookproject.model.model_class.Note> notez = new ArrayList<>();
        for(Note note : notes){
            if(!note.getIsDel()) {
                com.task.tangyifeng.newnotebookproject.model.model_class.Note not = new com.task.tangyifeng.newnotebookproject.model.model_class.Note
                        (note.getTitle(), note.getHtml(), note.getIsAlarm(), note.getIsSync());
                not.setId(note.getId());
                not.setNotebookName(note.getNotebookName());
                not.setAccount(note.getAccount());
                notez.add(not);
            }
        }
        return notez;
    }

    @Override
    public List<com.task.tangyifeng.newnotebookproject.model.model_class.Note> queryEntities(String[] noteId){
        List<Note> notes = queryData(noteId);
        ArrayList<com.task.tangyifeng.newnotebookproject.model.model_class.Note> notez = new ArrayList<>();
        for(Note note : notes){
            if(!note.getIsDel()) {
                com.task.tangyifeng.newnotebookproject.model.model_class.Note not = new com.task.tangyifeng.newnotebookproject.model.model_class.Note
                        (note.getTitle(), note.getHtml(), note.getIsAlarm(), note.getIsSync());
                not.setId(note.getId());
                not.setNotebookName(note.getNotebookName());
                not.setAccount(note.getAccount());
                notez.add(not);
            }
        }
        return notez;
    }


    @Override
    public List<Note> queryData(String[] noteId){
        noteBuilder = noteSession.queryBuilder(Note.class);
        noteBuilder.where(NoteDao.Properties.Id.eq(Long.parseLong(noteId[0])));
        return noteBuilder.list();
    }

    @Override
    public void updateData(Object object){
        com.task.tangyifeng.newnotebookproject.model.model_class.Note note = (com.task.tangyifeng.newnotebookproject.model.model_class.Note) object;
        List<Note> notes = queryData(new String[]{Long.toString(note.getId())});
        Note newNote = new Note(note.getId(), note.getTitle(), note.getRichTextHtml(), note.getNotebookName(), note.getAccount(), note.getIsSync(), note.getIsAlarm(), false);
        if(notes.isEmpty()){
            noteDao.insert(newNote);
        }else{
            noteDao.update(newNote);
        }
    }

    @Override
    public boolean deleteData(Object object){
        com.task.tangyifeng.newnotebookproject.model.model_class.Note note = (com.task.tangyifeng.newnotebookproject.model.model_class.Note) object;
        List<Note> notes = queryData(new String[]{Long.toString(note.getId())});
        if(notes.isEmpty()){
            return false;
        }else{
            Note delNote = notes.get(0);
            noteDao.delete(delNote);
            return true;
        }
    }

    @Override
    public void setIsDel(String[] noteId){
        List<Note> list = queryData(noteId);
        Note note = new Note(list.get(0).getId(), list.get(0).getTitle(), list.get(0).getHtml(), list.get(0).getNotebookName(), list.get(0).getAccount(), list.get(0).getIsSync(), list.get(0).getIsAlarm(), true);
        noteDao.update(note);
    }

    @Override
    public List<Note> getAllDels(){
        noteBuilder = noteSession.queryBuilder(Note.class);
        noteBuilder.where(NoteDao.Properties.IsDel.eq(true));
        return noteBuilder.list();
    }

    @Override
    public int deleteAllDel(){
        List<Note> dels;
        int count = 0;
        dels = getAllDels();
        for(Note note : dels){
            com.task.tangyifeng.newnotebookproject.model.model_class.Note n = new com.task.tangyifeng.newnotebookproject.model.model_class.Note(null, null, null, null);
            n.setId(note.getId());
            deleteData(n);
            count ++;
        }
        return count;
    }

    public List<com.task.tangyifeng.newnotebookproject.model.model_class.Note> getNotebookNoteEntities(String notebookName){
        noteBuilder = noteSession.queryBuilder(Note.class);
        noteBuilder.where(NoteDao.Properties.NotebookName.eq(notebookName));
        List<Note> list = noteBuilder.list();
        ArrayList<com.task.tangyifeng.newnotebookproject.model.model_class.Note> entities = new ArrayList<>();
        for(Note note : list){
            if(!note.getIsDel()) {
                com.task.tangyifeng.newnotebookproject.model.model_class.Note n = new com.task.tangyifeng.newnotebookproject.model.model_class.Note
                        (note.getTitle(), note.getHtml(), note.getIsAlarm(), note.getIsSync());
                n.setId(note.getId());
                n.setNotebookName(note.getNotebookName());
                n.setAccount(note.getAccount());
                entities.add(n);
            }
        }
        return entities;
    }

    public List<com.task.tangyifeng.newnotebookproject.model.model_class.Note> getNotSyncNoteEntities(){
        noteBuilder = noteSession.queryBuilder(Note.class);
        noteBuilder.where(NoteDao.Properties.IsSync.eq(false));
        List<Note> list = noteBuilder.list();
        ArrayList<com.task.tangyifeng.newnotebookproject.model.model_class.Note> entities = new ArrayList<>();
        for(Note note : list){
            if(!note.getIsDel()) {
                com.task.tangyifeng.newnotebookproject.model.model_class.Note n = new com.task.tangyifeng.newnotebookproject.model.model_class.Note
                        (note.getTitle(), note.getHtml(), note.getIsAlarm(), note.getIsSync());
                n.setId(note.getId());
                n.setNotebookName(note.getNotebookName());
                n.setAccount(note.getAccount());
                entities.add(n);
            }
        }
        return entities;
    }

    public List<com.task.tangyifeng.newnotebookproject.model.model_class.Note> getAccountNoteEntities(String account){
        noteBuilder = noteSession.queryBuilder(Note.class);
        noteBuilder.where(NoteDao.Properties.Account.eq(account));
        List<Note> list = noteBuilder.list();
        ArrayList<com.task.tangyifeng.newnotebookproject.model.model_class.Note> entities = new ArrayList<>();
        for(Note note : list){
            if(!note.getIsDel()) {
                com.task.tangyifeng.newnotebookproject.model.model_class.Note n = new com.task.tangyifeng.newnotebookproject.model.model_class.Note
                        (note.getTitle(), note.getHtml(), note.getIsAlarm(), note.getIsSync());
                n.setAccount(account);
                n.setId(note.getId());
                n.setNotebookName(note.getNotebookName());
                entities.add(n);
            }
        }
        return entities;
    }

}
