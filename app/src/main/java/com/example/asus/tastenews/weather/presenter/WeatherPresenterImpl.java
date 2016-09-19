package com.example.asus.tastenews.weather.presenter;

import android.content.Context;

import com.example.asus.tastenews.beans.WeatherBeanPackage.Info_;
import com.example.asus.tastenews.beans.WeatherBeanPackage.Weather_;
import com.example.asus.tastenews.utils.LogUtils;
import com.example.asus.tastenews.utils.ToolUtils;
import com.example.asus.tastenews.utils.WeatherUtils;
import com.example.asus.tastenews.weather.model.WeatherModel;
import com.example.asus.tastenews.weather.model.WeatherModelImpl;
import com.example.asus.tastenews.weather.view.WeatherView;

import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/5/30.
 */
public class WeatherPresenterImpl implements WeatherPresenter,WeatherModelImpl.LoadWeatherListener {

    private WeatherModel mWeatherModel;
    private WeatherView mWeatherView;
    private Context mContext;

    public WeatherPresenterImpl(Context context, WeatherView weatherView){
        mWeatherView = weatherView;
        mWeatherModel = new WeatherModelImpl();
        mContext = context;
    }

    @Override
    public void loadWeatherData(){
        mWeatherView.showProgress();
        if(!ToolUtils.isNetworkAvailable(mContext)){
            mWeatherView.hideProgress();
            mWeatherView.showErrorToast("no network connect");
            return;
        }

        WeatherModelImpl.LoadLocationListener listener = new WeatherModelImpl.LoadLocationListener() {
            @Override
            public void onSuccess(String cityname) {
                LogUtils.d("CITYNAMEC","onSuccess cityname = " + cityname);
                mWeatherView.setCity(cityname);
                mWeatherModel.loadWeatherData(cityname,WeatherPresenterImpl.this);
            }

            @Override
            public void onFailure(String msg, Exception e) {
                mWeatherView.showErrorToast(msg);
                mWeatherView.setCity("广州");
                mWeatherModel.loadWeatherData("广州",WeatherPresenterImpl.this);
            }
        };

        mWeatherModel.loadLocation(mContext,listener);
    }

    @Override
    public void onSuccess(List<Weather_> list){
        LogUtils.d("CITYNAMEC","onSuccess in weatherPresenterImpl");
        if(list != null && list.size() > 0){
            Weather_ todayWeather = list.remove(0);
            Info_ info = todayWeather.getInfo();
            List<String> day = info.getDay();
            mWeatherView.setWeather(day.get(1));
            mWeatherView.setTemperature(day.get(2));
            mWeatherView.setToday(todayWeather.getDate());
            mWeatherView.setWind(day.get(4));
            mWeatherView.setWeatherImage(WeatherUtils.getWeatherImage(day.get(1)));
        }

        mWeatherView.setWeatherData(list);
        mWeatherView.hideProgress();
        mWeatherView.showWeatherLayout();
    }

    @Override
    public void onFailure(String msg,Exception e){
        mWeatherView.hideProgress();
        mWeatherView.showErrorToast(msg);
    }
}
