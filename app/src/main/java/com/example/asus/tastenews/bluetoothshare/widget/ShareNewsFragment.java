package com.example.asus.tastenews.bluetoothshare.widget;



import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.bluetoothshare.helper.BluetoothService;
import com.example.asus.tastenews.bluetoothshare.helper.Constants;
import com.example.asus.tastenews.bluetoothshare.presenter.BluetoothPresenter;
import com.example.asus.tastenews.bluetoothshare.presenter.BluetoothPresenterImpl;
import com.example.asus.tastenews.bluetoothshare.view.BluetoothView;
import com.example.asus.tastenews.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ASUS on 2016/7/26.
 */
public class ShareNewsFragment extends Fragment{
//    private BluetoothPresenter mPresenter;
//    private ProgressBar mProgressbar;
//    private BroadcastReceiver mReceiver;
//    private BluetoothDevice mTargetDevice;
//    private BluetoothSocket mSocket;
//    private boolean isConnect;

    private final int REQUEST_ENABLE = 0x19;
    private List<BluetoothDevice>mDevicesList = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothService mService;
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case Constants.MESSAGE_FIND_DEVICE_SUCCESS:
                    LogUtils.d("FIND_SUCCESS","the device is " + mDevicesList.get(0));
                    connectDevice(mDevicesList.get(0).getAddress(),true);
                    ShareNewsFragment.this.sendMessage("hello cpb");
                    break;
                case Constants.MESSAGE_READ:
                    LogUtils.d("FIND_SUCCESS","receiver message is " + msg.arg1);
            }
        }
    };

    private final static int READ_MESSAGE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBluetoothFilter();
        //mPresenter = new BluetoothPresenterImpl(getContext(),this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_news,null);
//        mProgressbar = (ProgressBar)view.findViewById(R.id.share_progress);
//        mPresenter.searchDevices();
        doDiscovery();
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        if(!mBluetoothAdapter.isEnabled()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE);
        }else{
            if(mService == null){
                setupCommunition();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == REQUEST_ENABLE){
            if(resultCode == Activity.RESULT_OK){
                setupCommunition();
            }else{
                Toast.makeText(getContext(),"请允许蓝牙开启才能正常工作",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupCommunition(){
        mService = new BluetoothService(getContext(),mHandler);
    }

    private void sendMessage(String message){
        if(mService.getState() != BluetoothService.STATE_CONNECTED){
            Toast.makeText(getContext(),"请连接蓝牙成功后重试",Toast.LENGTH_SHORT).show();
            return;
        }

        if(message.length() > 0){
            byte[]buffers = message.getBytes();
            mService.write(buffers);
        }
    }

    private void connectDevice(String address,boolean secure){
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mService.connect(device,secure);
    }

    private void registerBluetoothFilter(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getContext().registerReceiver(mBroadcastReceiver,filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getContext().registerReceiver(mBroadcastReceiver,filter);

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                mDevicesList.add(device);
            }
        }
    }

    private void doDiscovery(){
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    public void onStop() {
        if(mBluetoothAdapter != null){
            mBluetoothAdapter.cancelDiscovery();
        }
        getContext().unregisterReceiver(mBroadcastReceiver);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if(mBluetoothAdapter != null){
            mBluetoothAdapter.cancelDiscovery();
        }
        getContext().unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_NAME);
                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    mDevicesList.add(device);
                }
                mHandler.sendEmptyMessage(Constants.MESSAGE_FIND_DEVICE_SUCCESS);
            }
        }
    };
//
//    @Override
//    public void showProgressbar() {
//        mProgressbar.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void hideProgressbar() {
//        mProgressbar.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void shareNewsToOtherDevices(BroadcastReceiver receiver,List<String> deviceName, List<BluetoothDevice> devices) {
//        mReceiver = receiver;
//        mTargetDevice = devices.get(0);
//        connectToTargetDevice("hello");
//    }
//
//    @Override
//    public void showErrorMessage(String msg) {
//
//    }
//
//    @Override
//    public void onDestroy() {
//        getContext().unregisterReceiver(mReceiver);
//        super.onDestroy();
//    }
//
//    @Override
//    public void onDetach() {
//        getContext().unregisterReceiver(mReceiver);
//        super.onDetach();
//    }
//
//    @Override
//    public void onDestroyView() {
//        getContext().unregisterReceiver(mReceiver);
//        super.onDestroyView();
//    }
//
//    private void connectToTargetDevice(final String message){
//        new Thread(){
//            @Override
//            public void run(){
//                BluetoothSocket tempSocket = null;
//                Method method;
//                try{
//                    method = mTargetDevice.getClass().getMethod("createRfcommSocket",new Class[]{int.class});
//                    tempSocket = (BluetoothSocket)method.invoke(mTargetDevice,1);
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//                mSocket = tempSocket;
//                try{
//                    mSocket.connect();
//                    isConnect = true;
//                    communicateToTargetDevice(message);
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    private void communicateToTargetDevice(String message){
//        if(isConnect){
//            try{
//                OutputStream os = mSocket.getOutputStream();
//                os.write(getHexBytes(message));
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//            try{
//                InputStream in = mSocket.getInputStream();
//                int data;
//                while(true){
//                    try{
//                        data = in.read();
//                        Message msg = mHandler.obtainMessage();
//                        msg.what = READ_MESSAGE;
//                        msg.arg1 = data;
//                        mHandler.sendMessage(msg);
//                    }catch(IOException e){
//                        e.printStackTrace();
//                        break;
//                    }
//                }
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//        }
//        if(mSocket != null){
//            try{
//                mSocket.close();
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private byte[] getHexBytes(String message){
//        int len = message.length() / 2;
//        char[]chars = message.toCharArray();
//        String[]hexStr = new String[len];
//        byte[]bytes = new byte[len];
//        for(int i=0,j=0;j<len;i+=2,j++){
//            hexStr[j] = chars[i] + chars[i+1] + "";
//            bytes[j] = (byte) Integer.parseInt(hexStr[j],16);
//        }
//        return bytes;
//    }

}
