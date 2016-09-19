package com.example.asus.tastenews.floatingwindow.model;
/**
 * Created by SomeOneInTheWorld on 2016/7/18.
 */
public interface FloatingWindowModel {
    void loadNews(int type,int page,FloatingWindowModelImpl.OnLoadNewsListListener listener);
    void loadNews(String url,int type,FloatingWindowModelImpl.OnLoadNewsListListener listener);
    void loadNewsDetail(String docid,FloatingWindowModelImpl.OnLoadNewsDetailListener listener);
}
