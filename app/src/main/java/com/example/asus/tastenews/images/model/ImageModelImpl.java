package com.example.asus.tastenews.images.model;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.application.NewsApplication;
import com.example.asus.tastenews.beans.ImageBean;
import com.example.asus.tastenews.common.Urls;
import com.example.asus.tastenews.images.ImageJsonUtils;
import com.example.asus.tastenews.utils.LogUtils;
import com.example.asus.tastenews.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/5/29.
 */
public class ImageModelImpl implements ImageModel{
    private final String TAG = "ImageModelImpllll";
    @Override
    public void loadImageList(final ImageModelImpl.OnLoadImageListListener listener){
        String url = Urls.IMAGES_URL + addParams();
        LogUtils.d(TAG,"url is " + url);
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtils.d(TAG,"response is " + response);
                List<ImageBean> beans = ImageJsonUtils.readJsonImageBeans(response);
                listener.onSuccess(beans);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("loadImageList error",e);
            }
        };

        OkHttpUtils.get(url,loadNewsCallback);
    }

    private String addParams(){
        return "?" + "showapi_appid=" + NewsApplication.getNewsContext().getString(R.string.INTERESTING_PICTURE_APPID)
                + "&" + "showapi_sign=" + NewsApplication.getNewsContext().getString(R.string.INTERESTING_PICTURE_KEY);
    }

    public interface OnLoadImageListListener{
        void onSuccess(List<ImageBean> list);
        void onFailure(String msg,Exception e);
    }
}
