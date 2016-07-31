package com.example.asus.tastenews.bluetoothshare.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.example.asus.tastenews.bluetoothshare.model.BluetoothModel;
import com.example.asus.tastenews.bluetoothshare.model.BluetoothModelImpl;
import com.example.asus.tastenews.bluetoothshare.view.BluetoothView;

import java.util.List;

/**
 * Created by ASUS on 2016/7/26.
 */
public class BluetoothPresenterImpl implements BluetoothPresenter,BluetoothModelImpl.OnSearchDeviceListener {
    private BluetoothView mView;
    private BluetoothModel mModel;
    private Context mContext;

    public BluetoothPresenterImpl(Context context, BluetoothView view){
        mView = view;
        mModel = new BluetoothModelImpl();
        mContext = context;
    }

    @Override
    public void onFailureSearchDevice(String message) {
        mView.hideProgressbar();
        mView.showErrorMessage(message);
    }

    @Override
    public void onSuccessSearchDevice(BroadcastReceiver receiver,List<String> deviceName, List<BluetoothDevice> devices){
        mView.hideProgressbar();
        mView.shareNewsToOtherDevices(receiver,deviceName,devices);
    }

    @Override
    public void searchDevices() {
        mView.showProgressbar();
        mModel.searchDevice(mContext,this);
    }
}
