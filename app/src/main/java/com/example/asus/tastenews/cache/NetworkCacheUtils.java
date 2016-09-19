package com.example.asus.tastenews.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.asus.tastenews.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SomeOneInTheWorld on 2016/7/19.
 */
public class NetworkCacheUtils {
    private MemoryCacheUtils mMemoryCacheUtils;
    private LocalCacheUtils mLocalCacheUtils;

    public NetworkCacheUtils(MemoryCacheUtils memoryCacheUtils,LocalCacheUtils localCacheUtils){
        this.mMemoryCacheUtils = memoryCacheUtils;
        this.mLocalCacheUtils = localCacheUtils;
    }

    public void getBitmapFromNetwork(ImageView imageView,String url){
        new NetworkDownLoadTask().execute(imageView,url);
    }

    /**
     * AsyncTask的第一个参数为输入参数
     * 第二个参数是进度参数
     * 第三个参数是返回的结果参数
     */
    class NetworkDownLoadTask extends AsyncTask<Object,Void,Bitmap>{
        private ImageView imageView;
        private String url;
        @Override
        protected Bitmap doInBackground(Object... params) {
            imageView = (ImageView)params[0];
            url = (String)params[1];
            LogUtils.d("TAGGGG","url is " + url);
            return downloadBitmap(url);
        }

        /**
         * 这个方法用于更新主线程中的UI
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        /**
         * doInBackground后即调用此方法
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null){
                LogUtils.d("TAGGG","success");
                imageView.setImageBitmap(bitmap);
                //将网络加载的图片保存到本地和内存
                mMemoryCacheUtils.saveBitmapToMemory(bitmap,url);//使用LruCache
                mLocalCacheUtils.saveBitmapToLocal(bitmap,url);//使用File并且用MD5算法加密
            }
        }

        private Bitmap downloadBitmap(String url){
            HttpURLConnection connection = null;
            try{
                connection = (HttpURLConnection)new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);
                connection.connect();

                int responseCode = connection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    InputStream is = connection.getInputStream();
                    byte[] data = getBytes(is);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeByteArray(data,0,data.length,options);
                    LogUtils.d("TAGGGG","options.width = " + options.outWidth + "    options.height = " + options.outHeight);
                    options.inSampleSize = caculateScaleSize(options.outWidth,options.outHeight,imageView.getWidth(),imageView.getHeight());
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap =  BitmapFactory.decodeByteArray(data,0,data.length,options);
                    LogUtils.d("TAGGG","bitmap is " + bitmap);
                    return bitmap;
                }
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                try{
                    if(connection != null)
                        connection.disconnect();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        private byte[] getBytes(InputStream is)throws IOException{
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffers = new byte[1024];
            int len = -1;
            while((len = is.read(buffers)) != -1){
                os.write(buffers,0,len);
            }
            os.close();
            return os.toByteArray();
        }

        private int caculateScaleSize(int originalWidth,int originalHeight,int targetWidth,int targetHeight) {
            int scaleWidth = (int) Math.ceil((originalWidth * 1.0f) / targetWidth);
            int scaleHeight = (int) Math.ceil((originalHeight * 1.0f) / targetHeight);
            return Math.max(scaleHeight, scaleWidth);
        }
    }
}
