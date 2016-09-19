package com.example.asus.tastenews.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.asus.tastenews.utils.LogUtils;

/**
 * Created by SomeOneInTheWorld on 2016/6/6.
 */
public class NewsApplication extends Application {
    private static Context newsContext;

    @Override
    public void onCreate(){
        LogUtils.d("dshfau","onCreate()");
        super.onCreate();
        newsContext = getApplicationContext();
    }

    public static Context getNewsContext(){
        return newsContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
