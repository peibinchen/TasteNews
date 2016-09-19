package com.example.asus.tastenews.images.view;

import com.example.asus.tastenews.beans.ImageBean;

import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/5/29.
 */
public interface ImageView {
    void addImages(List<ImageBean> images);
    void showProgress();
    void hideProgress();
    void showLoadFailMsg();
}
