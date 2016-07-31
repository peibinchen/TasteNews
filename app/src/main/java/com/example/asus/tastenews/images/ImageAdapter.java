package com.example.asus.tastenews.images;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.ImageBean;
import com.example.asus.tastenews.utils.ImageLoaderUtils;
import com.example.asus.tastenews.utils.ToolUtils;

import java.util.List;

/**
 * Created by ASUS on 2016/5/29.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ItemViewHolder>{

    private List<ImageBean> mDatas;
    private Context mContext;
    private int mMaxHeight;
    private int mMaxWidth;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public ImageAdapter(Context context){
        mContext = context;
        mMaxHeight = ToolUtils.getHeightInPx(context) - ToolUtils.getStatusHeight(context)
                -ToolUtils.dip2px(context,96);
        mMaxWidth = ToolUtils.getWidthInPx(context) - 20;
    }

    public void setDatas(List<ImageBean>beans){
        mDatas = beans;
        this.notifyDataSetChanged();
    }

    @Override
    public ImageAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent,int type){
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image,parent,false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder,int position){
        ImageBean bean = mDatas.get(position);
        if(bean == null){
            return;
        }
        holder.mTitle.setText(bean.getTitle());
        float scale = (float)bean.getWidth() / (float)mMaxHeight;
        int height = (int)(bean.getHeight() / scale);
        if(height > mMaxHeight){
            height = mMaxHeight;
        }
        holder.mImage.setLayoutParams(new LinearLayout.LayoutParams(mMaxWidth,height));
        ImageLoaderUtils.display(mContext,holder.mImage,bean.getThumburl());
    }

    @Override
    public int getItemCount(){
        if(mDatas == null){
            return 0;
        }else{
            return mDatas.size();
        }
    }

    public ImageBean getItem(int position){
        return mDatas == null ? null : mDatas.get(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle;
        private ImageView mImage;

        public ItemViewHolder(View v){
            super(v);
            mTitle = (TextView)v.findViewById(R.id.tvTitle);
            mImage = (ImageView)v.findViewById(R.id.ivImage);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            if(mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(view,this.getPosition());
            }
        }
    }
}
