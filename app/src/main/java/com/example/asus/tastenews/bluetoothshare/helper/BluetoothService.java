package com.example.asus.tastenews.bluetoothshare.helper;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by ASUS on 2016/7/27.
 */
public class BluetoothService {
    private final Handler mHandler;
    private final BluetoothAdapter mAdapter;
    private int mState;

    // Name for the SDP record when creating server socket
    private static final String NAME_SECURE = "BluetoothChatSecure";
    private static final String NAME_INSECURE = "BluetoothChatInsecure";

    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3; // now connected to a remote device

    private  ConnectedThread mConnectedThread;
    private  AcceptThread mSecureAcceptThread;
    private  AcceptThread mInSecureAcceptThread;
    private  ConnectThread mConnectThread;

    public BluetoothService(Context context,Handler handler){
        mHandler = handler;
        mState = STATE_NONE;
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 用于启动服务器（安全类型的服务器/不安全类型的服务器）
     */
    public synchronized void start(){
        if(mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
        setState(STATE_LISTEN);
        if(mSecureAcceptThread == null){
            mSecureAcceptThread = new AcceptThread(true);
            mSecureAcceptThread.start();
        }
        if(mInSecureAcceptThread == null){
            mInSecureAcceptThread = new AcceptThread(false);
            mInSecureAcceptThread.start();
        }
    }

    /**
     * 用于与服务器建立连接
     * 如果有手机设备想要建立联系时，先将当前的连接断开，再重新与当前设备建立连接（原因是设定服务器只接受一个连接请求）
     * @param device
     * @param secure
     */
    public synchronized void connect(BluetoothDevice device,boolean secure){
        if(mState == STATE_CONNECTING){
            if(mConnectThread != null){
                mConnectThread.cancel();
                mConnectThread = null;
            }

            if(mConnectedThread != null){
                mConnectedThread.cancel();
                mConnectedThread = null;
            }

            mConnectThread = new ConnectThread(device,secure);//创建出socket
            mConnectThread.start();//用socket建立联系
            setState(STATE_CONNECTING);
        }
    }

    /**
     * 用于与服务器传输数据
     * @param socket
     * @param device
     * @param socketType
     */
    public synchronized void connected(BluetoothSocket socket,BluetoothDevice device,String socketType){
        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if(mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        if(mSecureAcceptThread != null){
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }
        if(mInSecureAcceptThread != null){
            mInSecureAcceptThread.cancel();
            mInSecureAcceptThread = null;
        }

        mConnectedThread = new ConnectedThread(socket,socketType);
        mConnectedThread.start();

        Message msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME,device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    /**
     * 用于停止服务器的运行以及用户与服务器建立的连接
     */
    public synchronized void stop(){
        if (mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if(mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        if(mSecureAcceptThread != null){
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }
        if(mInSecureAcceptThread != null){
            mInSecureAcceptThread.cancel();
            mInSecureAcceptThread = null;
        }
        setState(STATE_NONE);
    }

    /**
     * 用于向服务器写数据
     * @param data
     */
    public void write(byte[]data){
        ConnectedThread t;
        synchronized (this){
            if(mState != STATE_CONNECTED){
                return;
            }
            t = mConnectedThread;
        }
        t.write(data);
    }

    private synchronized void setState(int state){
        mState = state;
        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE,state,-1).sendToTarget();
    }

    public synchronized int getState(){
        return mState;
    }

    private void connectionLost(){
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST,"the device has been lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        BluetoothService.this.start();
    }

    private void connectionFail(){
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST,"cannot connect the device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        BluetoothService.this.start();
    }

    /**
     * 用于创建服务器的线程，当有socket尝试建立连接时，响应连接请求
     */
    private class AcceptThread extends Thread{
        private final BluetoothServerSocket mBluetoothServerSocket;
        private String mSocketType;

        public AcceptThread(boolean secure){
            BluetoothServerSocket tempSocket = null;
            mSocketType = secure ? "Secure":"Insecure";

            try{
                if(secure){
                    tempSocket = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE,MY_UUID_SECURE);
                }else{
                    tempSocket = mAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME_INSECURE,MY_UUID_INSECURE);
                }
            }catch(IOException e){
                e.printStackTrace();
            }
            mBluetoothServerSocket = tempSocket;
        }

        @Override
        public void run(){
            setName("AcceptThread" + mSocketType);

            BluetoothSocket socket = null;

            while(mState != STATE_CONNECTED){
                try{
                    socket = mBluetoothServerSocket.accept();
                }catch(IOException e){
                    e.printStackTrace();
                    break;
                }

                if(socket != null){
                    synchronized (BluetoothService.this){
                        switch(mState){
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                connected(socket,socket.getRemoteDevice(),mSocketType);
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                try{
                                    socket.close();
                                }catch(IOException e){
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }
            }
        }

        public void cancel(){
            try{
                mBluetoothServerSocket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 用于创建socket建立连接的线程
     */
    private class ConnectThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mmSocketType;

        public ConnectThread(BluetoothDevice device,boolean secure){
            mmDevice = device;
            mmSocketType = secure ? "Secure" : "Insecure";

            BluetoothSocket temp = null;
            try{
                if(secure)
                    temp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
                else
                    temp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE);
            }catch(IOException e){
                e.printStackTrace();
            }
            mmSocket = temp;
        }

        @Override
        public void run(){
            setName("ConnectThread" + mmSocketType);
            mAdapter.cancelDiscovery();

            try{
                mmSocket.connect();
            }catch(IOException e){
                e.printStackTrace();
                try{
                    mmSocket.close();
                }catch(IOException e1){
                    e1.printStackTrace();
                }
                connectionFail();
                return;
            }

            synchronized (BluetoothService.this){
                mConnectThread = null;
            }

            connected(mmSocket,mmDevice,mmSocketType);
        }

        public void cancel(){
            try{
                mmSocket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }


    /**
     * 连接建立成功后的传输数据线程
     */
    private class ConnectedThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmIs;
        private final OutputStream mmOs;

        public ConnectedThread(BluetoothSocket socket,String socketType){
            mmSocket = socket;
            InputStream tempIs = null;
            OutputStream tempOs = null;
            try{
                tempIs = socket.getInputStream();
                tempOs = socket.getOutputStream();
            }catch(IOException e){
                e.printStackTrace();
            }
            mmIs = tempIs;
            mmOs = tempOs;
        }

        @Override
        public void run(){
            byte[]buffer = new byte[1024];
            int bytes;

            while(mState == STATE_CONNECTED){
                try{
                    bytes = mmIs.read(buffer);
                    mHandler.obtainMessage(Constants.MESSAGE_READ,bytes,-1,buffer)
                            .sendToTarget();
                }catch(IOException e){
                    connectionLost();
                    BluetoothService.this.start();
                    break;
                }
            }
        }

        public void write(byte[]buffers){
            try{
                mmOs.write(buffers);
                mHandler.obtainMessage(Constants.MESSAGE_WRITE,-1,-1,buffers)
                        .sendToTarget();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public void cancel(){
            try{
                mmSocket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
