package com.example.asus.tastenews.main.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ASUS on 2016/9/1.
 */
public class ThemeSwitchHelper {
  public static final int THEME_MODE_DAY = 0;
  public static final int THEME_MODE_NIGTH = 1;
  public static final int THEME_MODE_BLUE = 2;

  private SharedPreferences mSharedPreferences;
  private final String SHAREPRE_NAME = "themeswitch";
  private int mMode = THEME_MODE_DAY;
  private String THEME_MODE_KEY = "theme_mode_key";

  private Context mContext;

  public ThemeSwitchHelper(Context context){
    mContext = context;
    mSharedPreferences = context.getSharedPreferences(SHAREPRE_NAME, Activity.MODE_PRIVATE);
    mMode = mSharedPreferences.getInt(THEME_MODE_KEY,THEME_MODE_DAY);
  }

  public void setThemeMode(int mode) throws IllegalArgumentException{
    SharedPreferences.Editor editor = mSharedPreferences.edit();
    switch (mode){
      case THEME_MODE_DAY:
        mMode = THEME_MODE_DAY;
        editor.putInt(THEME_MODE_KEY,THEME_MODE_DAY);
        editor.apply();
        break;
      case THEME_MODE_BLUE:
        mMode = THEME_MODE_BLUE;
        editor.putInt(THEME_MODE_KEY,THEME_MODE_BLUE);
        editor.apply();
        break;
      case THEME_MODE_NIGTH:
        mMode = THEME_MODE_NIGTH;
        editor.putInt(THEME_MODE_KEY,THEME_MODE_NIGTH);
        editor.apply();
        break;
      default:
        editor.apply();
        throw new IllegalArgumentException("mode type error");

    }
  }

  public int getMode(){
    return mMode;
  }

  public boolean isDay(){
    return mMode == THEME_MODE_DAY;
  }

  public boolean isNight(){
    return mMode == THEME_MODE_NIGTH;
  }

}
