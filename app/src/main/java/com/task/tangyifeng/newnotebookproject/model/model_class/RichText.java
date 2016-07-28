package com.task.tangyifeng.newnotebookproject.model.model_class;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.EditText;

import com.task.tangyifeng.newnotebookproject.model.model_interface.Downloadable;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Editable;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Uploadable;
import com.task.tangyifeng.newnotebookproject.presenter.EnrichTextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tangyifeng on 16/7/18.
 */
public class RichText{

    private EnrichTextView enrichTextView;

    public RichText(EnrichTextView enrichTextView){
        this.enrichTextView = enrichTextView;
    }

    public SpannableStringBuilder outputSpannableString(EditText editText, int flag, String picturePath){
        SpannableStringBuilder output = new SpannableStringBuilder(editText.getText());
        int startPos = editText.getSelectionStart();
        int endPos = editText.getSelectionEnd();
        switch (flag){
            case EnrichTextView.ENRICH_BOLD:{
                output = setSpan(Typeface.BOLD, output, startPos, endPos);
                break;
            }
            case EnrichTextView.ENRICH_ITALIC:{
                output = setSpan(Typeface.ITALIC, output, startPos, endPos);
                break;
            }
            case EnrichTextView.ENRICH_PICTURE:{
                Drawable drawable = Drawable.createFromPath(picturePath);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                SpannableStringBuilder superBuilder = new SpannableStringBuilder(output, 0, startPos);
                SpannableStringBuilder subBuilder = new SpannableStringBuilder(output, endPos, output.length());
                SpannableStringBuilder imageSpannableString = new SpannableStringBuilder("<img src=\"" + picturePath + "\"/>");
                imageSpannableString.setSpan(new ImageSpan(drawable), 0, imageSpannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                output = superBuilder.append(imageSpannableString)
                        .append(subBuilder);
                Log.d("enrichPicture",output.toString());
                break;
            }
        }
        return output;
    }

    public static String outputHtml(SpannableStringBuilder resource){
        String html = Html.toHtml(resource);
        StringBuffer buffer = new StringBuffer();
        Matcher aim = Pattern.compile("<img src=\"null\"/?>").matcher(html);
        Matcher pic = Pattern.compile("<img src=\".+?\"/?>").matcher(resource.toString());
        while(pic.find() && aim.find()){
            aim.appendReplacement(buffer, pic.group());
        }
        aim.appendTail(buffer);
        html = buffer.toString();
        html = html.replaceAll("<img src=\"null\"/?>","");
        Log.d("result",html);
        return html;
    }

    public static Spanned outputSpannableString(String html){
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String s) {
                Drawable drawable = Drawable.createFromPath(s);
                if(drawable == null)
                    return null;
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            }
        };
        return Html.fromHtml(html, imageGetter, null);
    }

    private SpannableStringBuilder setSpan(int style, SpannableStringBuilder resource, int start, int end){
        ArrayList<Integer> positions = new ArrayList<>();
        StyleSpan span = new StyleSpan(style);
        StyleSpan[] spans = resource.getSpans(start, end, span.getClass());
        SpannableStringBuilder result = new SpannableStringBuilder(resource);
        int count = 0;
        positions.add(start);
        for(StyleSpan sp : spans){
            if(sp.getStyle() == style) {
                Log.d("span'name", "" + sp.toString());
                int spanStart = result.getSpanStart(sp);
                int spanEnd = result.getSpanEnd(sp);
                positions.add((spanStart < start) ? start : spanStart);
                positions.add((spanEnd > end) ? end : spanEnd);
                result.removeSpan(sp);
                if (spanStart < start) {
                    Log.d("set bold", "" + spanStart + " " + start);
                    result.setSpan(new StyleSpan(style), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (spanEnd > end) {
                    Log.d("set bold", "" + end + " " + spanEnd);
                    result.setSpan(new StyleSpan(style), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        positions.add(end);
        while(count < positions.size() - 1) {
            if (count % 2 == 0) {
                result.setSpan(new StyleSpan(style), positions.get(count), positions.get(count + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            count++;
        }
        return result;
    }
}
