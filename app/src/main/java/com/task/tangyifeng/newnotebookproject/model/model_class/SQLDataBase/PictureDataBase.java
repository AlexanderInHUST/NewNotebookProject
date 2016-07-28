package com.task.tangyifeng.newnotebookproject.model.model_class.SQLDataBase;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.task.tangyifeng.newnotebookproject.model.greenDao_class.DaoMaster;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.DaoSession;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.Note;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.NoteDao;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.Picture;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.PictureDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangyifeng on 16/7/21.
 */
public class PictureDataBase extends DataBase{

    Context context;
    DaoMaster pictureDaoMaster;
    DaoMaster.DevOpenHelper pictureHelper;
    DaoSession pictureSession;
    PictureDao pictureDao;
    QueryBuilder<Picture> pictureBuilder;

    public PictureDataBase(Context context){
        setContext(context);
        openADataBase();
        pictureSession = pictureDaoMaster.newSession();
        pictureDao = pictureSession.getPictureDao();
    }

    @Override
    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public void openADataBase(){
        pictureHelper = new DaoMaster.DevOpenHelper(context, "note_book_project_db");
        pictureDaoMaster = new DaoMaster(pictureHelper.getWritableDatabase());
    }

    @Override
    public List<Picture> getAllData(){
        return pictureDao.loadAll();
    }

    @Override
    public List<com.task.tangyifeng.newnotebookproject.model.model_class.Picture> getAllEntities(){
        List<Picture> pictures = getAllData();
        ArrayList<com.task.tangyifeng.newnotebookproject.model.model_class.Picture> pics = new ArrayList<>();
        for(Picture picture : pictures){
            if(!picture.getIsDel()) {
                com.task.tangyifeng.newnotebookproject.model.model_class.Picture pic = new com.task.tangyifeng.newnotebookproject.model.model_class.Picture
                        (picture.getUrl(), picture.getPicturePath());
                pics.add(pic);
            }
        }
        return pics;
    }

    @Override
    public List<com.task.tangyifeng.newnotebookproject.model.model_class.Picture> queryEntities(String[] picturePath){
        List<Picture> pictures = queryData(picturePath);
        ArrayList<com.task.tangyifeng.newnotebookproject.model.model_class.Picture> pics = new ArrayList<>();
        for(Picture picture : pictures){
            if(!picture.getIsDel()) {
                com.task.tangyifeng.newnotebookproject.model.model_class.Picture pic = new com.task.tangyifeng.newnotebookproject.model.model_class.Picture
                        (picture.getUrl(), picture.getPicturePath());
                pics.add(pic);
            }
        }
        return pics;
    }

    @Override
    public List<Picture> queryData(String[] picturePath){
        pictureBuilder = pictureSession.queryBuilder(Picture.class);
        pictureBuilder.where(PictureDao.Properties.PicturePath.eq(picturePath[0]));
        return pictureBuilder.list();
    }

    @Override
    public void updateData(Object object){
        com.task.tangyifeng.newnotebookproject.model.model_class.Picture picture = (com.task.tangyifeng.newnotebookproject.model.model_class.Picture) object;
        List<Picture> pictures = queryData(new String[]{picture.getPicturePath()});
        if(pictures.isEmpty()){
            Picture newPicture = new Picture(null, picture.getUrl(), picture.getPicturePath(), false);
            pictureDao.insert(newPicture);
        }else if(TextUtils.isEmpty(pictures.get(0).getUrl()) && !TextUtils.isEmpty(picture.getUrl())){
            Picture newPicture = new Picture(pictures.get(0).getId(), picture.getUrl(), picture.getPicturePath(), false);
            pictureDao.update(newPicture);
        }
    }

    @Override
    public boolean deleteData(Object object){
        com.task.tangyifeng.newnotebookproject.model.model_class.Picture picture = (com.task.tangyifeng.newnotebookproject.model.model_class.Picture) object;
        List<Picture> pictures = queryData(new String[]{picture.getPicturePath()});
        if(pictures.isEmpty()){
            return false;
        }else{
            Picture delPicture = pictures.get(0);
            pictureDao.delete(delPicture);
            return true;
        }
    }

    @Override
    public void setIsDel(String[] picturePath){
        List<Picture> list = queryData(picturePath);
        Picture picture = new Picture(list.get(0).getId(), list.get(0).getUrl(), list.get(0).getPicturePath(), true);
        pictureDao.update(picture);
    }

    @Override
    public List<Picture> getAllDels(){
        pictureBuilder = pictureSession.queryBuilder(Picture.class);
        pictureBuilder.where(PictureDao.Properties.IsDel.eq(true));
        return pictureBuilder.list();
    }

    @Override
    public int deleteAllDel(){
        List<Picture> dels;
        int count = 0;
        dels = getAllDels();
        for(Picture picture : dels){
            com.task.tangyifeng.newnotebookproject.model.model_class.Picture pic = new com.task.tangyifeng.newnotebookproject.model.model_class.Picture(picture.getUrl(), picture.getPicturePath());
            deleteData(pic);
            count ++;
        }
        return count;
    }

    public List<com.task.tangyifeng.newnotebookproject.model.model_class.Picture> getPictureEntitiesWithoutUrl(){
        pictureBuilder = pictureSession.queryBuilder(Picture.class);
        pictureBuilder.where(PictureDao.Properties.Url.isNull());
        List<Picture> list = pictureBuilder.list();
        ArrayList<com.task.tangyifeng.newnotebookproject.model.model_class.Picture> pictures = new ArrayList<>();
        for(Picture pic : list){
            com.task.tangyifeng.newnotebookproject.model.model_class.Picture picture =
                    new com.task.tangyifeng.newnotebookproject.model.model_class.Picture(pic.getUrl(), pic.getPicturePath());
            pictures.add(picture);
        }
        return pictures;
    }

}
