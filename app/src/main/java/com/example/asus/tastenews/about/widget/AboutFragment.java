package com.example.asus.tastenews.about.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.asus.tastenews.R;

/**
 * Created by ASUS on 2016/6/1.
 * 测试成功，可以成功读取json，现总结如下：
 * 如果网页的json数据不是单纯的{a:a1 b:b1}，而是{ [ ] }这种出现中括号的
 * 更普通的说，就是json数据中包括了list，
 * 这种情况下，就不能只是根据list来进行对应类的生成，
 * 而应该将整个json分成多个类，最后通过总的一个类传递到retrofit中
 */
public class AboutFragment extends Fragment{
//        implements BluetoothHelper.BluetoothCallback{
//    private BluetoothHelper mBluetoothHelper;
    private ProgressBar mProgressBar;


//    @Override
//    public void beginToDiscover() {
//        mProgressBar.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void finishDiscover(List<?> result) {
//        mProgressBar.setVisibility(View.GONE);
//        mBluetoothHelper.connectToDevice(result.size()-1);
//    }
//
//    @Override
//    public void read(String message) {
//
//    }
//
//    @Override
//    public void write(String message) {
//
//    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_about, null);
//        Button button = (Button)view.findViewById(R.id.server_bt);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mBluetoothHelper.sendMessage("this is successful");
//            }
//        });
//        mProgressBar = (ProgressBar)view.findViewById(R.id.progress_wait);
//        mBluetoothHelper = new BluetoothHelper(getContext(),this);
//        return view;

        View view = inflater.inflate(R.layout.fragment_test,container,false);

        return view;
    }



    @Override
    public void onResume(){
        super.onResume();
//        mBluetoothHelper.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
//        mBluetoothHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        mBluetoothHelper.onActivityResult(requestCode,resultCode,data);
    }
}
