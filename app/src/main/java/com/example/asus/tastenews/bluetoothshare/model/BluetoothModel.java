package com.example.asus.tastenews.bluetoothshare.model;

import android.content.Context;

/**
 * Created by ASUS on 2016/7/26.
 */
public interface BluetoothModel {
    void searchDevice(Context context, BluetoothModelImpl.OnSearchDeviceListener listener);
}
