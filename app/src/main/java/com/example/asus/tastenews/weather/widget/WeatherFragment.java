package com.example.asus.tastenews.weather.widget;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.application.NewsApplication;
import com.example.asus.tastenews.beans.WeatherBean;
import com.example.asus.tastenews.weather.presenter.WeatherPresenter;
import com.example.asus.tastenews.weather.presenter.WeatherPresenterImpl;
import com.example.asus.tastenews.weather.view.WeatherView;
import com.example.asus.tastenews.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2016/5/30.
 */
public class WeatherFragment extends Fragment implements WeatherView {

    private WeatherPresenter mWeatherPresenter;
    private TextView mCityTV;
    private TextView mTodayTV;
    private ImageView mTodayWeatherImage;
    private TextView mTodayTemperatureTV;
    private TextView mTodayWind;
    private TextView mTodayWeather;
    private ProgressBar mProgressbar;

    private LinearLayout mWeatherLayout;
    private LinearLayout mWeatherContentLayout;
    private FrameLayout mRootLayout;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mWeatherPresenter = new WeatherPresenterImpl(getActivity().getApplication(),this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_weather,null);
        mTodayTV = (TextView)view.findViewById(R.id.today);
        mCityTV = (TextView)view.findViewById(R.id.city);
        mTodayWeather = (TextView)view.findViewById(R.id.weather);
        mTodayTemperatureTV = (TextView)view.findViewById(R.id.weatherTemp);
        mTodayWind = (TextView)view.findViewById(R.id.wind);
        mTodayWeatherImage = (ImageView)view.findViewById(R.id.weatherImage);
        mProgressbar = (ProgressBar)view.findViewById(R.id.progress);
        mWeatherLayout = (LinearLayout)view.findViewById(R.id.weather_layout);
        mWeatherContentLayout = (LinearLayout)view.findViewById(R.id.weather_content);
        mRootLayout = (FrameLayout)view.findViewById(R.id.root_layout);

        mWeatherPresenter.loadWeatherData();

        return view;
    }

    @Override
    public void showProgress(){
        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress(){
        mProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void showWeatherLayout(){
        mWeatherLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCity(String city){
        mCityTV.setText(city);
    }

    @Override
    public void setToday(String data){
        mTodayTV.setText(data);
    }

    @Override
    public void setTemperature(String temperature){
        mTodayTemperatureTV.setText(temperature);
    }

    @Override
    public void setWind(String wind){
        mTodayWind.setText(wind);
    }

    @Override
    public void setWeather(String weather){
        mTodayWeather.setText(weather);
    }

    @Override
    public void setWeatherImage(int res){
        mTodayWeatherImage.setImageResource(res);
    }

    @Override
    public void setWeatherData(List<WeatherBean> lists){
        List<WeatherBean>adapterList = new ArrayList<>();
        for(WeatherBean bean : lists){
            LogUtils.d("TAGGGGGG","getActivity is " + getActivity());
            View view = LayoutInflater.from(NewsApplication.getNewsContext()).inflate(R.layout.item_weather,null,false);
            TextView dateTV = (TextView)view.findViewById(R.id.date);
            TextView weatherTempTV = (TextView)view.findViewById(R.id.weatherTemp);
            ImageView weatherImageIV = (ImageView)view.findViewById(R.id.weatherImage);
            TextView windTV = (TextView)view.findViewById(R.id.wind);
            TextView weather = (TextView)view.findViewById(R.id.weather);

            dateTV.setText(bean.getDate());
            weatherTempTV.setText(bean.getTemperature());
            weatherImageIV.setImageResource(bean.getImageRes());
            windTV.setText(bean.getWind());
            weather.setText(bean.getWeather());
            mWeatherContentLayout.addView(view);
            adapterList.add(bean);
        }
    }

    @Override
    public void showErrorToast(String msg){
        Snackbar.make(getActivity().findViewById(R.id.drawer_layout),msg,Snackbar.LENGTH_SHORT).show();
    }
}
