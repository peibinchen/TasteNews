package com.example.asus.tastenews.head.model;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

import com.example.asus.tastenews.application.NewsApplication;
import com.example.asus.tastenews.common.Urls;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ASUS on 2016/6/12.
 */
public class HeadModelImpl implements HeadModel{

    @Override
    public void getImageFromPhone(final OnFindPictureListener listener){
        new Thread(){
            @Override
            public void run(){
                ContentResolver sr = NewsApplication.getNewsContext().getContentResolver();
                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Cursor cursor = sr.query(imageUri,null,MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?",new String[]{"image/png","image/jpeg"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                if(cursor != null && cursor.moveToFirst()){
                    HashMap<String,List<String>> pictures = new HashMap<>();
                    while(! cursor.isAfterLast()){
                        String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        String parentPath = new File(imagePath).getParentFile().getName();

                        if(!pictures.containsKey(parentPath)){
                            List<String>picturesInFile = new ArrayList<String>();
                            picturesInFile.add(imagePath);
                            pictures.put(parentPath,picturesInFile);
                        }else{
                            pictures.get(parentPath).add(imagePath);
                        }
                        cursor.moveToNext();
                    }

                    cursor.close();

                    if(pictures.isEmpty()){
                        listener.onFailureFind("获取图片失败");
                    }else{
                        listener.onSuccessFind(pictures);
                    }
                }else{
                    listener.onFailureFind("获取图片失败");
                }

            }
        }.start();
    }

    public interface OnFindPictureListener{
        void onSuccessFind(HashMap<String,List<String>> pictures);
        void onFailureFind(String message);
    }
}
