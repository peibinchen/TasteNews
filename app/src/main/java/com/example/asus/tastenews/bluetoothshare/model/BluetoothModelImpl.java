package com.example.asus.tastenews.bluetoothshare.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2016/7/26.
 */
public class BluetoothModelImpl implements  BluetoothModel {
    private List<String> mDevicesName = new ArrayList<>();
    private List<BluetoothDevice>mDevices = new ArrayList<>();
    private BluetoothReceiver mBluetoothReceiver;

    private OnSearchDeviceListener mListener;

    @Override
    public void searchDevice(Context context,OnSearchDeviceListener listener) {
//        mListener = listener;
//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if(!mBluetoothAdapter.isEnabled() && !mBluetoothAdapter.enable()){
//            listener.onFailureSearchDevice("请开启蓝牙设备后重新尝试");
//            return;
//        }
//        Intent search = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        search.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,3600);
//        context.startActivity(search);
//        mBluetoothAdapter.startDiscovery();
//
//        mBluetoothReceiver = new BluetoothReceiver();
//        IntentFilter mIntentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        context.registerReceiver(mBluetoothReceiver,mIntentFilter);
    }

    private class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_NAME);
                if(mDevicesName.indexOf(device.getName()) == -1){
                    mDevices.add(device);
                    mDevicesName.add(device.getName());
                }
                mListener.onSuccessSearchDevice(this,mDevicesName,mDevices);
            }
        }
    }

    public interface OnSearchDeviceListener{
        void onSuccessSearchDevice(BroadcastReceiver mBluetoothReceiver,List<String>deviceName,List<BluetoothDevice>devices);
        void onFailureSearchDevice(String message);
    }
}
