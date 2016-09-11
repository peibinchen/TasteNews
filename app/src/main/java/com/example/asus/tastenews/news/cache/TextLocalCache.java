package com.example.asus.tastenews.news.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.news.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2016/8/11.
 */
public class TextLocalCache {
    private DatabaseHelper mDatabaseHelper;

    public TextLocalCache(Context context){
        mDatabaseHelper = DatabaseHelper.getInstance(context);
    }

    public void saveAllTextToLocal(List<NewsBean>bean){

        if(bean.size() <= 0)
            return;
        int size = bean.size();
        if(bean.size() > 200){
            size = 200;
        }
        for(int i = 0;i<size;i++){
            saveTextToLocal(bean.get(i));
        }
    }

    public void saveTextToLocal(NewsBean bean){
        ContentValues values = new ContentValues();
        values.put(Constants.DATABASE_DESC,bean.getDigest());
        values.put(Constants.DATABASE_DOCID,bean.getDocid());
        values.put(Constants.DATABASE_TITLE,bean.getTitle());
        values.put(Constants.DATABASE_IMGSRC,(String)null);
        values.put(Constants.DATABASE_PTIME,bean.getPtime());
        values.put(Constants.DATABASE_TAG,bean.getTag());
        values.put(Constants.DATABASE_SOURCE,bean.getSource());
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.insert(mDatabaseHelper.getTableName(),null,values);
        db.close();
    }

    public List<NewsBean> getTextLocal(String[] types){
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        List<NewsBean> newsBeanList = new ArrayList<>();
        Cursor cursor = db.query(mDatabaseHelper.getTableName(),null,Constants.DATABASE_TAG + "=?",types,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String desc = cursor.getString(getColumnIndex(cursor,Constants.DATABASE_DESC));
            String title = cursor.getString(getColumnIndex(cursor,Constants.DATABASE_TITLE));
            String imgSrc = cursor.getString(getColumnIndex(cursor,Constants.DATABASE_IMGSRC));
            String docid = cursor.getString(getColumnIndex(cursor,Constants.DATABASE_DOCID));
            String ptime = cursor.getString(getColumnIndex(cursor,Constants.DATABASE_PTIME));
            String source = cursor.getString(getColumnIndex(cursor,Constants.DATABASE_SOURCE));
            String tag = cursor.getString(getColumnIndex(cursor,Constants.DATABASE_TAG));
            NewsBean newsBean = new NewsBean();
            newsBean.setDocid(docid);
            newsBean.setDigest(desc);
            newsBean.setTitle(title);
            newsBean.setImgsrc(imgSrc);
            newsBean.setPtime(ptime);
            newsBean.setTag(tag);
            newsBean.setSource(source);
            newsBeanList.add(newsBean);
            //特别要注意这里一定要加moveToNext（），不然会爆内存
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return newsBeanList;
    }

    public void deleteAllText(String[]types){
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.delete(mDatabaseHelper.getTableName(),Constants.DATABASE_TAG + "=?",types);
        db.close();
    }

    private int getColumnIndex(Cursor cursor,String columnName){
        return cursor.getColumnIndex(columnName);
    }
}
