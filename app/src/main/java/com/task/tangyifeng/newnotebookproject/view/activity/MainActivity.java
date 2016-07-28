package com.task.tangyifeng.newnotebookproject.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.task.tangyifeng.newnotebookproject.R;
import com.task.tangyifeng.newnotebookproject.model.model_class.Note;
import com.task.tangyifeng.newnotebookproject.presenter.AdaptNavListView;
import com.task.tangyifeng.newnotebookproject.presenter.AdaptNoteListView;
import com.task.tangyifeng.newnotebookproject.presenter.CheckNetConnected;
import com.task.tangyifeng.newnotebookproject.presenter.GetAllNotebooks;
import com.task.tangyifeng.newnotebookproject.presenter.GuideTo;
import com.task.tangyifeng.newnotebookproject.presenter.TalkWithSQL;
import com.task.tangyifeng.newnotebookproject.presenter.TurnOnTheClocks;
import com.task.tangyifeng.newnotebookproject.view.service.InternetService;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{

    private ArrayList<Note> notes = new ArrayList<>();
    private ArrayList<String> notebookList;
    private int selectNotePos;
    private String account;

    private ListView noteListView;
    private ListView navListView;
    private TextView clockTextView;
    private TextView emailTextView;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private LinearLayout allLayout;
    private LinearLayout dustbinLayout;
    private LinearLayout cloudLayout;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Toast.makeText(MainActivity.this, "同步完成!", Toast.LENGTH_SHORT).show();
            flash();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("全部笔记");

        noteListView = (ListView)findViewById(R.id.main_note_list);
        registerForContextMenu(noteListView);

        navListView = (ListView)findViewById(R.id.nav_list_view);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("account",account);
                new GuideTo(MainActivity.this, EditActivity.class, bundle).guideToActivity();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        allLayout = (LinearLayout)findViewById(R.id.nav_all_layout_main);
        dustbinLayout = (LinearLayout)findViewById(R.id.nav_dustbin_layout_main);
        cloudLayout = (LinearLayout)findViewById(R.id.nav_cloud_layout_main);
        allLayout.setOnClickListener(this);
        dustbinLayout.setOnClickListener(this);
        cloudLayout.setOnClickListener(this);

        navigationView = (NavigationView)findViewById(R.id.nav_view);

        clockTextView = (TextView)findViewById(R.id.main_subhead_description);

        emailTextView = (TextView)findViewById(R.id.nav_account_e_mail);
    }

    @Override
    protected void onResume(){
        super.onResume();
        flash();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_delete_main) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.nav_all_layout_main:{
                break;
            }
            case R.id.nav_dustbin_layout_main:{
                Bundle data = new Bundle();
                data.putString("notebookName","DUSTBIN");
                data.putString("account",account);
                new GuideTo(MainActivity.this, NotebookActivity.class, data).guideToActivity();
                break;
            }
            case R.id.nav_cloud_layout_main:{
                if(CheckNetConnected.isNetworkConnected(MainActivity.this)) {
                    new GuideTo(MainActivity.this, InternetService.class, null).guideToService();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                handler.sendMessage(new Message());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else{
                    Toast.makeText(MainActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo){
        contextMenu.setHeaderTitle("将笔记移至垃圾篓中");
        contextMenu.add(0, 1, Menu.NONE, "移动");
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem){
        notes.get(selectNotePos).setNotebookName("DUSTBIN");
        notes.get(selectNotePos).setIsAlarm(false);
        new TalkWithSQL(MainActivity.this, "note").setDels(new String[]{Long.toString(notes.get(selectNotePos).getId())});
        new TalkWithSQL(MainActivity.this, "note").deleteAllDels();
        new TalkWithSQL(MainActivity.this, "note").updateData(notes.get(selectNotePos));
        flash();
        return true;
    }

    private AdapterView.OnItemClickListener noteAdapterListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Bundle data = new Bundle();
            data.putLong("noteId", notes.get(i).getId());
            data.putString("account", account);
            Log.d("putLong", "" + notes.get(i).getId() + " " +  data.toString());
            new GuideTo(MainActivity.this, EditActivity.class, data).guideToActivity();
        }
    };

    private AdapterView.OnItemClickListener notebookAdapterListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Bundle data = new Bundle();
            data.putString("notebookName",notebookList.get(i));
            data.putString("account",account);
            new GuideTo(MainActivity.this, NotebookActivity.class, data).guideToActivity();
        }
    };

    private AdapterView.OnItemLongClickListener noteAdapterLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectNotePos = i;
            return false;
        }
    };

    private void flash(){
        int clocksNum;
        Bundle bundle = getIntent().getExtras();
        account = bundle.getString("account");
        emailTextView.setText(account);
        notes = (ArrayList<Note>) new TalkWithSQL(MainActivity.this, "note").specialTalkWithNoteForAccount(account);
        Iterator<Note> it = notes.iterator();
        while (it.hasNext()){
            if(it.next().getNotebookName().equals("DUSTBIN")){
                it.remove();
            }
        }
        notebookList = new GetAllNotebooks(MainActivity.this).get(account);
        new TurnOnTheClocks(MainActivity.this).turnOn();
        new AdaptNavListView(MainActivity.this, navListView, notebookList).adapt();
        new AdaptNoteListView(MainActivity.this, noteListView, notes).adapt();
        if((clocksNum = new TalkWithSQL(MainActivity.this, "clock").getAllData().size()) != 0){
            clockTextView.setText("你有" + clocksNum + "篇即将提醒你的笔记");
        }else{
            clockTextView.setText("暂无设置了提醒的笔记");
        }
        navListView.setOnItemClickListener(notebookAdapterListener);
        noteListView.setOnItemClickListener(noteAdapterListener);
        noteListView.setOnItemLongClickListener(noteAdapterLongClickListener);
    }

}
