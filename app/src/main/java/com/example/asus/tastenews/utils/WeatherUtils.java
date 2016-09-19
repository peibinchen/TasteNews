package com.example.asus.tastenews.utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.asus.tastenews.R;

/**
 * Created by ASUS on 2016/8/2.
 */
public class WeatherUtils {
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    public WeatherUtils(Context context, AMapLocationListener listener){
        locationClient = new AMapLocationClient(context.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为低功耗模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 设置定位监听
        locationClient.setLocationListener(listener);
        locationOption.setOnceLocation(true);
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(true);
        locationClient.startLocation();
    }

    /**
     * 用于关闭locationClient，防止不断地请求位置
     * @return
     */
    public void stopLocation(){
        locationClient.stopLocation();
    }

    public static int getWeatherImage(String weather){
        if(weather.contains("特大暴雨")){
            return R.mipmap.biz_plugin_weather_tedabaoyu;
        }else if(weather.contains("暴雪")){
            return R.mipmap.biz_plugin_weather_baoxue;
        }else if(weather.contains("大暴雨")){
            return R.mipmap.biz_plugin_weather_dabaoyu;
        }else if(weather.contains("大雪")){
            return R.mipmap.biz_plugin_weather_daxue;
        }else if(weather.contains("多云")){
            return R.mipmap.biz_plugin_weather_duoyun;
        }else if(weather.contains("雷阵雨")){
            return R.mipmap.biz_plugin_weather_leizhenyu;
        }else if(weather.contains("雷阵雨冰雹")){
            return R.mipmap.biz_plugin_weather_leizhenyubingbao;
        }else if(weather.contains("晴")){
            return R.mipmap.biz_plugin_weather_qing;
        }else if(weather.contains("沙尘暴")){
            return R.mipmap.biz_plugin_weather_shachenbao;
        }if(weather.contains("暴雨")){
            return R.mipmap.biz_plugin_weather_baoyu;
        }else if(weather.contains("乌")){
            return R.mipmap.biz_plugin_weather_wu;
        }else if(weather.contains("小雪")){
            return R.mipmap.biz_plugin_weather_xiaoxue;
        }else if(weather.contains("小雨")){
            return R.mipmap.biz_plugin_weather_xiaoyu;
        }else if(weather.contains("阴")){
            return R.mipmap.biz_plugin_weather_yin;
        }else if(weather.contains("多云")){
            return R.mipmap.biz_plugin_weather_duoyun;
        }else if(weather.contains("雨夹雪")){
            return R.mipmap.biz_plugin_weather_yujiaxue;
        }else if(weather.contains("阵雪")){
            return R.mipmap.biz_plugin_weather_zhenxue;
        }else if(weather.contains("阵雨")){
            return R.mipmap.biz_plugin_weather_zhenyu;
        }else if(weather.contains("中雪")){
            return R.mipmap.biz_plugin_weather_zhongxue;
        }else if(weather.contains("中雨")){
            return R.mipmap.biz_plugin_weather_zhongyu;
        }else{
            return R.mipmap.biz_plugin_weather_qing;
        }
    }
}
