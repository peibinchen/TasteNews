package com.example.asus.tastenews.weather;

import android.text.TextUtils;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.WeatherBean;
import com.example.asus.tastenews.utils.LogUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2016/5/29.
 */
public class WeatherJsonUtils {

    public static String getCity(String json){
        LogUtils.d("TAGGGGGG","json is " + json);
//        JsonReader reader = new JsonReader(new StringReader(json));
//        reader.setLenient(true);
        String json2 = json.trim();
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(json2).getAsJsonObject();
        JsonElement status = object.get("status");
        if(status != null && "OK".equals(status.getAsString())){
            JsonObject result = object.getAsJsonObject("result");
            if(result != null){
                JsonElement addressElement = result.get("addressComponent");
                if(addressElement != null){
                    JsonElement cityElement = addressElement.getAsJsonObject().get("city");
                    if(cityElement != null){
                        return cityElement.getAsString().replace("市","");
                    }
                }
            }
        }
        return null;
    }

    public static List<WeatherBean> getWeatherInfo(String json){
        List<WeatherBean> list = new ArrayList<>();
        if(TextUtils.isEmpty(json)){
            return null;
        }
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        String status = jsonObj.get("status").getAsString();
        if("1000".equals(status)) {
            JsonArray jsonArray = jsonObj.getAsJsonObject("data").getAsJsonArray("forecast");
            for (int i = 0; i < jsonArray.size(); i++) {
                WeatherBean weatherBean = getWeatherBeanFromJson(jsonArray.get(i).getAsJsonObject());
                list.add(weatherBean);
            }
        }
        return list;
    }

    private static WeatherBean getWeatherBeanFromJson(JsonObject jsonObject){

        String temperature = jsonObject.get("high").getAsString() + " " + jsonObject.get("low").getAsString();
        String weather = jsonObject.get("type").getAsString();
        String wind = jsonObject.get("fengxiang").getAsString();
        String date = jsonObject.get("date").getAsString();

        WeatherBean bean = new WeatherBean();

        bean.setTemperature(temperature);
        bean.setWeather(weather);
        bean.setWind(wind);
        bean.setDate(date);
        bean.setImageRes(getWeatherImage(weather));
        bean.setWeek(date.substring(date.length() - 3));
        return bean;
    }

    public static int getWeatherImage(String weather) {
        if (weather.equals("多云") || weather.equals("多云转阴") || weather.equals("多云转晴")) {
            return R.mipmap.biz_plugin_weather_duoyun;
        } else if (weather.equals("中雨") || weather.equals("中到大雨")) {
            return R.mipmap.biz_plugin_weather_zhongyu;
        } else if (weather.equals("雷阵雨")) {
            return R.mipmap.biz_plugin_weather_leizhenyu;
        } else if (weather.equals("阵雨") || weather.equals("阵雨转多云")) {
            return R.mipmap.biz_plugin_weather_zhenyu;
        } else if (weather.equals("暴雪")) {
            return R.mipmap.biz_plugin_weather_baoxue;
        } else if (weather.equals("暴雨")) {
            return R.mipmap.biz_plugin_weather_baoyu;
        } else if (weather.equals("大暴雨")) {
            return R.mipmap.biz_plugin_weather_dabaoyu;
        } else if (weather.equals("大雪")) {
            return R.mipmap.biz_plugin_weather_daxue;
        } else if (weather.equals("大雨") || weather.equals("大雨转中雨")) {
            return R.mipmap.biz_plugin_weather_dayu;
        } else if (weather.equals("雷阵雨冰雹")) {
            return R.mipmap.biz_plugin_weather_leizhenyubingbao;
        } else if (weather.equals("晴")) {
            return R.mipmap.biz_plugin_weather_qing;
        } else if (weather.equals("沙尘暴")) {
            return R.mipmap.biz_plugin_weather_shachenbao;
        } else if (weather.equals("特大暴雨")) {
            return R.mipmap.biz_plugin_weather_tedabaoyu;
        } else if (weather.equals("雾") || weather.equals("雾霾")) {
            return R.mipmap.biz_plugin_weather_wu;
        } else if (weather.equals("小雪")) {
            return R.mipmap.biz_plugin_weather_xiaoxue;
        } else if (weather.equals("小雨")) {
            return R.mipmap.biz_plugin_weather_xiaoyu;
        } else if (weather.contains("阴")) {
            return R.mipmap.biz_plugin_weather_yin;
        } else if (weather.equals("雨夹雪")) {
            return R.mipmap.biz_plugin_weather_yujiaxue;
        } else if (weather.equals("阵雪")) {
            return R.mipmap.biz_plugin_weather_zhenxue;
        } else if (weather.equals("中雪")) {
            return R.mipmap.biz_plugin_weather_zhongxue;
        } else if (weather.contains("雨")) {
            return R.mipmap.biz_plugin_weather_xiaoyu;
        } else {
            return R.mipmap.biz_plugin_weather_duoyun;
        }
    }
}
