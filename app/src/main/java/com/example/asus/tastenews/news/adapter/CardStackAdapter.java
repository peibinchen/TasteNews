package com.example.asus.tastenews.news.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.QuestionTable;
import com.example.asus.tastenews.beans.ReplyTable;
import com.example.asus.tastenews.news.helper.FullyLinearLayoutManager;
import com.example.asus.tastenews.news.widget.CommentAreaFragment;
import com.example.asus.tastenews.utils.LogUtils;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by ASUS on 2016/8/23.
 */
public class CardStackAdapter extends StackAdapter<QuestionTable> {

  private final String TAG = "tuuuurr";
  private OnSwitch2CommentAddFragment mListener;
  private CardStackView.ViewHolder mHolder;

  public CardStackAdapter(Context context,OnSwitch2CommentAddFragment listener) {
    super(context);
    mListener = listener;
  }

  @Override
  public void bindView(QuestionTable data, int position, CardStackView.ViewHolder holder) {
    if (holder instanceof ColorItemViewHolder) {
      ColorItemViewHolder h = (ColorItemViewHolder) holder;
      h.onBind(data, position);
      mHolder = h;
    }
  }

  public void updateReplys(List<ReplyTable>list){
    if (mHolder instanceof ColorItemViewHolder) {
      ColorItemViewHolder h = (ColorItemViewHolder) mHolder;
      h.onUpdateDatas(list);
    }
  }

  @Override
  protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
    View view = getLayoutInflater().inflate(R.layout.list_card_item, parent, false);
    return new ColorItemViewHolder(view);
  }

  @Override
  public int getItemViewType(int position) {
    return R.layout.list_card_item;
  }

  class ColorItemViewHolder extends CardStackView.ViewHolder {
    View mLayout;
    View mContainerContent;
    TextView mTextTitle;
    TextView mTextQuestionDescription;
    RecyclerView mRecyclerView;
    FancyButton mCommentButton;
    QuestionTable mQuestionTable;
    QuestionAdapter mQuestionAdapter;
    boolean isFirst = true;

    public ColorItemViewHolder(View view) {
      super(view);
      mLayout = view.findViewById(R.id.frame_list_card_item);
      mContainerContent = view.findViewById(R.id.container_list_content);
      mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
      mTextQuestionDescription = (TextView)view.findViewById(R.id.tv_content);
      mCommentButton = (FancyButton)view.findViewById(R.id.bt_comment);
      mCommentButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mListener.switch2CommentAddFragment(mQuestionTable);
        }
      });
      mRecyclerView = (RecyclerView)view.findViewById(R.id.recycle_view);
    }

    @Override
    public void onItemExpand(boolean b) {
      LogUtils.d(QuestionAdapter.TAG,"onItemExpand and b = " + b);
      mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
      mCommentButton.setVisibility(b ? View.VISIBLE : View.GONE);
      mRecyclerView.setVisibility(b ? View.VISIBLE : View.GONE);
      if(b){
        mListener.getReplyTableByQuestion(mQuestionTable);
      }
    }

    public void onUpdateDatas(List<ReplyTable>list){
      LogUtils.d(QuestionAdapter.TAG,"onUpdateDatas and isFirst = " + isFirst );
      if (isFirst){
        LogUtils.d(TAG,"thread one  is " + Thread.currentThread().toString());
        mQuestionAdapter = new QuestionAdapter(getContext(),list);
        mRecyclerView.setAdapter(mQuestionAdapter);
//        setRecyclerViewHeightByMyself(mRecyclerView);
        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager(getContext()));
        isFirst = false;
      }else{
        LogUtils.d(TAG,"thread two is " + Thread.currentThread().toString());
        mQuestionAdapter.setDataSets(list);
        mQuestionAdapter.notifyDataSetChanged();
      }
    }

    private void setRecyclerViewHeightByMyself(RecyclerView recyclerView){
      RecyclerView.Adapter adapter = recyclerView.getAdapter();
      if(adapter == null){
        return;
      }
      int totalHeight = 0;
      for(int i=0;i<adapter.getItemCount();i++){
        View item = recyclerView.getChildAt(i);
        item.measure(0,0);
        totalHeight += item.getMeasuredHeight();
      }

      ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
      params.height = totalHeight;
      recyclerView.setLayoutParams(params);
      recyclerView.requestLayout();
    }

    public void onBind(QuestionTable questionTable, int position) {
      LogUtils.d(QuestionAdapter.TAG,"onBind and position = " + position);
      mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), CommentAreaFragment.BACKGROUND_DATAS[position % CommentAreaFragment.BACKGROUND_DATAS.length]), PorterDuff.Mode.SRC_IN);
      mTextTitle.setText(questionTable.getQuestion_content());//问题的标题描述
      mTextQuestionDescription.setText(questionTable.getQuestion_description());
      mQuestionTable = questionTable;
    }
  }

  /**
   * 此接口用于和添加评论的fragment传递QuestionTable
   */
  public interface OnSwitch2CommentAddFragment{
    void switch2CommentAddFragment(QuestionTable table);
    void getReplyTableByQuestion(QuestionTable table);
  }
}