package com.example.asus.tastenews.news.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.beans.QuestionTable;
import com.example.asus.tastenews.beans.ReplyTable;
import com.example.asus.tastenews.beans.ReplyToReplyerTable;
import com.example.asus.tastenews.news.presenter.CommentPresenter;
import com.example.asus.tastenews.news.presenter.CommentPresenterImpl;
import com.example.asus.tastenews.news.view.CommentView;
import com.example.asus.tastenews.utils.LogUtils;

import java.util.List;

/**
 * Created by ASUS on 2016/8/23.
 */

//因为showProgressBar和hideProgressBar应该由子类决定，
//因此声明为abstract，才能让有些方法让子类实现
public abstract class BaseFragment extends Fragment implements CommentView{
    public static final String TESTTAG = "odsjfaisdfow";
    private QuestionCallback mCallback;
    private NewsBean mNewsBean;
    private NewsBean tempBean;

    private CommentPresenter mPresenter;

    private final String TAG = "BASEFRAGMENT_TEST";

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof QuestionCallback){
            mCallback = (QuestionCallback)context;
        }
        tempBean = null;
        if(mCallback != null){
            tempBean = mCallback.getNewsBean();
            LogUtils.d(TAG,"bean is " + tempBean.getDigest());
        }
        mPresenter = new CommentPresenterImpl(context,this);
    }

    protected CommentPresenter getPresenter(){
        return mPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.d(BaseFragment.TESTTAG,"onCreateView");
        View view = super.onCreateView(inflater, container, savedInstanceState);
        LogUtils.d(TAG,"tempbean is " + tempBean);
        if(tempBean != null){
            mPresenter.queryNewsBeanByPresenter(tempBean.getDocid(),tempBean);
        }
        return view;
    }

    public NewsBean getMyNewsBean(){
        LogUtils.d(BaseFragment.TESTTAG,"getMyNewsBean");
        return mNewsBean;
    }

    public QuestionCallback getCallback(){
        return mCallback;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        if(mCallback != null){
            mCallback = null;
        }
    }

    @Override
    public void saveQuestionAnswerSuccess() {

    }

    @Override
    public void saveQuestionAnswerFailure(int errorCode, String message) {

    }

    @Override
    public void saveQuestionSuccess() {

    }

    @Override
    public void saveQuestionFailure(int errorCode, String message) {

    }

    @Override
    public void queryQuestionSuccess(List<QuestionTable> list) {

    }

    @Override
    public void queryQuestionFailure(int errorCode, String message) {

    }

    @Override
    public void queryAnswersSuccess(List<ReplyTable> list) {

    }

    @Override
    public void queryAnswersFailure(int errorCode, String message) {

    }

    @Override
    public void queryReply2ReplyerSuccess(List<ReplyToReplyerTable> list) {

    }

    @Override
    public void queryReply2ReplyerFailure(int errorCode, String message) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void queryNewsBeanSuccess(NewsBean bean) {
        LogUtils.d(TAG,"query successful bean is " + bean);
        mNewsBean = bean;
    }

    @Override
    public void saveNewsBeanSuccess(NewsBean bean) {
        LogUtils.d(TAG,"save successful bean is " + bean);
        mNewsBean = bean;
    }

    @Override
    public void saveNewsBeanFailure(int errorCode, String message) {
        Toast.makeText(getContext(),"save bean failure and errorCode = " + errorCode + "   message = " + message,Toast.LENGTH_LONG).show();
    }

    public interface QuestionCallback{
        NewsBean getNewsBean();
    }
}
