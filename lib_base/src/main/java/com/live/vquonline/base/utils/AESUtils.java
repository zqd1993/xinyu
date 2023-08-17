package com.live.vquonline.base.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zhangbin on 2019/1/23.
 */

public class AESUtils {

    private static final String CBC_PKCS5_PADDING = "AES/ECB/PKCS7Padding";//AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private static final String AES = "AES";//AES 加密

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return
     */
    public static String encrypt(String password, String content) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }
        try {
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), AES);
            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return new String(Base64.encode(result, Base64.DEFAULT)); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static String decrypt(String password, String content) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }
        try {
            byte[] enc = Base64.decode(content, Base64.DEFAULT);
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), AES);
            Cipher cipher = Cipher.getInstance(AES);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(enc);
            return new String(result); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return null;
    }
}
