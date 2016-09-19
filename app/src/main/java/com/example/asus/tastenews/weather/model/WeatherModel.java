package com.example.asus.tastenews.weather.model;

import android.content.Context;

/**
 * Created by SomeOneInTheWorld on 2016/5/29.
 */
public interface WeatherModel {
    void loadWeatherData(String cityname,WeatherModelImpl.LoadWeatherListener listener);
    void loadLocation(Context context,WeatherModelImpl.LoadLocationListener listener);
}
