package com.example.asus.tastenews.floatingwindow.model;

import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.beans.NewsDetailBean;
import com.example.asus.tastenews.common.Urls;
import com.example.asus.tastenews.news.NewsJsonUtils;
import com.example.asus.tastenews.news.widget.NewsFragment;
import com.example.asus.tastenews.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by ASUS on 2016/7/17.
 * 重复利用news包中的函数，用于加载网络新闻
 */

public class FloatingWindowModelImpl implements FloatingWindowModel{

    @Override
    public void loadNews(int type,int page,final OnLoadNewsListListener listener){
        String url = getUrl(type,page);
        loadNews(url,type,listener);
    }

    @Override
    public void loadNews(String url,final int type,final OnLoadNewsListListener listener){
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>(){
            @Override
            public void onSuccess(String response){
                List<NewsBean>newsBeanList = NewsJsonUtils.readJsonNewsBean(response,getID(type));
                listener.onSuccess(newsBeanList);
            }

            @Override
            public void onFailure(Exception e){
                listener.onFailure("load news failure",e);
            }
        };

        OkHttpUtils.get(url,callback);
    }

    @Override
    public void loadNewsDetail(final String docid,final OnLoadNewsDetailListener listener){
        String url = getDetailUrl(docid);
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>(){
            @Override
            public void onSuccess(String response){
                final NewsDetailBean newsDetailBean = NewsJsonUtils.readJsonNewsDetailBeans(response,docid);
                listener.onSuccess(newsDetailBean);
            }

            @Override
            public void onFailure(Exception e){
                listener.onFailureDetail(null,e);
            }
        };


        OkHttpUtils.get(url,loadNewsCallback);
    }

    /****************************内部细节************************************/

    private String getID(int type){
        String id;
        switch(type) {
            case NewsFragment.NEWS_TYPE_TOP:
                id = Urls.TOP_ID;
                break;
            case NewsFragment.NEWS_TYPE_NBA:
                id = Urls.NBA_ID;
                break;
            case NewsFragment.NEWS_TYPE_JOKES:
                id = Urls.JOKE_ID;
                break;
            case NewsFragment.NEWS_TYPE_CARS:
                id = Urls.CAR_ID;
                break;
            default:
                id = Urls.TOP_ID;
                break;
        }
        return id;
    }

    private String getDetailUrl(String docid){
        StringBuffer sb = new StringBuffer(Urls.NEW_DETAIL);
        sb.append(docid).append(Urls.END_DETAIL_URL);
        return sb.toString();
    }

    private String getUrl(int type,int page){
        StringBuffer sb = new StringBuffer();
        switch(type){
            case NewsFragment.NEWS_TYPE_TOP:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            case NewsFragment.NEWS_TYPE_NBA:
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            case NewsFragment.NEWS_TYPE_JOKES:
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            case NewsFragment.NEWS_TYPE_CARS:
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
        }
        sb.append('/').append(page).append(Urls.END_URL);
        return sb.toString();
    }


    /***************************对外接口******************************/

    public interface OnLoadNewsListListener{
        void onSuccess(List<NewsBean> list);
        void onFailure(String msg,Exception e);
    }

    public interface OnLoadNewsDetailListener{
        void onSuccess(NewsDetailBean newsDetailBean);
        void onFailureDetail(String msg,Exception e);
    }
}
