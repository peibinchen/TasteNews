package com.example.asus.tastenews.guide.model;

import com.example.asus.tastenews.beans.UserBean;

/**
 * Created by SomeOneInTheWorld on 2016/6/3.
 */
public interface GuideModel {
    void getUserFromCloud(UserBean user, GuideModelImpl.OnCheckUserListener listener);
}
