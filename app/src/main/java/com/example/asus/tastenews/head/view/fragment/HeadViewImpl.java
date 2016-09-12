package com.example.asus.tastenews.head.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.head.presenter.HeadPresenter;
import com.example.asus.tastenews.head.presenter.HeadPresenterImpl;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ASUS on 2016/6/29.
 */
public class HeadViewImpl extends Fragment implements HeadView {
    private ProgressBar mProgressBar;
    private HeadPresenter mPresenter;
    private GridView mGridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_head_select,container,false);
        if(view == null)
            return null;

        mProgressBar = (ProgressBar)view.findViewById(R.id.pb_head);
        mGridView = (GridView)view.findViewById(R.id.gv_head);

        mPresenter.loadImageFromPhone();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new HeadPresenterImpl(this);
    }

    @Override
    public void showProgressbar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showImagesInPhone(HashMap<String, List<String>> pictures) {

    }

    @Override
    public void showErrorMessage(String message) {

    }
}
