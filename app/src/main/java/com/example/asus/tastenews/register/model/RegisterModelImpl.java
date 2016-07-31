package com.example.asus.tastenews.register.model;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.asus.tastenews.beans.UserBean;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by ASUS on 2016/6/4.
 */
public class RegisterModelImpl implements RegisterModel {

    @Override
    public void register(UserBean user,Context context,final OnRegisterListener listener){
        user.signUp(context.getApplicationContext(), new SaveListener() {
            @Override
            public void onSuccess() {
               listener.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(i,s);
            }
        });
    }

    public interface OnRegisterListener{
        void onSuccess();
        void onFailure(int i,String s);
    }
}
