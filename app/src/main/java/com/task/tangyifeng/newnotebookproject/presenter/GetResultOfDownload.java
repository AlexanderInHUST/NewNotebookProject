package com.task.tangyifeng.newnotebookproject.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.task.tangyifeng.newnotebookproject.model.leanCloud_class.subAVObject.CloudAccount;
import com.task.tangyifeng.newnotebookproject.model.model_class.Account;
import com.task.tangyifeng.newnotebookproject.model.model_class.Note;
import com.task.tangyifeng.newnotebookproject.model.model_class.Picture;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Downloadable;

import java.util.List;

/**
 * Created by tangyifeng on 16/7/22.
 */
public class GetResultOfDownload {

    private Context context;
    private static EditText editText;
    private static String html;

    public GetResultOfDownload(Context context){
        this.context = context;
    }

    public void showResult(List < ? extends Downloadable> result){
        if(result.isEmpty()){
            Log.d("showResult","data is empty");
        }else {
            switch (result.get(0).getClass().getSimpleName()){
                case "Account":{
                    for(Account account : (List<Account>)result){
                        new TalkWithSQL(context, "account").updateData(account);
                    }
                    break;
                }
                case "Note":{
                    for(Note note : (List<Note>)result){
                        new TalkWithSQL(context, "note").updateData(note);
                    }
                    break;
                }
                case "Picture":{
                    for(Picture picture : (List<Picture>)result){
                        new TalkWithSQL(context, "picture").updateData(picture);
                    }
                    break;
                }
            }
        }
    }

    public void getReadyForPictures(EditText editText, String html){
        this.editText = editText;
        this.html = html;
    }

    public void showResultOfPicture(){
        if(editText == null || TextUtils.isEmpty(html)){
            Log.e("show result of pictures","not initialed");
        }else{
            editText.setText(new ToHtmlOrSpanned().toSpannableStringBuilder(html));
        }
    }

}
