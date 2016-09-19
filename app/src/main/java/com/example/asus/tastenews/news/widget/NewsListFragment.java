package com.example.asus.tastenews.news.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.common.Urls;
import com.example.asus.tastenews.news.adapter.NewsAdapter;
import com.example.asus.tastenews.news.cache.TextLocalCache;
import com.example.asus.tastenews.news.presenter.NewsPresenter;
import com.example.asus.tastenews.news.presenter.NewsPresenterImpl;
import com.example.asus.tastenews.news.view.NewsView;
import com.example.asus.tastenews.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/5/25.
 */
public class NewsListFragment extends Fragment implements NewsView,SwipeRefreshLayout.OnRefreshListener{

  private static final String TAG = "NewsListFragment";

  private SwipeRefreshLayout mSwipeRefreshWidget;
  private RecyclerView mRecyclerView;
  private LinearLayoutManager mLayoutManager;
  private NewsAdapter mAdapter;
  private List<NewsBean> mData;
  private NewsPresenter mNewsPresenter;

  private TextLocalCache mTextLocalCache;

  private int mType = NewsFragment.NEWS_TYPE_TOP;
  private int pageIndex = 0;
  private boolean isFirstInApp = true;

  public static NewsListFragment newInstance(int type){
    NewsListFragment fragment = new NewsListFragment();
    Bundle bundle = new Bundle();
    bundle.putInt("type",type);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    mNewsPresenter = new NewsPresenterImpl(this);
    mType = getArguments().getInt("type");
    mTextLocalCache = new TextLocalCache(getContext());
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent,Bundle savedInstanceState){
//    int mode = ((NewsApplication)(getActivity().getApplication())).getMode();
//    final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(),mode);
//    LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
    View view = inflater.inflate(R.layout.fragment_newslist,parent,false);

    mSwipeRefreshWidget = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_widget);
    mSwipeRefreshWidget.setColorSchemeColors(getResources().getColor(R.color.primary),getResources().getColor(R.color.primary_dark),
        getResources().getColor(R.color.primary_light),getResources().getColor(R.color.accent));
    mSwipeRefreshWidget.setOnRefreshListener(this);

    mRecyclerView = (RecyclerView)view.findViewById(R.id.recycle_view);
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    mAdapter = new NewsAdapter(getActivity());
    mAdapter.setOnItemClickListener(mOnItemClickListener);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.addOnScrollListener(mOnScrollListener);

    firstInShowCache(NewsPresenterImpl.getTagByType(mType));
    LogUtils.d("TAGGGGGGGGG","in the onCreateView");

    LogUtils.d("TIMESTTT","last time is " + System.currentTimeMillis());
    onRefresh();
    return view;
  }

  /**
   *用于将recyclerView中的主题改变
   * @return
   */
  public RecyclerView getRecyclerView(){
    return mRecyclerView;
  }


  private void firstInShowCache(String type){
    if(mData == null){
      mData = new ArrayList<>();
    }
    mData.addAll(mTextLocalCache.getTextLocal(new String[]{type}));
    if(pageIndex == 0){
      mAdapter.setDatas(mData);
    }
    mAdapter.notifyDataSetChanged();
  }

  private RecyclerView.OnScrollListener mOnScrollListener =
      new RecyclerView.OnScrollListener() {
        private int lastVisibleItem;
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
          if(newState == RecyclerView.SCROLL_STATE_IDLE
              && lastVisibleItem + 1 == mAdapter.getItemCount()
              && mAdapter.isShowFooter()){
            mNewsPresenter.loadNews(mType,pageIndex + Urls.PAZE_SIZE);
          }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView,int dx,int dy){
          super.onScrolled(recyclerView,dx,dy);
          lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }
      };

  private NewsAdapter.OnItemClickListener mOnItemClickListener =
      new NewsAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View view,int position){
          NewsBean news = mAdapter.getItem(position);
          Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
          intent.putExtra("news",news);

          View transitionView = view.findViewById(R.id.ivNews);
          ActivityOptionsCompat options =
              ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                  transitionView,getString(R.string.transition_news_img));
          ActivityCompat.startActivity(getActivity(),intent,options.toBundle());
        }
      };

  @Override
  public void showProgress(){
    if(isFirstInApp){
      firstInShowCache(NewsPresenterImpl.getTagByType(mType));
    }
    mSwipeRefreshWidget.setRefreshing(true);
  }

  @Override
  public void hideProgress(){
    mSwipeRefreshWidget.setRefreshing(false);
  }

  @Override
  public void addNews(List<NewsBean>newsList){
    LogUtils.d("TIMESTTTT","newsList = " + newsList);
    mAdapter.isShowFooter(true);
    if(mData == null){
      mData = new ArrayList<NewsBean>();
    }

    if(isFirstInApp){
      mData.clear();
    }
    mData.addAll(newsList);
    isFirstInApp = false;

    if(newsList != null && newsList.size() > 0){
      String tag = newsList.get(0).getTag();
      mTextLocalCache.deleteAllText(new String[]{tag});
      mTextLocalCache.saveAllTextToLocal(newsList);
    }

    if(pageIndex == 0){
      mAdapter.setDatas(mData);
    }else{
      if(newsList == null || newsList.size() == 0){
        mAdapter.isShowFooter(false);
      }
      mAdapter.notifyDataSetChanged();
    }

    pageIndex += Urls.PAZE_SIZE;
  }

  @Override
  public void showLoadFailMsg(){
    if(pageIndex == 0){
      mAdapter.isShowFooter(false);
      mAdapter.notifyDataSetChanged();
    }

    View view = getActivity() == null ? mRecyclerView.getRootView()
        : getActivity().findViewById(R.id.drawer_layout);

    Snackbar.make(view,R.string.load_fail,Snackbar.LENGTH_SHORT).show();
  }

  @Override
  public void onRefresh(){
    LogUtils.d("TAGGGGGGGGG","onRefresh() and isFirstInApp = " + isFirstInApp);
    pageIndex = 0;
    if(isFirstInApp){
      mData.clear();
    }
    isFirstInApp = false;
    mNewsPresenter.loadNews(mType,pageIndex);
  }
}
