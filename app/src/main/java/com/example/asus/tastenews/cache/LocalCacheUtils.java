package com.example.asus.tastenews.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by ASUS on 2016/7/19.
 */
public class LocalCacheUtils {

    private static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public Bitmap getBitmapFromLocal(String url){
        String filename = null;
        try{
            filename = MD5Encoder.encode(url);//使用MD5加密
            File file = new File(CACHE_PATH,filename);//路径+文件名
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    public void saveBitmapToLocal(Bitmap bitmap,String url){
        try{
            String filename = MD5Encoder.encode(url);
            File file = new File(CACHE_PATH,filename);

            File parent = file.getParentFile();
            if(!parent.exists()){
                parent.mkdirs();//创建parent的目录，并且如果有需要的话创建parent的父母目录
            }

            //将Bitmap进行压缩后储存在file中
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
