package com.mshy.VInterestSpeed.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.util.Log;

import com.mshy.VInterestSpeed.common.bean.BaseResponse;
import com.mshy.VInterestSpeed.common.net.GlobalApiService;
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage;
import com.sdk.engine.RiskCallBack;
import com.sdk.engine.RiskControlEngine;
import com.sdk.engine.RiskErrorCode;
import com.sdk.engine.RiskInfo;
import com.sdk.engine.RiskRequestParams;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author: Tany
 * date: 2023/2/4
 * description:
 */
public class RiskControlUtil {
    public static void getToken(Context context,int type) {
        int max=9;
        int min=0;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        if(s<8){
            return;
        }
        String userAccount="login";
        if(UserManager.INSTANCE.getUserInfo()!=null){
            userAccount=UserManager.INSTANCE.getUserInfo().getUserId();
        }
        //构建请求参数
        RiskRequestParams requestParams = new RiskRequestParams.Builder()
                //设置设备ip地址
                .setIp(getIP(context))
                //设置用户账户
                .setUserAccount(userAccount)
                //设置场景，1买量，2提现，3注册，4登录，5充值
//                .setScene(RiskRequestParams.SCENE_LOGIN)
                .setScene(type)
                //设置接口的超时时间
                .setTimeout(15000)
                .build();
        //异步请求获取token
        int ret = RiskControlEngine.getToken(requestParams, new RiskCallBack() {
            @Override
            public void onFinish(RiskInfo riskInfo) {
                int code = riskInfo.getResultCode();
                String token = riskInfo.getToken();
                Log.v("TAG", "code:" + code);
                Log.v("TAG", "token:" + token);
                if (code == RiskErrorCode.SUCCESS) {
                    Log.v("TAG", "getToken success");
                    GlobalApiService globalApiService = GlobalServiceManage.INSTANCE.getRetrofit().create(GlobalApiService.class);
                    globalApiService.vquRiskControl(token).enqueue(new Callback<BaseResponse<Object>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<Object>> call, Response<BaseResponse<Object>> response) {
                            Log.i("RiskControl","上传成功");
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {

                        }
                    });
                }
            }
        });
        Log.v("TAG", "getToken ret::" + ret);
    }
    public static String getIP(Context context) {
        String ip = "0.0.0.0";
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        int type = info.getType();
        if (type == ConnectivityManager.TYPE_ETHERNET) {
            ip = getEtherNetIP();
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            ip = getWifiIP(context);
        }
        return ip;
    }
    public static String getEtherNetIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "0.0.0.0";
    }
    public static String getWifiIP(Context context) {
        android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
                .getSystemService(android.content.Context.WIFI_SERVICE);
        WifiInfo wifiinfo = wifi.getConnectionInfo();
        int intaddr = wifiinfo.getIpAddress();
        byte[] byteaddr = new byte[] { (byte) (intaddr & 0xff),
                (byte) (intaddr >> 8 & 0xff), (byte) (intaddr >> 16 & 0xff),
                (byte) (intaddr >> 24 & 0xff) };
        InetAddress addr = null;
        try {
            addr = InetAddress.getByAddress(byteaddr);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String mobileIp = addr.getHostAddress();
        return mobileIp;
    }
}
