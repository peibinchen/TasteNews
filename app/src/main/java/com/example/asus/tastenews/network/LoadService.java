package com.example.asus.tastenews.network;

import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.beans.WechatIInfoBean;
import com.example.asus.tastenews.common.Urls;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;
/**
 * Created by ASUS on 2016/7/29.
 */
public interface LoadService {
    @GET("query")
    Observable<WechatIInfoBean>loadNews(@QueryMap Map<String,String>otions);
}
