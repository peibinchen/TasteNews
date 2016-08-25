package com.example.asus.tastenews.news.model;

import com.example.asus.tastenews.application.NewsApplication;
import com.example.asus.tastenews.beans.CommentBean;
import com.example.asus.tastenews.beans.NewsBean;
import com.example.asus.tastenews.beans.NewsDetailBean;
import com.example.asus.tastenews.beans.UserBean;
import com.example.asus.tastenews.common.Urls;
import com.example.asus.tastenews.news.NewsJsonUtils;
import com.example.asus.tastenews.news.widget.NewsFragment;
import com.example.asus.tastenews.utils.LogUtils;
import com.example.asus.tastenews.utils.OkHttpUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by ASUS on 2016/5/24.
 */
public class NewsModelImpl implements NewsModel {

    private String docid = null;

    @Override
    public void loadNews(String url,final int type,final OnLoadNewsListListener listener){
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>(){
            @Override
            public void onSuccess(String response){
                List<NewsBean>newsBeanList = NewsJsonUtils.readJsonNewsBean(response,getID(type));
                saveNews(newsBeanList);
                listener.onSuccess(newsBeanList);
            }

            @Override
            public void onFailure(Exception e){
                listener.onFailure("load news failure",e);
            }
        };

        OkHttpUtils.get(url,callback);

//        NetworkResolver.Callback callback = new NetworkResolver.Callback(){
//            @Override
//            public void onSuccess(List<NewsBean> object) {
//                LogUtils.d("REFTEXT","list is " + object);
//                listener.onSuccess(object);
//            }
//
//            @Override
//            public void onFailure(Throwable e) {
//                LogUtils.d("REFTEXT","error in onFailure");
//                listener.onFailure(e.getMessage(),null);
//            }
//        };
//
//        NetworkResolver.getResponse(url,callback);
    }

    @Override
    public void loadNewsDetail(final String docid,final OnLoadNewsDetailListener listener){
        String url = getDetailUrl(docid);
        this.docid = docid;
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>(){
            @Override
            public void onSuccess(String response){
                final NewsDetailBean newsDetailBean = NewsJsonUtils.readJsonNewsDetailBeans(response,docid);
                BmobQuery<CommentBean> query = new BmobQuery<CommentBean>();
                //query.addWhereEqualTo("docid",docid);
                query.setLimit(5);

                String sql = "select * from CommentBean where docid = " + "'" + docid + "'" + " order by updatedAt desc limit 5";
                LogUtils.d("opopp","sql is " + sql);
                query.setSQL(sql);
                query.doSQLQuery(NewsApplication.getNewsContext(),new SQLQueryListener<CommentBean>() {
                    @Override
                    public void done(BmobQueryResult<CommentBean> bmobQueryResult, BmobException e) {
                        if(e == null){
                            List<CommentBean>list = bmobQueryResult.getResults();
                            listener.onSuccess(newsDetailBean,list);
                        }else{
                            listener.onSuccess(newsDetailBean,null);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception e){
                listener.onFailure(null,e);
            }
        };


        OkHttpUtils.get(url,loadNewsCallback);
    }

    @Override
    public void sendCommentInDetailNews(UserBean user,final String comment,final OnCommentDetailNewsListener listener){
        CommentBean commentBean = new CommentBean();
        commentBean.setComment_people(user);
        commentBean.setContent(comment);
        commentBean.setDocid(docid);
        commentBean.save(NewsApplication.getNewsContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess(comment);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(i,s);
            }
        });
    }

    private String getDetailUrl(String docid){
        StringBuffer sb = new StringBuffer(Urls.NEW_DETAIL);
        sb.append(docid).append(Urls.END_DETAIL_URL);
        return sb.toString();
    }

    private void saveNews(List<NewsBean>news_list){
//        BmobQuery<NewsBean>query = new BmobQuery<>();
//
//        for(NewsBean news : news_list){
//            news.save(NewsApplication.getNewsContext(), new SaveListener() {
//                @Override
//                public void onSuccess() {
//
//                }
//
//                @Override
//                public void onFailure(int i, String s) {
//
//                }
//            });
//        }
    }

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

    public interface OnLoadNewsListListener{
        void onSuccess(List<NewsBean> list);
        void onFailure(String msg,Exception e);
    }

    public interface OnLoadNewsDetailListener{
        void onSuccess(NewsDetailBean newsDetailBean,List<CommentBean>commentBeanList);
        void onFailure(String msg,Exception e);
    }

    public interface OnCommentDetailNewsListener{
        void onSuccess(String comment);
        void onFailure(int i,String s);
    }
}
