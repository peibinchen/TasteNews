package com.example.asus.tastenews.news.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.QuestionTable;
import com.example.asus.tastenews.beans.ReplyTable;
import com.example.asus.tastenews.news.widget.CommentAddFragment;
import com.example.asus.tastenews.news.widget.CommentToQuestionFragment;
import com.example.asus.tastenews.news.widget.DetailFragment;
import com.example.asus.tastenews.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by SomeOneInTheWorld on 2016/8/24.
 */
public class QuestionAdapter<T> extends RecyclerView.Adapter<QuestionAdapter<T>.MyViewHolder> {
  private LayoutInflater mInflater;
  private Context mContext;
  private List<T> mTable = new ArrayList<>();
  private MyViewHolder mHolder;
  public static final String TAG = "RECYCLERTEXT";

  public QuestionAdapter(Context context){
    mContext = context;
    this.mInflater = LayoutInflater.from(context);
  }


  public QuestionAdapter(Context context, List<T> list){
    this(context);
    mTable = list;
  }

  public void setDataSets(List<T>list) {
    LogUtils.d(TAG,"setDataSets and list size = " + list.size());
    mTable.clear();
    mTable.addAll(list);
    notifyDataSetChanged();
  }

  @Override
  public void onBindViewHolder(QuestionAdapter<T>.MyViewHolder holder, int position) {
    LogUtils.d(TAG, "onBindViewHolder() and position = " + position);
    T table = mTable.get(position);
    if (table instanceof QuestionTable) {
      final QuestionTable questionTable = (QuestionTable) table;
      handleQuestion(questionTable,holder,position);
    }else if(table instanceof ReplyTable){
      ReplyTable replyTable = (ReplyTable)table;
      handleAnswer(replyTable,holder,position);
    }
  }

  private void handleQuestion(final QuestionTable questionTable,QuestionAdapter<T>.MyViewHolder holder,int position){
    holder.contentTV.setText(questionTable.getQuestion_content());
    holder.nameTV.setText(questionTable.getQuestionerId().getUsername());
    holder.answerBT.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CommentAddFragment.EXTRA_USERBEAN, questionTable.getNewsId());
        bundle.putSerializable(CommentAddFragment.EXTRA_QUESTIONTABLE, questionTable);
        CommentAddFragment fragment = new CommentAddFragment();
        fragment.setArguments(bundle);
        if (mContext instanceof AppCompatActivity) {
          AppCompatActivity activity = (AppCompatActivity) mContext;
          FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
          transaction.replace(R.id.frame_comment_content, fragment);
          transaction.commit();
        }
      }
    });
    holder.cardView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CommentAddFragment.EXTRA_USERBEAN, questionTable.getNewsId());
        bundle.putSerializable(CommentAddFragment.EXTRA_QUESTIONTABLE, questionTable);
        CommentToQuestionFragment fragment = new CommentToQuestionFragment();
        fragment.setArguments(bundle);
        if (mContext instanceof AppCompatActivity) {
          AppCompatActivity activity = (AppCompatActivity) mContext;
          FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
          transaction.replace(R.id.frame_comment_content, fragment);
          transaction.commit();
        }
      }
    });
  }

  private void handleAnswer(final ReplyTable replyTable,QuestionAdapter<T>.MyViewHolder holder,int position){
    holder.nameTV.setText(replyTable.getReplyerId().getUsername());
    holder.contentTV.setText(replyTable.getReply_content());
    holder.answerBT.setText("更多");
    holder.answerBT.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DetailFragment.DETAIL_CONTENT, replyTable);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(bundle);
        if (mContext instanceof AppCompatActivity) {
          AppCompatActivity activity = (AppCompatActivity) mContext;
          FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
          transaction.replace(R.id.frame_comment_content, fragment);
          transaction.commit();
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    if(mTable == null)
      return 0;
    LogUtils.d(TAG, "getItemCount() and item count = " + mTable.size());
    return mTable.size();
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
    View view = mInflater.inflate(R.layout.item_comment_main,parent,false);
    mHolder = new MyViewHolder(view);
    return mHolder;
  }


  public class MyViewHolder extends RecyclerView.ViewHolder{
    private TextView nameTV;
    private TextView contentTV;
    private FancyButton answerBT;
    private CardView cardView;

    public MyViewHolder(View view){
      super(view);
      cardView = (CardView)view.findViewById(R.id.comment_cardview);
      nameTV = (TextView)view.findViewById(R.id.tv_name);
      contentTV = (TextView)view.findViewById(R.id.tv_content);
      answerBT = (FancyButton)view.findViewById(R.id.fbt_comment_submit);
    }
  }
}
