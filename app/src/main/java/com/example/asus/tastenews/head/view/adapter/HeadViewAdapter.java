package com.example.asus.tastenews.head.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.application.NewsApplication;
import com.example.asus.tastenews.news.widget.NewsFragment;
import com.example.asus.tastenews.ui.MyImageView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ASUS on 2016/6/29.
 */
public class HeadViewAdapter extends BaseAdapter {

    private Context mContext;
    private HashMap<String, List<String>> mPictures;

    public HeadViewAdapter(Context context,HashMap<String, List<String>> pictures){
        mContext = context;
        mPictures = pictures;
    }

    @Override
    public int getCount() {
        return mPictures.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_head_select,null);
            viewHolder.mImageView = (MyImageView)convertView.findViewById(R.id.miv_tophead);
            viewHolder.mTextView = (TextView)convertView.findViewById(R.id.tv_headcount);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        return null;
    }


    private class ViewHolder{
        MyImageView mImageView;
        TextView mTextView;
    }
}
