package com.example.asus.tastenews.head.view.fragment;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ASUS on 2016/6/12.
 */
public interface HeadView {
    void showProgressbar();
    void hideProgressbar();
    void showImagesInPhone(HashMap<String,List<String>> pictures);
    void showErrorMessage(String message);
}
