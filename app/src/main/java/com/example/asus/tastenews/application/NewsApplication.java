package com.example.asus.tastenews.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by ASUS on 2016/6/6.
 */
public class NewsApplication extends Application {
    private static Context newsContext;

    @Override
    public void onCreate(){
        super.onCreate();
        newsContext = getApplicationContext();
    }

    public static Context getNewsContext(){
        return newsContext;
    }

}