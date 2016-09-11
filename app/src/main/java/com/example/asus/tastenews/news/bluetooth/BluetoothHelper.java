package com.example.asus.tastenews.news.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.asus.tastenews.news.Constants;
import com.example.asus.tastenews.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by ASUS on 2016/8/9.
 */
public class BluetoothHelper {
    private BluetoothAdapter mBluetoothAdapter;
    private List<String>mDevicesList = new ArrayList<>();
    private final BluetoothCallback mListener;
    private Context mContext;
    private CommunicationService mCommnuicationService;

    private final String TAG = "TESTREAD";

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d(TAG,"onReceive in BluetoothHelper");
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    mDevicesList.add(device.getName() + "\n" + device.getAddress());
                }
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())){
                mListener.finishDiscover(mDevicesList);
            }
        }
    };

    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case Constants.MESSAGE_LOG:
                    String log = (String)msg.obj;
                    Toast.makeText(mContext,log,Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_READ:
                    byte[] buffers = (byte[])msg.obj;
                    Toast.makeText(mContext,"read is " + new String(buffers),Toast.LENGTH_LONG).show();
                    mListener.read(new String(buffers));
                    break;
                case Constants.MESSAGE_WEITE:
                    byte[] write = (byte[])msg.obj;
                    Toast.makeText(mContext,"write is " + new String(write),Toast.LENGTH_LONG).show();
                    mListener.write(new String(write));
                    break;
                case Constants.MESSAHE_DEVICE_NAME:
                    Toast.makeText(mContext,msg.getData().getString(Constants.DEVICE_NAME),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public BluetoothHelper(Context context,BluetoothCallback listener){
        LogUtils.d(TAG,"BluetoothHelper()");
        mListener = listener;
        mContext = context;

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver,filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mReceiver,filter);

        setupAdapter();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice device: pairedDevices){
                mDevicesList.add(device.getName() + "\n" + device.getAddress());
            }
        }

        ensureDiscovrable();
        doDiscovery();
    }

    public void sendMessage(String message){
        LogUtils.d(TAG,"sendMessage in BluetoothHelper" + "  state = " + mCommnuicationService.getState());
        if(mCommnuicationService == null || mCommnuicationService.getState() != CommunicationService.STATE_TRANSFORM_DATA){
            Toast.makeText(mContext,"state is not connected",Toast.LENGTH_SHORT).show();
            return;
        }
        mCommnuicationService.write(message.getBytes());
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        LogUtils.d(TAG,"onActivityResult in BluetoothHelper and requestCode is " + requestCode +
        "  resultCode = " + resultCode);
        switch(requestCode){
            case Constants.RESULT_ENABLE:
                if(resultCode == Activity.RESULT_OK){
                    setupService();
                }else{
                    Toast.makeText(mContext,"not enable",Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }

    public void connectToDevice(int position){
        LogUtils.d(TAG,"connectToDevice in BluetoothHelper and mDevicesList size is " + mDevicesList.size());
        mBluetoothAdapter.cancelDiscovery();

        if(mDevicesList.size() == 0)
            return;
        if(position < 0 || position > mDevicesList.size() - 1){
            position = 0;
        }

        String info = mDevicesList.get(position);
        String address = info.substring(info.length() - 17);

        LogUtils.d(TAG,"address is " + address);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if(mCommnuicationService == null){
            LogUtils.d(TAG,"service is null in the helper");
            return;
        }
        mCommnuicationService.connect(device);
    }

    public void onResume(){
        if(mCommnuicationService != null){
            LogUtils.d(TAG,"onResume() in BluetoothHelper and service is not null");
            if(mCommnuicationService.getState() == CommunicationService.STATE_NONE){
                mCommnuicationService.start();
            }
        }
    }

//    public void onPause(){
//        try{
//            mContext.unregisterReceiver(mReceiver);
//        }catch(RuntimeException e){
//            e.printStackTrace();
//        }
//    }

    public void onDestroy(){
        LogUtils.d(TAG,"onDestroy() in BluetoothHelper");
        if(mBluetoothAdapter != null){
            mBluetoothAdapter.cancelDiscovery();
        }

        try{
            mContext.unregisterReceiver(mReceiver);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }


        if(mCommnuicationService != null){
            mCommnuicationService.stop();
        }
    }

    public interface BluetoothCallback{
        void beginToDiscover();
        void finishDiscover(List<?>result);
        void read(String message);
        void write(String message);
    }

    private void setupService(){
        mCommnuicationService = new CommunicationService(mContext,mHandler);
        mCommnuicationService.start();
    }

    private void setupAdapter(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null){
            Toast.makeText(mContext,"not support bluetooth",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!mBluetoothAdapter.isEnabled()){
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            ((Activity)mContext).startActivityForResult(enableIntent, Constants.RESULT_ENABLE);
            mBluetoothAdapter.enable();
        }else if(mCommnuicationService == null){
            setupService();
        }
    }

    private void doDiscovery(){
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        mListener.beginToDiscover();
        while(!mBluetoothAdapter.startDiscovery()){
            try{
                Thread.sleep(100);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

    }

    private void ensureDiscovrable(){
        if(mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
            mContext.startActivity(discoverableIntent);
        }
    }
}
