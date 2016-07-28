package com.task.tangyifeng.newnotebookproject.model.leanCloud_class.subAVObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.SaveCallback;
import com.task.tangyifeng.newnotebookproject.model.model_class.Note;
import com.task.tangyifeng.newnotebookproject.model.model_class.Picture;
import com.task.tangyifeng.newnotebookproject.model.model_interface.Downloadable;
import com.task.tangyifeng.newnotebookproject.presenter.GetResultOfDownload;
import com.task.tangyifeng.newnotebookproject.presenter.SyncWithCloud;
import com.task.tangyifeng.newnotebookproject.presenter.TalkWithSQL;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tangyifeng on 16/7/25.
 */
public class CloudFile {

    private AVFile avFile;
    private ArrayList<AVFile> avFiles;
    private ArrayList<File> files;
    private Context context;
    private File file;
    private int listCount;

    public CloudFile(Context context){
        this.context = context;
    }

    public void initialDataBase(){
        new SyncWithCloud((Downloadable) new com.task.tangyifeng.newnotebookproject.model.model_class.Picture(), context).download();
    }

    public void downloadFile(Note note){
        files = new ArrayList<>();
        String html = note.getRichTextHtml();
        final ArrayList<String> pictureDirs = new ArrayList<>();
        listCount = 0;
        Matcher picMatcher = Pattern.compile("<img src=\".+?\"/?>").matcher(html);
        while(picMatcher.find()){
            String picturePath = picMatcher.group().split("\"")[1];
            String pictureName = picturePath.split("/")[picturePath.split("/").length - 1];
            String pictureDir = picturePath.replaceAll("/" + pictureName, "");
            pictureDirs.add(pictureDir);
            Log.d("download picture",picturePath);
            Picture picture = ((ArrayList<Picture>)new TalkWithSQL(context, "picture").queryEntities(picturePath)).get(0);
            file = new File(picture.getPicturePath());
            if(!file.exists()){
                files.add(file);
                avFile = new AVFile(pictureName, picture.getUrl(), new HashMap<String, Object>());
                avFile.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, AVException e) {
                        if (e != null)
                            e.printStackTrace();
                        Log.d("download picture", "download succeed");
                        File dirs = new File(pictureDirs.get(listCount));
                        if (!dirs.exists())
                            dirs.mkdirs();
                        try {
                            FileOutputStream outputStream = new FileOutputStream(files.get(listCount));
                            outputStream.write(bytes, 0, bytes.length);
                        } catch (IOException IOe) {
                            IOe.printStackTrace();
                        }
                        listCount++;
                        Log.d("download picture",""+listCount + " " + files.size());
                        if(listCount == files.size())
                            new GetResultOfDownload(context).showResultOfPicture();
                    }
                });
            }
        }
    }

    public void uploadFile(Note note){
        avFiles = new ArrayList<>();
        final ArrayList<String> picturePaths = new ArrayList<>();
        listCount = 0;
        String html = note.getRichTextHtml();
        Matcher picMatcher = Pattern.compile("<img src=\".+?\"/?>").matcher(html);
        while(picMatcher.find()) {
            String picturePath = picMatcher.group().split("\"")[1];
            String pictureName = picturePath.split("/")[picturePath.split("/").length - 1];
            picturePaths.add(picturePath);
            Log.d("upload picture",picturePath);
            Picture picture = ((ArrayList<Picture>)new TalkWithSQL(context, "picture").queryEntities(picturePath)).get(0);
            if(TextUtils.isEmpty(picture.getUrl())){
                try {
                    avFile = AVFile.withAbsoluteLocalPath(pictureName, picturePath);
                    avFiles.add(avFile);
                    avFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e != null)
                                e.printStackTrace();
                            AVFile tempAVFile = avFiles.get(listCount);
                            String tempPicturePath = picturePaths.get(listCount);
                            listCount++;
                            Log.d("upload picture","upload succeed "+tempAVFile.getUrl() + " " + tempPicturePath);
                            new TalkWithSQL(context, "picture").updateData(new Picture(tempAVFile.getUrl(), tempPicturePath));
                        }
                    });
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
