package com.example.asus.tastenews.floatingwindow.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.tastenews.R;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by ASUS on 2016/7/1.
 */
public class FloatingFragment extends Fragment {

    private NewsTagService mService;
    private Intent mServiceIntent;
    private ServiceConnection mConnection;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_floating_window,container,false);
        mContext = getActivity();
        bindService();

        FancyButton fb = (FancyButton) view.findViewById(R.id.bt_switch2floatingwindow);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startService(mServiceIntent);
                if(mService != null){
                    mService.show();
                }
            }
        });
        return view;
    }

    private void bindService(){
        mServiceIntent = new Intent(mContext,NewsTagService.class);
        if(mConnection == null){
            mConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mService = ((NewsTagService.FloatingNewsBinder)service).getService();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };

            mContext.bindService(mServiceIntent,mConnection,mContext.BIND_AUTO_CREATE);
        }
    }

    private void unbindService(){
        if(null != mConnection){
            mContext.unbindService(mConnection);
            mConnection = null;
        }
    }

    @Override
    public void onDestroy(){
        unbindService();
        super.onDestroy();
    }

    @Override
    public void onPause(){
        unbindService();
        super.onPause();
    }

    @Override
    public void onStop(){
        unbindService();
        super.onStop();
    }

    @Override
    public void onResume(){
        bindService();
        super.onResume();
    }
}
