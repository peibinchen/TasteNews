package com.example.asus.tastenews.news.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.utils.ImageLoaderUtils;
import com.example.asus.tastenews.utils.LogUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by SomeOneInTheWorld on 2016/5/26.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final String TAG = "NewsTestAdapter";
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private List<NewsBean> mData;
    private HashMap<String,TypedValue> mThemes;
    private boolean mShowFooter = true;
    private Context mContext;
    private int mode;

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

    public void setThemes(HashMap<String,TypedValue>themes){
        LogUtils.d(TAG,"setThemes()");
//        LogUtils.d(TAG,"themes is " + themes.get(MainActivity.THEME_BACKGROUND));
        if(mThemes == null){
            mThemes = themes;
            notifyDataSetChanged();
            return;
        }
        mThemes.clear();
        mThemes.putAll(themes);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        LogUtils.d(TAG,"onCreateViewHolder()");
//        mode = ((NewsApplication)(((Activity)mContext).getApplication())).getMode();
//        final Context contextThemeWrapper = new ContextThemeWrapper(mContext,mode);
//        LayoutInflater localInflater = LayoutInflater.from(mContext).cloneInContext(contextThemeWrapper);

        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_news,null);
            ItemViewHolder ivh = new ItemViewHolder(v);
            return ivh;
        }else{
            View v = LayoutInflater.from(mContext).inflate(R.layout.footer,null);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            FooterViewHolder fvh = new FooterViewHolder(v);
            return fvh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int position){
        LogUtils.d(TAG,"onBindViewHolder()");
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

            if(mThemes == null || mThemes.size() == 0){
                LogUtils.d(TAG,"theme is null is null is null is null");
                return;
            }

            //换肤功能：
            //这里不能直接使用resouceId去调用函数，而是要resources.getColor()再去调用函数，
            //不然就会出现没法更换主题的情况
//            Resources resources = mContext.getResources();
//            ((ItemViewHolder)holder).mTitle.setTextColor(resources.getColor(mThemes.get(MainActivity.THEME_TEXT).resourceId));
//            ((ItemViewHolder)holder).mDesc.setTextColor(resources.getColor(mThemes.get(MainActivity.THEME_TEXT_SECOND).resourceId));
//            ((ItemViewHolder)holder).mCardView.setBackgroundResource(mThemes.get(MainActivity.THEME_BACKGROUND).resourceId);
        }
    }

    @Override
    public int getItemCount(){
        LogUtils.d(TAG,"getItemCount()");
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
        LogUtils.d(TAG,"getItemViewType()");
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
        public CardView mCardView;

        public ItemViewHolder(View v){
            super(v);
            mTitle = (TextView)v.findViewById(R.id.tvTitle);
            mDesc = (TextView)v.findViewById(R.id.tvDesc);
            mNewsImg = (ImageView)v.findViewById(R.id.ivNews);
            mCardView = (CardView)v.findViewById(R.id.cardView);
//            initItemLayout();
            v.setOnClickListener(this);
        }

        private void initItemLayout() {
            TypedValue backgroundColor = new TypedValue();
            TypedValue textColor = new TypedValue();
            TypedValue textPrimaryColor = new TypedValue();
            Resources.Theme theme = mContext.getTheme();
            theme.resolveAttribute(R.attr.colorBackground, backgroundColor, true);
            theme.resolveAttribute(R.attr.colorText, textColor, true);
            theme.resolveAttribute(R.attr.colorTextPrimary, textPrimaryColor, true);

            mCardView.setBackgroundResource(backgroundColor.resourceId);
            mTitle.setTextColor(mContext.getResources().getColor(textColor.resourceId));
            mDesc.setTextColor(mContext.getResources().getColor(textPrimaryColor.resourceId));
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
