package com.task.tangyifeng.newnotebookproject.presenter;

import android.content.Context;

import com.task.tangyifeng.newnotebookproject.model.model_class.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangyifeng on 16/7/26.
 */
public class GetAllNotebooks {

    Context context;

    public GetAllNotebooks(Context context){
        this.context = context;
    }

    public ArrayList<String> get(String account){
        ArrayList<Note> notes = new ArrayList<>();
        ArrayList<String> notebooks = new ArrayList<>();
        notes =(ArrayList<Note>) new TalkWithSQL(context, "note").specialTalkWithNoteForAccount(account);
        for(Note note : notes){
            if(!notebooks.contains(note.getNotebookName()) && !note.getNotebookName().equals("DUSTBIN")){
                notebooks.add(note.getNotebookName());
            }
        }
        return notebooks;
    }

}
