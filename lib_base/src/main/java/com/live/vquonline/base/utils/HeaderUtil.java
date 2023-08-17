package com.live.vquonline.base.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/1/5 17:12
 */
public class HeaderUtil {
    public static void setHeader(Request.Builder builder) {
        if (DeviceManager.getInstance().getOpenGreen() == 1) {
            builder.addHeader("package-type", "1");
        }

        int max = 900000, min = 100000;
        long randomNum = System.currentTimeMillis();
        int ran3 = (int) (randomNum % (max - min) + min);
        Random random1 = new Random();
        int number = random1.nextInt(100000);
        String random = number + "";
        String timestamp = System.currentTimeMillis() + DeviceManager.getInstance().getTimestamp() + "";

        builder.addHeader("timestamp", timestamp);
        builder.addHeader("random", random);
        RequestBody requestBody = builder.build().body();
        String parametersStr = "";
        if (requestBody != null && requestBody instanceof FormBody) {
            Map<String, String> paramsMap = new HashMap<>();
            for (int i = 0; i < ((FormBody) requestBody).size(); i++) {
                String key = ((FormBody) requestBody).encodedName(i);
                String value = ((FormBody) requestBody).encodedValue(i);
                try {
                    paramsMap.put(URLDecoder.decode(key, "UTF-8"), URLDecoder.decode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    paramsMap.put(key, value);
                }
            }
            parametersStr = sortMapByKey(paramsMap);
            Log.e("parametersStr", parametersStr);
        }
//        DeviceManager.getInstance().getDeviceImei()
        String plainText = parametersStr + "8oxhZnp5iPHtmsJB" + timestamp + random + DeviceManager.getInstance().getDeviceImei();
        String sign = MD5Utils.MD5(plainText);
        builder.addHeader("sign", sign);

    }

    public static String sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());

        sortMap.putAll(map);
        StringBuilder builder = new StringBuilder("");
        for (Map.Entry<String, String> entry : sortMap.entrySet()) {
            builder.append(entry.getKey()).append(entry.getValue());
        }
        if (builder.length() > 0) {
            return builder.toString();
        } else {
            return "";
        }
    }
}
