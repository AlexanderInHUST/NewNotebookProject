package com.task.tangyifeng.newnotebookproject.presenter;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;

import com.task.tangyifeng.newnotebookproject.model.model_class.RichText;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Editable;

/**
 * Created by tangyifeng on 16/7/20.
 */
public class ToHtmlOrSpanned {

    public Spanned toSpannableStringBuilder(String html){
        return RichText.outputSpannableString(html);
    }

    public String toHtml(EditText editText){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editText.getText());
        return RichText.outputHtml(spannableStringBuilder);
    }
}
