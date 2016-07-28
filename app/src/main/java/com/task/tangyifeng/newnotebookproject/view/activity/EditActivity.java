package com.task.tangyifeng.newnotebookproject.view.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.task.tangyifeng.newnotebookproject.R;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.AccountBook;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.Clock;
import com.task.tangyifeng.newnotebookproject.model.model_class.Account;
import com.task.tangyifeng.newnotebookproject.model.model_class.Note;
import com.task.tangyifeng.newnotebookproject.model.model_class.Picture;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Deleteable;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Downloadable;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Uploadable;
import com.task.tangyifeng.newnotebookproject.presenter.AdaptNotebookListView;
import com.task.tangyifeng.newnotebookproject.presenter.CheckNetConnected;
import com.task.tangyifeng.newnotebookproject.presenter.GetAllNotebooks;
import com.task.tangyifeng.newnotebookproject.presenter.GetResultOfDownload;
import com.task.tangyifeng.newnotebookproject.presenter.GuideTo;
import com.task.tangyifeng.newnotebookproject.presenter.SyncPictures;
import com.task.tangyifeng.newnotebookproject.presenter.SyncWithCloud;
import com.task.tangyifeng.newnotebookproject.presenter.TalkWithSQL;
import com.task.tangyifeng.newnotebookproject.presenter.ToHtmlOrSpanned;
import com.task.tangyifeng.newnotebookproject.presenter.TranslateTime;
import com.task.tangyifeng.newnotebookproject.view.util_view.RichTextView;

