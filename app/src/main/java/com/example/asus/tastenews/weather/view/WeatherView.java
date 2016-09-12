package com.example.asus.tastenews.weather.view;

import com.example.asus.tastenews.beans.WeatherBean;
import com.example.asus.tastenews.beans.WeatherBeanPackage.Weather_;

import java.util.List;

/**
 * Created by ASUS on 2016/5/30.
 */
public interface WeatherView {

    void showProgress();
    void hideProgress();
    void showWeatherLayout();

    void setCity(String city);
    void setToday(String data);
    void setTemperature(String temperature);
    void setWind(String wind);
    void setWeather(String weather);
    void setWeatherImage(int res);
    void setWeatherData(List<Weather_> lists);

    void showErrorToast(String msg);
}
