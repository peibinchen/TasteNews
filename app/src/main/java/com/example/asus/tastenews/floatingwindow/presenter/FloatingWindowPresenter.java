package com.example.asus.tastenews.floatingwindow.presenter;

/**
 * Created by ASUS on 2016/7/18.
 */
public interface FloatingWindowPresenter {
    void loadNewsInFloatingWindow(int type,int page);
    void loadNewsDetailInFloatingWindow(String docid);
}
