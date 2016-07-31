package com.example.asus.tastenews.guide.model;

import android.content.Context;

import com.example.asus.tastenews.beans.UserBean;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by ASUS on 2016/6/3.
 */
public class GuideModelImpl implements GuideModel {

    private Context mContext;

    public GuideModelImpl(Context context){
        mContext = context;
    }

    @Override
    public void getUserFromCloud(UserBean user,final OnCheckUserListener listener){
        user.login(mContext,  new SaveListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(s);
            }
        });
    }

    public interface OnCheckUserListener{
        void onSuccess();
        void onFailure(String message);
    }
}
