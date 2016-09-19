package com.example.asus.tastenews.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by SomeOneInTheWorld on 2016/8/8.
 */
public class ShareUtils implements SensorEventListener {
    private Context mContext;

    public ShareUtils(Context context){
        super();
        mContext = context;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        float[] values = event.values;

        if(sensorType == Sensor.TYPE_ACCELEROMETER){
            /**
             * 正常情况下是9.78-10左右
             */
            if(Math.abs(values[0]) > 13 || Math.abs(values[1]) > 13 || Math.abs(values[2]) > 13){
                /**
                 * 进行蓝牙传输新闻
                 */
                LogUtils.d("SHAKE_SHAKE","is shaking");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //当传感器精度发生变化时调用此方法
    }
}
