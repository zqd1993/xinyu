package com.live.vquonline.base.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.UserManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bytedance.hume.readapk.HumeSDK;
import com.kwai.monitor.payload.TurboHelper;
import com.live.vquonline.base.BaseApplication;
import com.live.vquonline.service.common.ICommonService;
import com.tencent.vasdolly.helper.ChannelReaderUtil;

/**
 * Created by abby on 2018/4/15.
 */

public class DeviceManager {
    private String mImei = "unknown";
    private int timestamp = 0;
    private String mVersion = "unknown";
    private static String mToken = "";
    private Context mContext;
    private int openGreen;


    private String mac;
    private String oaid = "";
    private static DeviceManager mInstance;

    public static DeviceManager getInstance() {
        if (null == mInstance) {
            mInstance = new DeviceManager();
        }

        return mInstance;
    }

    public int getOpenGreen() {
        return openGreen;
    }

    public void setOpenGreen(int openGreen) {
        this.openGreen = openGreen;
    }

    public int getTimestamp() {
        if (timestamp == 0 && mContext != null) {
            return SpUtils.INSTANCE.getInt("timestamp", 0);
        }
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    private DeviceManager() {

    }

    public void init(Context context) {
        mContext = context;
    }


    public String getDeviceImei() {
        if (null != mContext) {
            mImei = Settings.System.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        return mImei;
    }

    public String getAppVersion() {
        if (null == mContext) {
            return mVersion;
        }

        try {
            mVersion = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }

        return mVersion;
    }

    public String getPhoneVersion() {
        String mVersionCode = "88";
        if (null == mContext) {
            return mVersionCode;
        }
        try {
            mVersionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {

        }
        return mVersionCode + "";
    }

    String channelName = null;

    public String getChannel() {
        if (TextUtils.isEmpty(channelName)) {
            channelName = "22"; //默认官方包 1
        }
        return channelName;
    }

    public void setChannelName(String channelName) {
        //获取抖音分包渠道号
        String channel = HumeSDK.getChannel(mContext);
        Log.d("channel", "douyin channel = " + channel);
        if(channel == null){
            channel = "";
        }
        if (channel.equals("")) {
            //获取快手分包渠道号
            channel = TurboHelper.getChannel(mContext);
            Log.d("channel", "kuaishou channel = " + channel);
            if (channel.equals("")) {
                //获取腾讯广告分包渠道号
                channel = ChannelReaderUtil.getChannel(mContext);
                if (channel == null) {
                    channel = "";
                }
                Log.d("channel", "tencent channel = " + channel);
            }
        }
        if (channel.equals("")) {
            this.channelName = channelName;
        } else {
            this.channelName = channel;
        }
    }


    public String getAiJiaMiChannel() {
        String channelName = "1";//默认官方渠道号 1
        try {

            PackageManager packageManager = mContext.getPackageManager();
            if (null != packageManager) {
                ApplicationInfo applicationInfo = packageManager
                        .getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
                if (null != applicationInfo) {
                    if (applicationInfo.metaData != null) {
                        channelName = String.valueOf(applicationInfo.metaData.get("channel_id"));
                    }
                    return channelName;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            return "1";//默认官方渠道号 1
        }

        return channelName;
    }

    public String getPackageName() {
        return mContext.getPackageName();
    }


    public String getSystemModel() {
        return android.os.Build.MODEL;
    }

    public String getToken() {
        if (TextUtils.isEmpty(mToken)) {
            ICommonService servie = (ICommonService) ARouter.getInstance().build(ICommonService.COMMON_SERVICE_PATH).navigation();
            mToken = servie.getToken();
        }
        System.out.println("mToken:" + mToken);
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getOaid() {
        return oaid;
    }

    public void setOaid(String oaid) {
        this.oaid = oaid;
    }

    public String getAPNType() {
        String netType = "none";
        ConnectivityManager manager = (ConnectivityManager) BaseApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }

        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = "wifi";
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) BaseApplication.application.getSystemService(Context.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                netType = "4g";
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
                netType = "3g";
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                netType = "2g";
            } else {
                netType = "2g";
            }
        }
        return netType;
    }
}
