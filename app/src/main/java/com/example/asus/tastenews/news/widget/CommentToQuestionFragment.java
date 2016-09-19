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
import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.beans.QuestionTable;
import com.example.asus.tastenews.beans.ReplyTable;
import com.example.asus.tastenews.news.adapter.QuestionAdapter;
import com.example.asus.tastenews.news.presenter.CommentPresenter;
import com.example.asus.tastenews.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/8/28.
 */
public class CommentToQuestionFragment extends BaseFragment{
  private RecyclerView mRecyclerView;
  private QuestionAdapter<ReplyTable> mReplyAdapter;
  private ProgressBar mProgressBar;

  private QuestionTable mQuestionTable;
  private List<ReplyTable> mReplyTables;
  private CommentPresenter mPresenter;
  private NewsBean mNewsBean;

  private String TAG = "COMQUESTF";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_comment_toquestion,container,false);

    Bundle bundle = getArguments();
    mQuestionTable = (QuestionTable)bundle.getSerializable(CommentAddFragment.EXTRA_QUESTIONTABLE);
    LogUtils.d(TAG,"questionTable is " + mQuestionTable);
    mNewsBean = (NewsBean)bundle.getSerializable(CommentAddFragment.EXTRA_USERBEAN);

    initLayout(view);
    mPresenter = getPresenter();
    mPresenter.showQuestionAnswersByPresenter(mQuestionTable);

    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstance){
    super.onActivityCreated(savedInstance);
    if(savedInstance != null){
      if(mReplyTables == null){
        mReplyTables = new ArrayList<>();
      }
      for(String key : savedInstance.keySet()){
        mReplyTables.add((ReplyTable)savedInstance.get(key));
      }
      if(mReplyAdapter != null){
        mReplyAdapter.setDataSets(mReplyTables);
      }
    }
  }

  @Override
  public void onSaveInstanceState(Bundle bundle){
    super.onSaveInstanceState(bundle);
    for(int i = 0;i<mReplyTables.size();i++){
      bundle.putSerializable("reply" + i,mReplyTables.get(i));
    }
  }

  private void initLayout(View view){
    if(view == null){
      return;
    }

    mProgressBar = (ProgressBar)view.findViewById(R.id.progress);

    mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_commenttoquestion);
    mReplyAdapter = new QuestionAdapter<ReplyTable>(getContext());
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecyclerView.setAdapter(mReplyAdapter);
  }

  @Override
  public void queryAnswersSuccess(List<ReplyTable> list) {
    if(list.size() == 0){
      Toast.makeText(getContext(),"暂时无人评论",Toast.LENGTH_SHORT).show();
      return;
    }
    mReplyTables = list;
    mReplyAdapter.setDataSets(list);
  }

  @Override
  public void queryAnswersFailure(int errorCode, String message) {
    Toast.makeText(getContext(),"queryAnswersFailure fail and errorCode = " + errorCode,Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showProgress() {
    mProgressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideProgress() {
    mProgressBar.setVisibility(View.GONE);
  }
}
