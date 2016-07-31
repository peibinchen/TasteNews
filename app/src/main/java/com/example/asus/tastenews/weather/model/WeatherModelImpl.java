package com.example.asus.tastenews.weather.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.text.TextUtils;

import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.beans.WeatherBean;
import com.example.asus.tastenews.common.Urls;
import com.example.asus.tastenews.utils.LogUtils;
import com.example.asus.tastenews.utils.OkHttpUtils;
import com.example.asus.tastenews.weather.WeatherJsonUtils;
import com.squareup.okhttp.OkHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by ASUS on 2016/5/29.
 */
public class WeatherModelImpl implements WeatherModel{

    private static final String TAG = "WeatherModelImpl";

    @Override
    public void loadWeatherData(String cityname,final WeatherModelImpl.LoadWeatherListener listener){
        try{
            String url = Urls.WEATHER + URLEncoder.encode(cityname,"utf-8");
            OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
                @Override
                public void onSuccess(String response) {
                    List<WeatherBean>lists = WeatherJsonUtils.getWeatherInfo(response);
                    listener.onSuccess(lists);
                }

                @Override
                public void onFailure(Exception e) {
                    listener.onFailure("loadWeatherData error",e);
                }
            };

            OkHttpUtils.get(url,callback);
        }catch(UnsupportedEncodingException e){
            LogUtils.e(TAG,"loadWeatherData error",e);
        }
    }

    @Override
    public void loadLocation(Context context,final WeatherModelImpl.LoadLocationListener listener){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                LogUtils.e(TAG,"loadLocation error 2");
                listener.onFailure("loadLocation error",null);
                return;
            }
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location == null){
            LogUtils.e(TAG,"loadLocation error 1");
            listener.onFailure("loadLoaction error",null);
            return;
        }

        double lantitude = location.getLatitude();
        double longitude = location.getLongitude();
        String url = getLocationURL(longitude,lantitude);

        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                String city = WeatherJsonUtils.getCity(response);
                if(TextUtils.isEmpty(city)){
                    LogUtils.e(TAG,"load location fail");
                    listener.onFailure("load location fail",null);
                }else{
                    listener.onSuccess(city);
                }
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load location fail",e);
            }
        };

        OkHttpUtils.get(url,callback);
    }

    private String getLocationURL(double longitude,double lantitude){
        StringBuffer sb = new StringBuffer(Urls.INTERFACE_LOCATION);
        sb.append("?output=json").append("&referer=32D45CBEEC107315C553AD1131915D366EEF79B4");
        sb.append("&location=").append(lantitude).append(",").append(longitude);

        LogUtils.d(TAG,sb.toString());

        return sb.toString();
    }

    public interface LoadWeatherListener{
        void onSuccess(List<WeatherBean> list);
        void onFailure(String msg,Exception e);
    }

    public interface LoadLocationListener{
        void onSuccess(String cityname);
        void onFailure(String msg,Exception e);
    }
}
