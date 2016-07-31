package com.example.asus.tastenews.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 网络请求的帮助类
 */
public class OkHttpUtils {

    private static final String TAG = "OkHttpUtils";

    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    /**
     * 这里使用了单例模式，使得整个应用只有一个OkHttpUtils实例
     */
    private OkHttpUtils(){
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(10,TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30,TimeUnit.SECONDS);

        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
    }

    private synchronized static OkHttpUtils getInstance(){
        if(mInstance == null){
            mInstance = new OkHttpUtils();
        }
        return mInstance;
    }

    /**
     * 获得请求
     * @param url
     * @param callback
     */
    private void getRequest(String url,final ResultCallback callback){
        final Request request = new Request.Builder().url(url).build();
        deliveryResult(callback,request);
    }

    /**
     * 发送请求
     * @param url
     * @param callback
     * @param params
     */
    private void postRequest(String url, ResultCallback callback, List<Param>params){
        Request request = buildPostRequests(url,params);
        deliveryResult(callback,request);
    }

    /**
     * 根据url和Params参数构造请求的字段
     * @param url
     * @param params
     * @return
     */
    private Request buildPostRequests(String url,List<Param>params){
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for(Param param:params){
            builder.add(param.key,param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    /**
     * 处理请求并回调
     * @param callback
     * @param request
     */
    private void deliveryResult(final ResultCallback callback, Request request){
        mOkHttpClient.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Request request,final IOException e){
                sendFailCallback(callback,e);
            }

            @Override
            public void onResponse(Response response)throws IOException{
                try{
                    String str = response.body().string();
                    if(callback.mType == String.class){
                        sendSuccessCallback(callback,str);
                    }else{
                        Object obj = JsonUtils.deserialize(str,callback.mType);
                        sendSuccessCallback(callback,obj);
                    }
                }catch(final Exception e){
                    LogUtils.e(TAG,"convert json failure",e);
                    sendFailCallback(callback,e);
                }
            }
        });
    }

    /**
     * 根据回调的成功或失败异步处理消息
     * @param callback
     * @param e
     */
    private void sendFailCallback(final ResultCallback callback,final Exception e){
        mDelivery.post(new Runnable(){
            @Override
            public void run(){
                if(callback !=null){
                    callback.onFailure(e);
                }
            }
        });
    }

    private void sendSuccessCallback(final ResultCallback callback,final Object obj){
        mDelivery.post(new Runnable(){
            @Override
            public void run(){
                if(callback != null){
                    callback.onSuccess(obj);
                }
            }
        });
    }


    /***********************对外接口***************************/

    public static void get(String url,ResultCallback callback){
        getInstance().getRequest(url,callback);
    }

    public static void post(String url,ResultCallback callback,List<Param>params){
        getInstance().postRequest(url,callback,params);
    }

    public static abstract class ResultCallback<T>{
        Type mType;

        public ResultCallback(){
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?>subclass){
            Type superclass = subclass.getGenericSuperclass();
            if(superclass instanceof Class){
                throw new RuntimeException("missing type parameter.");
            }

            ParameterizedType parameterized = (ParameterizedType)superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onSuccess(T response);

        public abstract void onFailure(Exception e);
    }

    public static class Param{

        String key;
        String value;

        public Param(){}

        public Param(String key,String value){
            this.key = key;
            this.value = value;
        }
    }
}
