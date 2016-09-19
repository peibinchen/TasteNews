package com.example.asus.tastenews.news.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.ReplyTable;
import com.example.asus.tastenews.news.presenter.CommentPresenter;

/**
 * Created by SomeOneInTheWorld on 2016/8/28.
 */
public class DetailFragment extends BaseFragment {
  private TextView mTextView;
  private CommentPresenter mPresenter;

  private ReplyTable mReplyTable;

  private static final String TAG = "DetailTest";
  public static final String DETAIL_CONTENT = "detail_content";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_detail,container,false);
    mTextView = (TextView)view.findViewById(R.id.detail_tv);

    Bundle bundle = getArguments();
    mReplyTable = (ReplyTable) bundle.getSerializable(DETAIL_CONTENT);
    mTextView.setText(mReplyTable == null ? "no reply" : mReplyTable.getReply_content());
    return view;
  }
}
