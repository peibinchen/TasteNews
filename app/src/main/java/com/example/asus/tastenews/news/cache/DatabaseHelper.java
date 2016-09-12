package com.example.asus.tastenews.news.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asus.tastenews.news.Constants;

/**
 * Created by ASUS on 2016/8/11.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static Context context = null;
    private static String databaseName = Constants.DEFAULT_DATABASE_NAME;
    private static SQLiteDatabase.CursorFactory factory = null;
    private static int version = 1;
    private String tableName = "cache";

    private DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory,int version){
        super(context,databaseName,factory,version);
    }
    private static class DatabaseHolder{
        private static DatabaseHelper helper = new DatabaseHelper(context,databaseName,factory,version);
    }

    public static DatabaseHelper getInstance(Context context1, String databaseName1, SQLiteDatabase.CursorFactory factory1,int version1){
        context = context1;
        databaseName = databaseName1;
        factory = factory1;
        version = version1;
        return DatabaseHolder.helper;
    }

    public static DatabaseHelper getInstance(Context context1,String databaseName1,int version1){
        context = context1;
        databaseName = databaseName1;
        version = version1;
        return DatabaseHolder.helper;
    }

    public static DatabaseHelper getInstance(Context context1,String databaseName1){
        context = context1;
        databaseName = databaseName1;
        factory = null;
        version = 1;
        return DatabaseHolder.helper;
    }

    public static DatabaseHelper getInstance(Context context1){
        context = context1;
        return DatabaseHolder.helper;
    }

    private String docid;//docid
    private String title;//标题
    private String digest;//小内容
    private String source;//来源
    private String imgsrc;//图片地址
    private String ptime;//时间
    private String tag;//TAG

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cache (id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT," +
                "imgsrc TEXT,desc TEXT,docid TEXT,source TEXT,ptime TEXT,tag TEXT);");
        tableName = "cache";
    }

    public String getTableName(){
        return tableName;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
