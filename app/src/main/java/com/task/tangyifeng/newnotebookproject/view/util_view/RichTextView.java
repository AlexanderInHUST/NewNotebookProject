package com.task.tangyifeng.newnotebookproject.view.util_view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.task.tangyifeng.newnotebookproject.R;
import com.task.tangyifeng.newnotebookproject.presenter.EnrichTextView;
import com.task.tangyifeng.newnotebookproject.view.activity.EditActivity;

/**
 * Created by tangyifeng on 16/7/19.
 */
public class RichTextView extends LinearLayout implements View.OnClickListener{

    private ImageView boldIcon;
    private ImageView italicIcon;
    private ImageView pictureIcon;
    private EditText editText;

    private EditActivity editActivity;

    public RichTextView(Context context){
        super(context, null);
    }

    public RichTextView(Context context, AttributeSet attr){
        super(context, attr);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.linearlayout_rich_text_view, RichTextView.this);
        boldIcon = (ImageView)findViewById(R.id.rich_text_view_bold);
        italicIcon = (ImageView)findViewById(R.id.rich_text_view_italic);
        pictureIcon = (ImageView)findViewById(R.id.rich_text_view_picture);
        editText = (EditText) findViewById(R.id.rich_text_view_edit_text);

        boldIcon.setOnClickListener(this);
        italicIcon.setOnClickListener(this);
        pictureIcon.setOnClickListener(this);
    }

    public EditText getEditText(){
        return editText;
    }

    public void setEditActivity(EditActivity editActivity){
        this.editActivity = editActivity;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rich_text_view_bold:{
                new EnrichTextView(editText, EnrichTextView.ENRICH_BOLD).enrich(null);
                break;
            }
            case R.id.rich_text_view_italic:{
                new EnrichTextView(editText, EnrichTextView.ENRICH_ITALIC).enrich(null);
                break;
            }
            case R.id.rich_text_view_picture:{
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                editActivity.startActivityForResult(intent, 0);
                break;
            }
        }
    }

    public void enrichPicture(String picturePath){
        new EnrichTextView(editText, EnrichTextView.ENRICH_PICTURE).enrich(picturePath);
    }

}
