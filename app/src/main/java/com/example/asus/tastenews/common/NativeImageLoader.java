package com.example.asus.tastenews.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

import com.example.asus.tastenews.images.model.ImageModelImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ASUS on 2016/6/12.
 */
public class NativeImageLoader {
    private LruCache<String,Bitmap> mCache;
    private NativeImageLoader mInstance;
    private ExecutorService mImageThreadPool = Executors.newFixedThreadPool(1);

    private NativeImageLoader(){
        final int maxsize = (int)(Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxsize / 4;

        mCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key,Bitmap value){
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    public NativeImageLoader getInstance(){
        if(mInstance == null){
            mInstance = new NativeImageLoader();
        }
        return mInstance;
    }

    public void loadNativeImage(String path, OnLoadImageListener listener){
        this.loadNativeImage(path,null,listener);
    }

    public void loadNativeImage(final String path, final Point size,final OnLoadImageListener callback){
        Bitmap bitmap = mCache.get(path);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch(msg.what){
                    case Urls.LOAD_IMAGE_SUCCESS:
                        callback.onImageLoad((Bitmap)msg.obj,path);
                        break;
                    case Urls.LOAD_IMAGE_FAILURE:
                        callback.onImageLoadFailure((String)msg.obj);
                        break;
                }

            }
        };

        if(bitmap == null){
            mImageThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap bp = decodeThumbBitmapInFile
                            (path,size == null ? 0 : size.x,size == null ? 0 : size.y);
                    if(null != bp){
                        Message msg = new Message();
                        msg.obj = bp;
                        msg.what = Urls.LOAD_IMAGE_SUCCESS;
                        handler.sendMessage(msg);
                        mCache.put(path,bp);
                    }else{
                        Message msg = new Message();
                        msg.what = Urls.LOAD_IMAGE_FAILURE;
                        msg.obj = Urls.LOAD_IMAGE_FAILURE_MESSAGE;
                        handler.sendMessage(msg);
                    }
                }
            });
        }else{
            callback.onImageLoad(bitmap,path);
        }
    }

    private Bitmap decodeThumbBitmapInFile(String path,int width,int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        options.inSampleSize = getScaleSize(options,width,height);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path,options);
    }

    private int getScaleSize(BitmapFactory.Options options,int width,int height){
        int imgWidth = options.outWidth;
        int imgHeight = options.outHeight;
        int scale = 1;
        if(imgHeight > height || imgWidth > width){
            int widthScale = Math.round((float)imgWidth /(float) width);
            int heightScale = Math.round((float)imgHeight /(float) height);
            scale = widthScale < heightScale ? widthScale : heightScale;
        }
        return scale;
    }

    public interface OnLoadImageListener{
        void onImageLoad(Bitmap bitmap,String path);
        void onImageLoadFailure(String message);
    }
}
