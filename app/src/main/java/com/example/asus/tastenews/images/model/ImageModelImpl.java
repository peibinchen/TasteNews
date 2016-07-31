package com.example.asus.tastenews.images.model;

import com.example.asus.tastenews.beans.ImageBean;
import com.example.asus.tastenews.common.Urls;
import com.example.asus.tastenews.images.ImageJsonUtils;
import com.example.asus.tastenews.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by ASUS on 2016/5/29.
 */
public class ImageModelImpl implements ImageModel{

    @Override
    public void loadImageList(final ImageModelImpl.OnLoadImageListListener listener){
        String url = Urls.IMAGES_URL;
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
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

    public interface OnLoadImageListListener{
        void onSuccess(List<ImageBean> list);
        void onFailure(String msg,Exception e);
    }
}
