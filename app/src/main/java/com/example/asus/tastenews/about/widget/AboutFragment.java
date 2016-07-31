package com.example.asus.tastenews.about.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.WechatIInfoBean;
import com.example.asus.tastenews.network.NetworkResolver;
import com.example.asus.tastenews.ui.FloatingMenu.ArcMenu;
import com.example.asus.tastenews.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASUS on 2016/6/1.
 * 测试成功，可以成功读取json，现总结如下：
 * 如果网页的json数据不是单纯的{a:a1 b:b1}，而是{ [ ] }这种出现中括号的
 * 更普通的说，就是json数据中包括了list，
 * 这种情况下，就不能只是根据list来进行对应类的生成，
 * 而应该将整个json分成多个类，最后通过总的一个类传递到retrofit中
 */
public class AboutFragment extends Fragment {
    private final String APP_KEY = "f3369c472eeae6d409340454358b7b61";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Map params = new HashMap();//请求参数
        params.put("pno","");//当前页数，默认1
        params.put("ps","");//每页返回条数，最大100，默认20
        params.put("key",APP_KEY);//应用APPKEY(应用详细页查询)
        params.put("dtype","");//返回数据的格式,xml或json，默认json

        NetworkResolver.Callback callback = new NetworkResolver.Callback() {
            @Override
            public void onSuccess(WechatIInfoBean object) {
                for(int i = 0;i<object.getResult().getList().size();i++){
                    LogUtils.d("RETR"," i = " + i + "   " + object.getResult().getList().get(i).getTitle());
                }
            }

            @Override
            public void onFailure(Throwable e) {

            }
        };
        NetworkResolver.getResponse("http://v.juhe.cn/weixin/",params,callback);
        return inflater.inflate(R.layout.fragment_about, null);
    }
}
