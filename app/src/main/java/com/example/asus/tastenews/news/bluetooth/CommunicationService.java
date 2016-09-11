package com.example.asus.tastenews.news.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.news.Constants;
import com.example.asus.tastenews.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by ASUS on 2016/8/9.
 * 用于管理服务器和客户端的socket连接和数据传输
 */
public class CommunicationService {
    public final static int STATE_NONE = 0;
    public final static int STATE_LISTENING = 1;
    public final static int STATE_CONNECTING = 2;
    public final static int STATE_TRANSFORM_DATA = 3;

    private int mState = STATE_NONE;
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final Handler mHandler;
    private final Context mContext;
    private final BluetoothAdapter mBluetoothAdapter;

    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private ListeningThread mListeningThread;

    private final String TAG = "TESTREAD";

    public CommunicationService(Context context,Handler handler){
        LogUtils.d(TAG,"CommnuicationService()");
        mHandler = handler;
        mContext = context;
        mState = STATE_NONE;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private synchronized void setState(int state){
        mState = state;
    }

    public synchronized int getState(){
        return mState;
    }

    /**
     * start（）用于将之前的全部线程和socket关闭
     *
     * 刚开始调用CommnuicationService，如果之前有其他的地方已经在
     * 使用这个service，则将service中的全部传输的线程ConnectedThread和
     * 连接线程ConnectThread全部关闭，只保留监听线程ListeningThread；
     * 如果只是第一次启动，则是创造ListeningThread线程，在这个线程中开启了
     * 一个serverSocket充当服务器，用于监听socket并根据自己的状态做出相应的反映
     *
     * 当serverSocket接受到一个socket时，
     * 如果service的状态处于正在监听（也就是说还没有其他socket与其连接）---> STATE_LISTENING
     * 或者处于正在连接（也就是有其他线程的socket正在尝试连接）----> STATE_CONNECTING:
     * 这两种情况下，考虑到目前serverSocket已经接受到一个socket，因此直接进入connected()，开启
     * ConnectedThread线程用于数据传输。
     *
     * 如果service的状态处于STATE_NONE，意味着还没调用start（）函数，就意味着这个调用很可能
     * 没有先将其他线程都关掉，这种情况下不允许接受socket
     * 如果service的状态处于STATE_TRANSFORM_DATA，意味着当前已经有个socket用于传输数据了，
     * 这种情况下没有必要再创造一个connectedThread线程用于传输数据
    */
    public synchronized void start(){
        LogUtils.d(TAG,"start() in CommnuicationService");
        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if(mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        setState(STATE_LISTENING);

        if(mListeningThread == null){
            mListeningThread = new ListeningThread();
            mListeningThread.start();
        }
    }

    /**
     * connect（）函数用于根据一个device创造出相应的ConnectThread线程用于尝试和服务器连接
     * @param device
     */
    public synchronized void connect(BluetoothDevice device){
        /**
         * STATE_CONNECTING只有在connect中才会被设置，
         * 因此如果state==STATE_CONNECTING，意味着事先已经有device建立了ConnectThread线程，
         * 并且这个device并没有下一步的动作（和服务器传输数据），
         * 这个时候就应该将之前的device的线程释放，为当前的device提供新的ConnectThread线程
         */

        LogUtils.d(TAG,"connect in CommnuicationService");
        if(mState == STATE_CONNECTING){
            if(mConnectThread != null){
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }
        if(mConnectedThread != null){
            mConnectedThread = null;
        }
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        mState = STATE_CONNECTING;
    }

    /**
     * 根据已经接受的socket和对应的设备进行数据传输
     * @param socket
     * @param device
     */
    public synchronized void connected(BluetoothSocket socket,BluetoothDevice device){
        LogUtils.d(TAG,"connected in CommunicationService");
        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if(mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        if(mListeningThread != null){
            mListeningThread.cancel();
            mListeningThread = null;
        }

        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        Message msg = mHandler.obtainMessage(Constants.MESSAHE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME,device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        setState(STATE_TRANSFORM_DATA);
    }

    /**
     * 停止全部socket和线程
     */
    public synchronized void stop(){
        LogUtils.d(TAG,"stop() in CommnuicationService");
        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if(mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        if(mListeningThread != null){
            mListeningThread.cancel();
            mListeningThread = null;
        }
        setState(STATE_NONE);
    }

    public void write(byte[]datas){
        LogUtils.d(TAG,"write() in CommnuicationService");
        ConnectedThread r;
        synchronized (this){
            if(mState != STATE_TRANSFORM_DATA) return;
            r = mConnectedThread;
        }
        r.write(datas);
    }

    private void connectFailed(){
        mHandler.obtainMessage(Constants.MESSAGE_LOG,-1,-1,"connectFailed");
    }

    private void connectLost(){
        mHandler.obtainMessage(Constants.MESSAGE_LOG,-1,-1,"connectLost");
    }

    private class ListeningThread extends Thread{
        private final BluetoothServerSocket mServerSocket;

        public ListeningThread(){
            LogUtils.d(TAG,"ListeningThread()");
            BluetoothServerSocket tempSocket = null;
            try{
                tempSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(mContext.getString(R.string.app_name),uuid);
            }catch(IOException e){
                e.printStackTrace();
            }
            mServerSocket = tempSocket;
        }

        @Override
        public void run(){
            LogUtils.d(TAG,"run in ListeningThread");
            BluetoothSocket socket = null;
            while(mState != STATE_TRANSFORM_DATA){
                try{
                    LogUtils.d(TAG,"serverSocket.accept()");
                    //accept（）函数阻断，直到成功接受socket
                    socket = mServerSocket.accept();
                }catch(IOException e){
                    LogUtils.d(TAG,"serverSocket.accept() and exception is " + e.getMessage());
                    e.printStackTrace();
                }

                LogUtils.d(TAG,"before if(socket != null)");
                if(socket != null){
                    LogUtils.d(TAG,"socket is not null");
                    synchronized(CommunicationService.this){
                        switch(mState){
                            //在服务器监听或者其他客户端正在连接时，因为在这个时候服务器已经接收到了一个socket，
                            //因此其他的客户端全部停止，已连接的这个socket直接进入传输数据阶段
                            case STATE_LISTENING:
                            case STATE_CONNECTING:
                                connected(socket,socket.getRemoteDevice());
                                break;
                            //在STATE_NONE情况下说明serverSocket还没有调用到start函数
                            //在STATE_TRANSFORM_DATA情况下说明当前已经存在一个socket用于传输数据了
                            case STATE_NONE:
                            case STATE_TRANSFORM_DATA:
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
                mServerSocket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * ConnectThread为客户端主动和服务器建立连接的一个线程
     */
    public class ConnectThread extends Thread{
        private BluetoothDevice targetDevice;
        private BluetoothSocket socket;

        public ConnectThread(BluetoothDevice device){
            targetDevice = device;
            BluetoothSocket tempSocket = null;
            try{
                tempSocket = targetDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            }catch(IOException e){
                e.printStackTrace();
            }
            socket = tempSocket;
            LogUtils.d(TAG,"socket is " + socket);
        }

        @Override
        public void run(){
            mBluetoothAdapter.cancelDiscovery();
            try{
                LogUtils.d(TAG,"socket.connect()");
                //connect()函数会阻塞函数，直到connect成功或失败
                socket.connect();
            }catch(IOException e){
                LogUtils.d(TAG,"socket.connect() failed and exception is " + e.getMessage());
                e.printStackTrace();
                try{
                    socket =(BluetoothSocket) targetDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(targetDevice,2);
                    Thread.sleep(500);
                    socket.connect();
                }catch(Exception e1){
                    e1.printStackTrace();
                    LogUtils.d(TAG,"the next exception is " + e1.getMessage());
                    try{
                        socket.close();
                    }catch(IOException e2){
                        e2.printStackTrace();
                    }
                    connectFailed();
                    return;
                }
            }
            synchronized (CommunicationService.this){
                mConnectThread = null;
            }
            connected(socket,targetDevice);
        }

        public void cancel(){
            try{
                socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 在建立起连接后用于传输数据的线程
     */
    private class ConnectedThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInputStream;
        private final OutputStream mmOutputStream;

        public ConnectedThread(BluetoothSocket socket){
            mmSocket = socket;
            InputStream is = null;
            OutputStream os = null;
            try{
                is = socket.getInputStream();
                os = socket.getOutputStream();
            }catch(IOException e){
                e.printStackTrace();
            }
            mmInputStream = is;
            mmOutputStream = os;
        }

        @Override
        public void run(){
            byte[]buffer = new byte[1024];
            int bytes;
            while(true){
                try{
                    bytes = mmInputStream.read(buffer);
                    mHandler.obtainMessage(Constants.MESSAGE_READ,bytes,-1,buffer).sendToTarget();
                }catch(IOException e){
                    e.printStackTrace();
                    connectLost();
                    CommunicationService.this.start();
                    break;
                }
            }
        }

        public void write(byte[]bytes){
            try{
                LogUtils.d(TAG,"write in service connectedThread");
                mmOutputStream.write(bytes);
                mHandler.obtainMessage(Constants.MESSAGE_WEITE,-1,-1,bytes).sendToTarget();
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
