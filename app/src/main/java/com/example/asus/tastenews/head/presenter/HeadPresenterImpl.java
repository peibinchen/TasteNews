package com.example.asus.tastenews.head.presenter;

import android.os.Handler;

import com.example.asus.tastenews.head.model.HeadModel;
import com.example.asus.tastenews.head.model.HeadModelImpl;
import com.example.asus.tastenews.head.view.fragment.HeadView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ASUS on 2016/6/12.
 */
public class HeadPresenterImpl implements HeadPresenter,HeadModelImpl.OnFindPictureListener {
    private HeadView mView;
    private HeadModel mModel;

    private HashMap<String,List<String>> mPictures;

    public HeadPresenterImpl(HeadView view){
        mView = view;
        mModel = new HeadModelImpl();
    }

    @Override
    public void loadImageFromPhone() {
        mView.showProgressbar();
        mModel.getImageFromPhone(this);
    }


    @Override
    public void onSuccessFind(HashMap<String, List<String>> pictures) {
        mView.hideProgressbar();
        mView.showImagesInPhone(pictures);
    }

    @Override
    public void onFailureFind(String message) {
        mView.hideProgressbar();
        mView.showErrorMessage(message);
    }
}
