package com.example.asus.tastenews.bluetoothshare.view;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;

import java.util.List;

/**
 * Created by ASUS on 2016/7/26.
 */
public interface BluetoothView {
    void showProgressbar();
    void hideProgressbar();
    void shareNewsToOtherDevices(BroadcastReceiver receiver,List<String> deviceName, List<BluetoothDevice> devices);
    void showErrorMessage(String msg);
}
