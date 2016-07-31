package com.example.asus.tastenews.weather.presenter;

import android.content.Context;

import com.example.asus.tastenews.beans.WeatherBean;
import com.example.asus.tastenews.utils.ToolUtils;
import com.example.asus.tastenews.weather.model.WeatherModel;
import com.example.asus.tastenews.weather.model.WeatherModelImpl;
import com.example.asus.tastenews.weather.view.WeatherView;

import java.util.List;

/**
 * Created by ASUS on 2016/5/30.
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
                mWeatherView.setCity(cityname);
                mWeatherModel.loadWeatherData(cityname,WeatherPresenterImpl.this);
            }

            @Override
            public void onFailure(String msg, Exception e) {
                mWeatherView.showErrorToast("error to gain city name");
                mWeatherView.setCity("广州");
                mWeatherModel.loadWeatherData("广州",WeatherPresenterImpl.this);
            }
        };

        mWeatherModel.loadLocation(mContext,listener);
    }

    @Override
    public void onSuccess(List<WeatherBean> list){
        if(list != null && list.size() > 0){
            WeatherBean todayWeather = list.remove(0);
            mWeatherView.setWeather(todayWeather.getWeather());
            mWeatherView.setTemperature(todayWeather.getTemperature());
            mWeatherView.setToday(todayWeather.getDate());
            mWeatherView.setWind(todayWeather.getWind());
            mWeatherView.setWeatherImage(todayWeather.getImageRes());
        }

        mWeatherView.setWeatherData(list);
        mWeatherView.hideProgress();
        mWeatherView.showWeatherLayout();
    }

    @Override
    public void onFailure(String msg,Exception e){
        mWeatherView.hideProgress();
        mWeatherView.showErrorToast("cannot gain the weather");
    }
}
