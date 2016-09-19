package com.example.asus.tastenews.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.asus.tastenews.R;
import com.example.asus.tastenews.cache.LocalCacheUtils;

/**
 * Created by SomeOneInTheWorld on 2016/5/26.
 */
public class ImageLoaderUtils {
    private static LocalCacheUtils mLocalCacheUtils = new LocalCacheUtils();

    public static void display(Context context, final ImageView imageView, final String url,
                               int placeholder,int error){
        if(imageView == null){
            throw new IllegalArgumentException("argument error");
        }
        /*
        这里之所以沿用原来的Glide是因为Glide原本就有二级缓存（内存+磁盘），而且十分顺滑，用户体验好
        而CacheManager虽然实现了三级缓存（内存+本地+网络），但是在滑动时会出现item图片跳变的情况，
        虽然可以通过郭霖大神的博客解决这个问题，但是会出现卡顿，因此决定还是使用Glide来实现界面图片
        的加载
         */
        //mCacheManager.display(imageView,url);
        Glide.with(context).load(url).placeholder(placeholder).error(error)
                .crossFade().into(imageView);
    }

    public static void display(Context context,final ImageView imageView,final String url){
        if(imageView == null){
            throw new IllegalArgumentException("argument error");
        }
        //mCacheManager.display(imageView,url);
        /*
        尝试使用target来实现取出bitmap用于本地缓存，但是在刷新的时候发现会有
        一点跳变，所以暂时先由原来的Glide来实现加载。
        SimpleTarget target = new SimpleTarget<GlideBitmapDrawable>() {
            @Override
            public void onResourceReady(GlideBitmapDrawable bitmapDrawable, GlideAnimation glideAnimation) {
                if(imageView != null){
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    imageView.setImageBitmap(bitmap);
                    if(mLocalCacheUtils != null && url != null && !url.equals("")){
                        mLocalCacheUtils.saveBitmapToLocal(bitmap,url);
                    }
                }
            }
        };
        */

        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.ic_image_loading)
                .error(R.mipmap.ic_image_loadfail)
                .crossFade()
                .into(imageView);
    }
}
