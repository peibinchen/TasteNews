package com.example.asus.tastenews.utils;

import android.util.Log;

/**
 * Created by ASUS on 2016/5/24.
 */
public class LogUtils {
    public static final boolean DEBUG = true;

    public static void v(String tag,String message){
        if(DEBUG){
            Log.v(tag,message);
        }
    }

    public static void i(String tag,String message){
        if(DEBUG){
            Log.i(tag,message);
        }
    }

    public static void d(String tag,String message){
        if(DEBUG){
            Log.d(tag,message);
        }
    }

    public static void e(String tag,String message){
        if(DEBUG){
            Log.e(tag,message);
        }
    }

    public static void e(String tag,String message,Throwable error){
        if(DEBUG){
            Log.e(tag,message,error);
        }
    }

    public static void w(String tag,String message){
        if(DEBUG){
            Log.w(tag,message);
        }
    }

}
