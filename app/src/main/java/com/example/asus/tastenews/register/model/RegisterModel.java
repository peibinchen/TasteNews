package com.example.asus.tastenews.register.model;

import android.content.Context;

import com.example.asus.tastenews.beans.UserBean;

/**
 * Created by SomeOneInTheWorld on 2016/6/4.
 */
public interface RegisterModel {
    void register(UserBean user, Context context, RegisterModelImpl.OnRegisterListener listener);
}
