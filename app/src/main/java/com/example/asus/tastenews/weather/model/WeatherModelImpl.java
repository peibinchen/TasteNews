package com.example.asus.tastenews.weather.model;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.example.asus.tastenews.R;
import com.example.asus.tastenews.application.NewsApplication;
import com.example.asus.tastenews.beans.WeatherBeanPackage.WeatherBean;
import com.example.asus.tastenews.beans.WeatherBeanPackage.Weather_;
import com.example.asus.tastenews.network.NetworkResolver;
import com.example.asus.tastenews.utils.LogUtils;
import com.example.asus.tastenews.utils.WeatherUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASUS on 2016/5/29.
 */
public class WeatherModelImpl implements WeatherModel,AMapLocationListener {

    private static final String TAG = "WeatherModelImpl";
    private WeatherUtils mWeatherHelper;
    private WeatherModelImpl.LoadLocationListener mCallback;
    private String APPKEY = NewsApplication.getNewsContext().getString(R.string.MAP_API_KEY);

    @Override
    public void loadWeatherData(String cityname,final WeatherModelImpl.LoadWeatherListener listener){
        LogUtils.d("CITYNAMEC","loadWeatherdata now");
        load(cityname, listener);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(aMapLocation.getErrorCode() == 0){
            String temp = aMapLocation.getCity();
            String cityName = temp.replace("市","");
            LogUtils.d("CITYNAME","cityname = " + cityName);
            mWeatherHelper.stopLocation();
            mCallback.onSuccess(cityName);
        }else{
            mWeatherHelper.stopLocation();
            mCallback.onFailure(aMapLocation.getErrorInfo(),new Exception());
        }
    }

    @Override
    public void loadLocation(Context context,final WeatherModelImpl.LoadLocationListener listener){
        mWeatherHelper = new WeatherUtils(context,this);
        mCallback = listener;
    }

    /**
     * 使用Rxjava+Retrofit加载数据
     * @param cityName
     * @param listener
     */
    private void load(String cityName,final WeatherModelImpl.LoadWeatherListener listener){
        Map params = new HashMap();//请求参数
        params.put("cityname",cityName);//要查询的城市，如：温州、上海、北京
        params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
        params.put("dtype","");//返回数据的格式,xml或json，默认json

        NetworkResolver.Callback callback = new NetworkResolver.Callback() {
            @Override
            public void onSuccess(Object object) {
                LogUtils.d("CITYNAMEC", "callback onSuccess object");
                LogUtils.d("CITYNAMEC", "in instanceof");
                WeatherBean bean = (WeatherBean) object;
                List<Weather_> weatherList = bean.getResult().getData().getWeather();
                LogUtils.d("CITYNAMEC","weatherList is " + weatherList);
                listener.onSuccess(weatherList);
            }

            @Override
            public void onFailure(Throwable e) {
                listener.onFailure(e.getMessage(),new Exception());
            }
        };
        NetworkResolver.getResponse("http://op.juhe.cn/onebox/weather/", params, callback);
    }

    public interface LoadWeatherListener{
        void onSuccess(List<Weather_> list);
        void onFailure(String msg,Exception e);
    }

    public interface LoadLocationListener{
        void onSuccess(String cityname);
        void onFailure(String msg,Exception e);
    }
}
