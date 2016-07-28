package com.task.tangyifeng.newnotebookproject.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.task.tangyifeng.newnotebookproject.R;
import com.task.tangyifeng.newnotebookproject.model.model_class.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by tangyifeng on 16/7/19.
 */
public class AdaptNoteListView {

    Context context;
    ListView noteListView;
    ArrayList<Note> notes;
    List<Map<String, Object>> adapterList;
    SimpleAdapter adapter;

    public AdaptNoteListView(Context context, ListView noteListView, ArrayList<Note> notes){
        this.context = context;
        this.noteListView = noteListView;
        this.notes = notes;
        adapterList = new LinkedList<>();
    }

    public void adapt(){
        // FIXME: 16/7/26 
        for(Note n : notes){
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("title", n.getTitle());
            tempMap.put("content", n.getContent());
            tempMap.put("isAlarm", (n.getIsAlarm()) ? R.drawable.ic_alarm_black_24dp : R.drawable.ic_alarm_black_24dp_unable);
            tempMap.put("isSync", (n.getIsSync()) ? R.drawable.ic_cloud_queue_black_24dp : R.drawable.ic_cloud_black_24dp_unable);
            adapterList.add(tempMap);
        }
        adapter = new SimpleAdapter(context, adapterList, R.layout.note_listview_main,
                new String[]{"title", "content", "isAlarm", "isSync"},
                new int[]{R.id.note_listview_title, R.id.note_listview_description, R.id.note_listview_alarm, R.id.note_listview_cloud});
        noteListView.setAdapter(adapter);
    }

}

