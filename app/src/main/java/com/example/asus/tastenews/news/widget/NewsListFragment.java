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

import com.example.asus.tastenews.news.adapter.NewsAdapter;
import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.common.Urls;
import com.example.asus.tastenews.news.presenter.NewsPresenter;
import com.example.asus.tastenews.news.presenter.NewsPresenterImpl;
import com.example.asus.tastenews.news.view.NewsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2016/5/25.
 */
public class NewsListFragment extends Fragment implements NewsView,SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "NewsListFragment";

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    private List<NewsBean> mData;
    private NewsPresenter mNewsPresenter;

    private int mType = NewsFragment.NEWS_TYPE_TOP;
    private int pageIndex = 0;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_newslist,null);

        mSwipeRefreshWidget = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshWidget.setColorSchemeColors(R.color.primary,R.color.primary_dark,
                R.color.primary_light,R.color.accent);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new NewsAdapter(getActivity().getApplicationContext());
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        onRefresh();

        return view;
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
        mSwipeRefreshWidget.setRefreshing(true);
    }

    @Override
    public void hideProgress(){
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    public void addNews(List<NewsBean>newsList){
        mAdapter.isShowFooter(true);
        if(mData == null){
            mData = new ArrayList<NewsBean>();
        }
        mData.addAll(newsList);
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
        pageIndex = 0;
        if(mData != null){
            mData.clear();
        }

        mNewsPresenter.loadNews(mType,pageIndex);
    }

}