import org.greenrobot.greendao.annotation.Entity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditActivity extends AppCompatActivity {

    private Note note;
    private ArrayList<String> notebooks;
    private Clock clock;
    private String account;

    private EditText editText;
    private EditText editTitle;
    private RichTextView richTextView;
    private TextView notebookTextView;
    private TextView clockTextView;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private ImageView notebookImageView;
    private ImageView clockImageView;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private AlertDialog alertDialog;
    private AlertDialog deleteAlertDialog;
    private AlertDialog clockDialogForDate;
    private AlertDialog clockDialogForTime;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private Date epoch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("编辑笔记");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setOnMenuItemClickListener(toolbarMenuClickListener);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String html = new ToHtmlOrSpanned().toHtml(editText);
                if(!TextUtils.isEmpty(html)) {
                    note.setTitle(editTitle.getText().toString());
                    note.setRichTextHtml(html);
                    note.setIsSync(false);
                    note.setAccount(account);
                    if(note.getNotebookName().equals("DUSTBIN")) {
                        new TalkWithSQL(EditActivity.this, "clock").setDels(new String[]{Long.toString(note.getId())});
                        new TalkWithSQL(EditActivity.this, "clock").deleteAllDels();
                        note.setIsAlarm(false);
                    }
                    new TalkWithSQL(EditActivity.this, "note").updateData(note);
                    if(note.getIsAlarm()) {
                        new TalkWithSQL(EditActivity.this, "clock").updateData
                                (new Clock(null, epoch.getTime(), note.getId(), false));
                    }
                    Matcher picMatcher = Pattern.compile("<img src=\".+?\"/?>").matcher(html);
                    while(picMatcher.find()){
                        String picturePath = picMatcher.group().split("\"")[1];
                        String pictureName = picturePath.split("/")[picturePath.split("/").length - 1];
                        String pictureDir = picturePath.replaceAll("/" + pictureName, "");
                        new TalkWithSQL(EditActivity.this, "picture").updateData(new Picture(null, picturePath));
                        Log.d("update picture", picturePath + " " + pictureDir + " " + pictureName);
                    }
                }else{
                    Toast.makeText(EditActivity.this, "不能保存一个空文档", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setEnabled(true);
                editTitle.setEnabled(true);
            }
        });

        notebookImageView = (ImageView)findViewById(R.id.notebook_icon_edit);
        notebookImageView.setOnClickListener(openNotebookListener);

        notebookTextView = (TextView)findViewById(R.id.notebook_info_edit);
        notebookTextView.setOnClickListener(openNotebookListener);

        clockImageView = (ImageView)findViewById(R.id.alarm_icon_edit);
        clockImageView.setOnClickListener(chooseAlarmClockListener);

        clockTextView = (TextView)findViewById(R.id.alarm_text_edit);
        clockTextView.setOnClickListener(chooseAlarmClockListener);

        editTitle = (EditText) findViewById(R.id.note_title_edit);

        richTextView = (RichTextView)findViewById(R.id.rich_text_view_edit);
        richTextView.setEditActivity(this);
        editText = richTextView.getEditText();

        initialText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestId, int resultId, Intent data){
        super.onActivityResult(requestId, resultId, data);
        String picturePath;
        if(resultId == RESULT_OK){
            ContentResolver resolver = getContentResolver();
            Uri imageUri = data.getData();
            Cursor imageCursor = resolver.query(imageUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            imageCursor.moveToFirst();
            picturePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            richTextView.enrichPicture(picturePath);
        }
    }

    @Override
    public void onBackPressed(){
        String html = new ToHtmlOrSpanned().toHtml(editText);
        if(!TextUtils.isEmpty(html)) {
            note.setTitle(editTitle.getText().toString());
            note.setRichTextHtml(html);
            note.setIsSync(false);
            note.setAccount(account);
            new TalkWithSQL(EditActivity.this, "note").updateData(note);
            if(note.getIsAlarm()) {
                new TalkWithSQL(EditActivity.this, "clock").updateData
                        (new Clock(null, epoch.getTime(), note.getId(), false));
            }
            if(note.getNotebookName().equals("DUSTBIN")) {
                new TalkWithSQL(EditActivity.this, "clock").setDels(new String[]{Long.toString(note.getId())});
                new TalkWithSQL(EditActivity.this, "clock").deleteAllDels();
                note.setIsAlarm(false);
            }
            Matcher picMatcher = Pattern.compile("<img src=\".+?\"/?>").matcher(html);
            while(picMatcher.find()){
                String picturePath = picMatcher.group().split("\"")[1];
                String pictureName = picturePath.split("/")[picturePath.split("/").length - 1];
                String pictureDir = picturePath.replaceAll("/" + pictureName, "");
                new TalkWithSQL(EditActivity.this, "picture").updateData(new Picture(null, picturePath));
                Log.d("update picture", picturePath + " " + pictureDir + " " + pictureName);
            }
        }else{
            Toast.makeText(EditActivity.this, "不能保存一个空文档", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private View.OnClickListener openNotebookListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle data = new Bundle();
            data.putString("notebookName",note.getNotebookName());
            new GuideTo(EditActivity.this, NotebookActivity.class, data).guideToActivity();
        }
    };

    private View.OnClickListener chooseAlarmClockListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RelativeLayout relativeLayoutDate = (RelativeLayout)getLayoutInflater()
                    .inflate(R.layout.clock_choose_edit, null);
            RelativeLayout relativeLayoutTime = (RelativeLayout)getLayoutInflater()
                    .inflate(R.layout.clock_choose_edit_time, null);
            datePicker = (DatePicker)relativeLayoutDate.findViewById(R.id.datePicker);
            timePicker = (TimePicker)relativeLayoutTime.findViewById(R.id.timePicker);
            clockDialogForDate = new AlertDialog.Builder(EditActivity.this)
                    .setView(relativeLayoutDate)
                    .setTitle("选择提醒日期")
                    .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            year = datePicker.getYear();
                            month = datePicker.getMonth() + 1;
                            day = datePicker.getDayOfMonth();
                            clockDialogForDate.dismiss();
                            clockDialogForTime.show();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            clockDialogForDate.dismiss();
                        }
                    })
                    .show();
            clockDialogForTime = new AlertDialog.Builder(EditActivity.this)
                    .setView(relativeLayoutTime)
                    .setTitle("选择提醒时间")
                    .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            hour = timePicker.getCurrentHour();
                            minute = timePicker.getCurrentMinute();
                            try {
                                String time = String.format("%d-%d-%d %d:%d:00",year,month,day,hour,minute);
                                epoch = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
                                Log.d("time",""+year+" "+month+" "+day+" "+hour+" "+minute);
                                note.setIsAlarm(true);
                                clockTextView.setText(time);
                            }catch (ParseException e){
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            clockDialogForDate.dismiss();
                        }
                    })
                    .create();
        }
    };

    private void initialText(){
        Bundle bundle = getIntent().getExtras();
        epoch = new Date();
        account = bundle.getString("account");
        if(bundle.getLong("noteId", -1) != -1){
            editText.setEnabled(false);
            editTitle.setEnabled(false);
            long noteId = bundle.getLong("noteId");
            note = (Note) new TalkWithSQL(EditActivity.this, "note").queryEntities(new String[]{Long.toString(noteId)}).get(0);
            editTitle.setText(note.getTitle().equals("DUSTBIN") ? "垃圾篓" : note.getTitle());
            Log.d("initialText",note.getTitle());
            editText.setText(new ToHtmlOrSpanned().toSpannableStringBuilder(note.getRichTextHtml()));
            if(CheckNetConnected.isNetworkConnected(EditActivity.this)) {
                new GetResultOfDownload(EditActivity.this).getReadyForPictures(editText, note.getRichTextHtml());
                new SyncPictures(EditActivity.this).download(note);
            }else{
                Toast.makeText(EditActivity.this, "当前网络不可用,图片可能无法加载!", Toast.LENGTH_SHORT).show();
            }
            if(note.getIsAlarm()){
                clock = ((List<Clock>) new TalkWithSQL(EditActivity.this, "clock").queryData(new String[]{Long.toString(note.getId())})).get(0);
                epoch.setTime(clock.getTime());
                Log.d("time",epoch.toString());
                clockTextView.setText(TranslateTime.translate(epoch.toString()));
            }
        }else{
            note = new Note("","",false, false);
        }
        notebookTextView.setText(note.getNotebookName());
    }

    private Toolbar.OnMenuItemClickListener toolbarMenuClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_choose_another_notebook:{
                    notebooks = new GetAllNotebooks(EditActivity.this).get();
                    getAlertDialog();
                    break;
                }
                case R.id.menu_delete_main:{
                     deleteAlertDialog = new AlertDialog.Builder(EditActivity.this)
                            .setTitle("确定要移动至废纸篓吗")
                            .setPositiveButton("移动", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    note.setNotebookName("DUSTBIN");
                                    Toast.makeText(EditActivity.this, "已移动到废纸篓", Toast.LENGTH_SHORT).show();
                                    deleteAlertDialog.dismiss();
                                    if(note.getIsAlarm()) {
                                        note.setIsAlarm(false);
                                        new TalkWithSQL(EditActivity.this, "clock").setDels(new String[]{Long.toString(note.getId())});
                                        new TalkWithSQL(EditActivity.this, "clock").deleteAllDels();
                                    }
                                    EditActivity.this.onBackPressed();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteAlertDialog.dismiss();
                                }
                            })
                            .show();
                    break;
                }
                case R.id.menu_delete_clock:{
                    if(note.getIsAlarm()) {
                        note.setIsAlarm(false);
                        new TalkWithSQL(EditActivity.this, "clock").setDels(new String[]{Long.toString(note.getId())});
                        new TalkWithSQL(EditActivity.this, "clock").deleteAllDels();
                        clockTextView.setText("未设置");
                    }else{
                        Toast.makeText(EditActivity.this, "并没有设置提醒", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
            return true;
        }
    };

    private void getAlertDialog(){
        RelativeLayout relativeLayout = (RelativeLayout)getLayoutInflater()
                .inflate(R.layout.notebook_change_edit, null);
        final EditText editText = (EditText)relativeLayout.findViewById(R.id.change_note_listview_editview_edit);
        ListView listView = (ListView)relativeLayout.findViewById(R.id.change_notebook_listview_edit);
        new AdaptNotebookListView(EditActivity.this, listView, notebooks).adapt();
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("选择一个笔记本")
                .setView(relativeLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!TextUtils.isEmpty(editText.getText().toString())){
                            note.setNotebookName(editText.getText().toString());
                            Toast.makeText(EditActivity.this, "加入到"+editText.getText().toString()+"中", Toast.LENGTH_SHORT).show();
                            notebookTextView.setText(note.getNotebookName().equals("DUSTBIN") ? "垃圾篓" : note.getNotebookName());
                            alertDialog.dismiss();
                        }else{
                            Toast.makeText(EditActivity.this, "未或添加选择一个笔记本", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                })
                .show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                note.setNotebookName(notebooks.get(i));
                Toast.makeText(EditActivity.this, "加入到"+notebooks.get(i), Toast.LENGTH_SHORT).show();
                notebookTextView.setText(notebooks.get(i));
                alertDialog.dismiss();
            }
        });
    }

}
