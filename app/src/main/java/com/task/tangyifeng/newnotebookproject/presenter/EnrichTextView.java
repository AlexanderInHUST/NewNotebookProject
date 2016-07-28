package com.task.tangyifeng.newnotebookproject.presenter;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.widget.EditText;
import android.widget.TextView;

import com.task.tangyifeng.newnotebookproject.model.model_class.RichText;

/**
 * Created by tangyifeng on 16/7/20.
 */
public class EnrichTextView {

    public static final int NULL = -1;
    public static final int ENRICH_BOLD = 0;
    public static final int ENRICH_ITALIC = 1;
    public static final int ENRICH_PICTURE = 2;

    private EditText editText;
    private int flag;

    public EnrichTextView(EditText editText, int flag){
        this.editText = editText;
        this.flag = flag;
    }

    public void enrich(String picturePath){
        SpannableStringBuilder output = new RichText(this).outputSpannableString(editText, flag, picturePath);
        editText.setText(output);
    }

}
