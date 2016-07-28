package com.task.tangyifeng.newnotebookproject.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.task.tangyifeng.newnotebookproject.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by tangyifeng on 16/7/26.
 */
public class AdaptNotebookListView {

    Context context;
    ListView noteListView;
    ArrayList<String> notebooks;
    List<Map<String, Object>> adapterList;
    SimpleAdapter adapter;

    public AdaptNotebookListView(Context context, ListView noteListView, ArrayList<String> notebooks){
        this.context = context;
        this.noteListView = noteListView;
        this.notebooks = notebooks;
        adapterList = new LinkedList<>();
    }

    public void adapt(){
        Collections.sort(notebooks);
        for(String s : notebooks){
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("notebook",s);
            adapterList.add(tempMap);
        }
        adapter = new SimpleAdapter(context, adapterList, R.layout.change_notebook_listview_edit,
                new String[]{"notebook"},
                new int[]{R.id.change_notebook_listview_text_edit});
        noteListView.setAdapter(adapter);
    }

}
