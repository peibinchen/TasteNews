package com.example.asus.tastenews.images.presenter;

import com.example.asus.tastenews.beans.ImageBean;
import com.example.asus.tastenews.images.model.ImageModel;
import com.example.asus.tastenews.images.model.ImageModelImpl;
import com.example.asus.tastenews.images.view.ImageView;

import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/5/29.
 */
public class ImagePresenterImpl implements ImagePresenter,ImageModelImpl.OnLoadImageListListener{

    private ImageModel mImageModel;
    private ImageView mImageView;

    public ImagePresenterImpl(ImageView imageView){
        mImageView = imageView;
        mImageModel = new ImageModelImpl();
    }

    @Override
    public void loadImageList(){
        mImageView.showProgress();
        mImageModel.loadImageList(this);
    }

    @Override
    public void onSuccess(List<ImageBean> list){
        mImageView.addImages(list);
        mImageView.hideProgress();
    }

    @Override
    public void onFailure(String msg,Exception e){
        mImageView.hideProgress();
        mImageView.showLoadFailMsg();
    }
}
