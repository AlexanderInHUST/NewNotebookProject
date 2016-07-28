package com.task.tangyifeng.newnotebookproject.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.task.tangyifeng.newnotebookproject.R;
import com.task.tangyifeng.newnotebookproject.model.model_class.Note;
import com.task.tangyifeng.newnotebookproject.presenter.AdaptNoteListView;
import com.task.tangyifeng.newnotebookproject.presenter.GuideTo;
import com.task.tangyifeng.newnotebookproject.presenter.TalkWithSQL;

import java.util.ArrayList;

public class NotebookActivity extends AppCompatActivity {

    private ArrayList<Note> notes = new ArrayList<>();
    private int selectNotePos;
    private String account;

    private Toolbar toolbar;
    private ListView listView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        listView = (ListView)findViewById(R.id.notebook_listview);
        listView.setOnItemClickListener(adapterListener);
        listView.setOnItemLongClickListener(adapterLongClickListener);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initialText();

    }

    private AdapterView.OnItemClickListener adapterListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Bundle data = new Bundle();
            data.putLong("noteId",notes.get(i).getId());
            new GuideTo(NotebookActivity.this, EditActivity.class, data).guideToActivity();
        }
    };

    private AdapterView.OnItemLongClickListener adapterLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectNotePos = i;
            return false;
        }
    };

    private void initialText(){
        Bundle bundle = getIntent().getExtras();
        account = bundle.getString("account");
        String notebookName = bundle.getString("notebookName");
        ArrayList<Note> tempNotes = (ArrayList<Note>) new TalkWithSQL(NotebookActivity.this, "note").specialTalkWithNoteForNotebook(notebookName);
        notes = new ArrayList<>();
        for(Note note : tempNotes){
            if(note.getAccount().equals(account))
                notes.add(note);
        }
        toolbar.setTitle((notebookName.equals("DUSTBIN")) ? "垃圾篓" : notebookName);
        if(notebookName.equals("DUSTBIN"))
            registerForContextMenu(listView);
        new AdaptNoteListView(NotebookActivity.this, listView, notes).adapt();
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo){
        contextMenu.setHeaderTitle("编辑");
        contextMenu.add(0, 1, Menu.NONE, "彻底删除");
        contextMenu.add(0, 2, Menu.NONE, "恢复到默认笔记本中");
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case 1:{
                new TalkWithSQL(NotebookActivity.this, "note").setDels(new String[]{Long.toString(notes.get(selectNotePos).getId())});
                break;
            }
            case 2:{
                notes.get(selectNotePos).setNotebookName("默认记事本");
                new TalkWithSQL(NotebookActivity.this, "note").updateData(notes.get(selectNotePos));
                break;
            }
        }
        initialText();
        return true;
    }

}
