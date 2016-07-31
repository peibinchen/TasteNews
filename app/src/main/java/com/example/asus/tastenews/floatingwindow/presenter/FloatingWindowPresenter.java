package com.example.asus.tastenews.floatingwindow.presenter;

import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.beans.NewsDetailBean;

import java.util.List;

/**
 * Created by ASUS on 2016/7/18.
 */
public interface FloatingWindowPresenter {
    void loadNewsInFloatingWindow(int type,int page);
    void loadNewsDetailInFloatingWindow(String docid);
}
