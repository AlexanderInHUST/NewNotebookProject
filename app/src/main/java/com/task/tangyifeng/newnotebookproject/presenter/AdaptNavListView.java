package com.task.tangyifeng.newnotebookproject.presenter;

import android.content.Context;
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
 * Created by tangyifeng on 16/7/24.
 */
public class AdaptNavListView {

    Context context;
    ListView noteListView;
    ArrayList<String> notebooks;
    List<Map<String, Object>> adapterList;
    SimpleAdapter adapter;

    public AdaptNavListView(Context context, ListView noteListView, ArrayList<String> notebooks){
        this.context = context;
        this.noteListView = noteListView;
        this.notebooks = notebooks;
        adapterList = new LinkedList<>();
    }

    public void adapt(){
        Collections.sort(notebooks);
        for(String n : notebooks){
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("notebookName", n);
            adapterList.add(tempMap);
        }
        adapter = new SimpleAdapter(context, adapterList, R.layout.nav_listview_main,
                new String[]{"notebookName"},
                new int[]{R.id.nav_list_view_notebook_name});
        noteListView.setAdapter(adapter);
    }
}
