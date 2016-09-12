package com.example.asus.tastenews.floatingwindow;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.tastenews.application.NewsApplication;
import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.news.widget.NewsDetailActivity;
import com.example.asus.tastenews.ui.TagCloud.TagsAdapter;
import com.example.asus.tastenews.utils.LogUtils;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

/**
 * Created by ASUS on 2016/8/15.
 */
public class NewsTagsAdapter extends TagsAdapter {
    private List<String> dataSet = new ArrayList<>();
    private List<NewsBean> newsBeanList;
    private OnFloatWindowClose mListener;

    public NewsTagsAdapter(OnFloatWindowClose listener,@NonNull String... data) {
        dataSet.clear();
        Collections.addAll(dataSet, data);
        mListener = listener;
    }

    public void setDataSource(List<NewsBean> source){
        newsBeanList = source;
        dataSet.clear();
        for(NewsBean bean : source){
            dataSet.add(bean.getTitle());
        }
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public View getView(final Context context, final int position, ViewGroup parent) {
        TextView tv = new TextView(context);
        tv.setText(dataSet.get(position));
        tv.setGravity(Gravity.CENTER);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.jumpToNews(newsBeanList.get(position));
            }
        });
        tv.setTextColor(Color.WHITE);
        return tv;
    }


    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return position % 7;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {
        view.setBackgroundColor(themeColor);
    }

    public interface OnFloatWindowClose{
        void jumpToNews(NewsBean item);
    }
}
