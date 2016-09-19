package com.example.asus.tastenews.network;

import com.example.asus.tastenews.beans.WeatherBeanPackage.WeatherBean;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;
/**
 * Created by SomeOneInTheWorld on 2016/7/29.
 */
public interface LoadService {
    @GET("query")
    Observable<WeatherBean>loadNews(@QueryMap Map<String,String>otions);
}
