package com.example.asus.tastenews.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by ASUS on 2016/5/26.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private List<NewsBean> mData;
    private boolean mShowFooter = true;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public NewsAdapter(Context context){
        mContext = context;
    }

    public void setDatas(List<NewsBean>data){
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
            ItemViewHolder ivh = new ItemViewHolder(v);
            return ivh;
        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer,null);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            FooterViewHolder fvh = new FooterViewHolder(v);
            return fvh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int position){
        if(holder instanceof ItemViewHolder){
            NewsBean news = mData.get(position);
            if(news == null){
                return;
            }

            ((ItemViewHolder)holder).mTitle.setText(news.getTitle());
            ((ItemViewHolder)holder).mDesc.setText(news.getDigest());
            if(news.getImgsrc() == null){
                ((ItemViewHolder)holder).mNewsImg.setImageResource(R.mipmap.ic_image_white_24dp);
            }else{
                ImageLoaderUtils.display(mContext,((ItemViewHolder)holder).mNewsImg,news.getImgsrc());
            }
        }
    }

    @Override
    public int getItemCount(){
        int begin = mShowFooter ? 1 : 0;
        if(mData == null){
            return begin;
        }
        return mData.size() + begin;
    }

    public NewsBean getItem(int position){
        return mData == null ? null : mData.get(position);
    }

    public void isShowFooter(boolean showFooter){
        mShowFooter = showFooter;
    }

    public boolean isShowFooter(){
        return mShowFooter;
    }

    @Override
    public int getItemViewType(int position){
        if(!mShowFooter){
            return TYPE_ITEM;
        }

        if(position + 1 == getItemCount()){
            return TYPE_FOOTER;
        }else{
            return TYPE_ITEM;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTitle;
        public TextView mDesc;
        public ImageView mNewsImg;

        public ItemViewHolder(View v){
            super(v);
            mTitle = (TextView)v.findViewById(R.id.tvTitle);
            mDesc = (TextView)v.findViewById(R.id.tvDesc);
            mNewsImg = (ImageView)v.findViewById(R.id.ivNews);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            if(mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(v,this.getPosition());
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view){
            super(view);
        }

    }
}
