package com.example.asus.tastenews.main.widget;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.UserBean;
import com.example.asus.tastenews.guide.widget.GuideFragment;
import com.example.asus.tastenews.news.widget.NewsFragment;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by ASUS on 2016/6/5.
 */
public class LoadActivity extends AppCompatActivity {

    private static final int LOADING_TIME = 2500;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.activity_guide_page);

        BmobConfig config =new BmobConfig.Builder(this)
                ////设置appkey
                .setApplicationId("ab58538db241fd9bf86baff8753e46f3")
                ////请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                ////文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024*1024)
                ////文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*
                Intent intent = new Intent(LoadActivity.this,MainActivity.class);
                LoadActivity.this.startActivity(intent);
                LoadActivity.this.finish();
                */
                UserBean bmobUser = UserBean.getCurrentUser(LoadActivity.this,UserBean.class);
                if(bmobUser != null){
                    Intent intent = new Intent(LoadActivity.this,MainActivity.class);
                    LoadActivity.this.startActivity(intent);
                    LoadActivity.this.finish();
                }else{
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_content,new GuideFragment()).commit();
                }


            }
        },LOADING_TIME);
    }
}
