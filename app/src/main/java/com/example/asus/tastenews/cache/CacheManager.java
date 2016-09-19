package com.example.asus.tastenews.cache;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by SomeOneInTheWorld on 2016/7/19.
 * 缓存管理类，这是对外的类
 * 三级缓存：第一级是内存缓存，最快；第二级是本地缓存，中等；第三级是网络缓存，最慢
 */

public class CacheManager {
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;
    private NetworkCacheUtils mNetworkCacheUtils;

    public CacheManager(){
        mLocalCacheUtils = new LocalCacheUtils();
        mMemoryCacheUtils = new MemoryCacheUtils();
        mNetworkCacheUtils = new NetworkCacheUtils(mMemoryCacheUtils,mLocalCacheUtils);
    }

    public void display(ImageView imageView,String url){
        Bitmap bitmap;
        bitmap = mMemoryCacheUtils.getBitmapFromFromMemory(url);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }

        bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
            mMemoryCacheUtils.saveBitmapToMemory(bitmap,url);//之所以不在LocalCacheUtils的类中实现向内存添加的原因是解耦，防止在本地缓存的时候一定要内存缓存
            return;
        }

        mNetworkCacheUtils.getBitmapFromNetwork(imageView,url);//内部在查找成功后会自动向memoryCache和LocalCache添加
    }
}
