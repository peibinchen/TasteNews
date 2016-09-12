package com.example.asus.tastenews.utils.SpeechRecognitionUtils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;

import com.example.asus.tastenews.utils.LogUtils;
import com.example.asus.tastenews.utils.SpeechRecognitionUtils.setting.IatSettings;
import com.example.asus.tastenews.utils.SpeechRecognitionUtils.util.ApkInstaller;
import com.example.asus.tastenews.utils.SpeechRecognitionUtils.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by ASUS on 2016/8/1.
 */
public class SpeechRecognitionUtils {
    private static String TAG = SpeechRecognitionUtils.class.getSimpleName();
    // 语音听写对象
    private SpeechRecognizer mIat;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private RecognizerDialog mIatDialog;
    private SharedPreferences mSharedPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 语记安装助手类
    ApkInstaller mInstaller;

    private Context mContext;
    private SpeechRecognitionCallback mCallback;

    int ret = 0; // 函数调用返回值

    public void startSpeechRecognition(Context context,SpeechRecognitionCallback callback){
        if(context == null || callback == null){
            return;
        }
        mContext = context;
        mCallback = callback;

        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        mIatDialog = new RecognizerDialog(context, mInitListener);
        mSharedPreferences = context.getSharedPreferences(IatSettings.PREFER_NAME,Activity.MODE_PRIVATE);
        mInstaller = new ApkInstaller((Activity)context);

        LogUtils.d("SPEECHT","start listening in SPeechRecognitionUtils");
        startListen();
    }

    public void stopSpeechRecognition() {
        if (mIat != null && mIat.isListening())
            mIat.stopListening();
    }

    public interface SpeechRecognitionCallback{
        void onSuccess(String result);
        void onFailure(String errorMessage);
    }

    /*******************************************内部实现***************************************************/

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            mCallback.onFailure(error.getMessage());
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            LogUtils.d("SPEECHT", results.getResultString());
            mCallback.onSuccess(getResult(results));

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            showTip("当前正在说话，音量大小：" + volume);
            LogUtils.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    public void startListen(){
        FlowerCollector.onEvent(mContext, "iat_recognize");
        LogUtils.d("SPEECHT","in startlisten()");
//        mResultText.setText(null);// 清空显示内容
        mIatResults.clear();
        // 设置参数
        setParam();
        boolean isShowDialog = mSharedPreferences.getBoolean(
                "pref_key_iat_show",false);
        if (isShowDialog) {
            // 显示听写对话框
            mIatDialog.setListener(mRecognizerDialogListener);
            mIatDialog.show();
            LogUtils.d("SPEECHT","is showing dialog");
        } else {
            // 不显示听写对话框
            ret = mIat.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                mCallback.onFailure("听写失败,错误码：" + ret);
            }
        }
    }

    private void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "0"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }

    private String getResult(RecognizerResult results) {
        LogUtils.d("SPEECHT","getResult()");
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        LogUtils.d("SPEECHT","the result is " + resultBuffer.toString());
        return resultBuffer.toString();
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            mCallback.onSuccess(getResult(results));
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            mCallback.onFailure(error.getPlainDescription(true));
        }
    };

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            LogUtils.d("SPEECHT", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                mCallback.onFailure("初始化失败，错误码：" + code);
            }
        }
    };

}
