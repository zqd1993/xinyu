package com.live.vquonline.base.utils;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/1/5 17:12
 */
public class HeaderUtil {
    public static void setHeader(Request.Builder builder, String url) {
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

        Log.i("header randomNum", randomNum + "");
        String startNum = String.valueOf(timestamp.charAt(6));
        String endNum = String.valueOf(timestamp.charAt(10));
        String randomStr = startNum + endNum + (int) ((Math.random() * 9000) + 1000);
        Log.i("header randomStr", randomStr);

        builder.addHeader("timestamp", timestamp);
        builder.addHeader("random", randomStr);
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
        //心动、搭讪或者发送im消息，验签
        if (url.contains("sendIm") || url.contains("beckon/send")) {
            Buffer buffer = new Buffer();
            try {
                requestBody.writeTo(buffer);
                MediaType contentType = requestBody.contentType();
                Charset charset = contentType.charset(StandardCharsets.UTF_8);
                String data = buffer.readString(charset);
                Log.i("header parametersStr", data);
                if (url.contains("sendIm") || (data.startsWith("{") && data.endsWith("}"))) {
                    Gson gson = new Gson();
                    Map<String, String> map = gson.fromJson(data, new TypeToken<Map<String, String>>() {
                    }.getType());
                    data = sortMapByKey(map);
                } else {
                    String[] parameters = data.split("&");
                    Map<String, String> map = new HashMap<>();
                    for (String param : parameters) {
                        String[] mapStr = param.split("=");
                        map.put(URLDecoder.decode(mapStr[0], "UTF-8"), URLDecoder.decode(mapStr[1], "UTF-8"));
                    }
                    data = sortMapByKey(map);
                }
                Log.i("header mapStr", data);
                String checkSum = MD5Utils.MD5("ahw4vxalrnkmjai1rcoifaphj3aizlwk" + randomStr + data + timestamp);
                builder.addHeader("checksum", checkSum);
            } catch (IOException e) {

            }
        }
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
