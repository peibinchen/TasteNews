package com.example.asus.tastenews.news.widget;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.QuestionTable;
import com.example.asus.tastenews.news.adapter.QuestionAdapter;
import com.example.asus.tastenews.news.presenter.CommentPresenter;
import com.example.asus.tastenews.utils.LogUtils;

import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/8/28.
 */
public class NewsCommentMainFragment extends BaseFragment {
  private ProgressBar mProgressBar;
  private RecyclerView mRecyclerView;
  private QuestionAdapter<QuestionTable> mQuestionAdapter;

  private boolean isQueryNewsBean = true;

  private String TAG = "COMMENT_TEST";

  private CommentPresenter mPresenter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_comment_main,container,false);
    mPresenter = getPresenter();
    initLayout(view);

    super.onCreateView(inflater, container, savedInstanceState);
    return view;
  }

  private void initLayout(View view) {
    mQuestionAdapter = new QuestionAdapter<QuestionTable>(getContext());
    mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_commentmain);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecyclerView.setAdapter(mQuestionAdapter);

    mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
  }

  @Override
  public void showProgress() {
    mProgressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideProgress() {
    mProgressBar.setVisibility(View.GONE);
    if (isQueryNewsBean)
      mPresenter.showQuestionsByPresenter(getMyNewsBean());
    isQueryNewsBean = false;
  }

  @Override
  public void queryQuestionSuccess(List<QuestionTable> list) {
    if(list.size() == 0){
      Toast.makeText(getContext(),"暂无评论。",Toast.LENGTH_SHORT).show();
      return;
    }
    mQuestionAdapter.setDataSets(list);
  }

  @Override
  public void queryQuestionFailure(int errorCode, String message) {
    Toast.makeText(getContext(),"评论区貌似出了点问题，请稍后再试。",Toast.LENGTH_SHORT).show();
    LogUtils.d(TAG, "errorCode = " + errorCode + " and message is " + message);
  }
}
