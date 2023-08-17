package com.mshy.VInterestSpeed.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.util.Map;

/**
 * Created by abby on 2016/12/11.
 */

public class ShareManager {
    public static final int TYPE_QQ = 0;
    public static final int TYPE_WEIXIN = 1;
    public static final int TYPE_WEIBO = 2;
    public static final int TYPE_PENG = 3;
    public static final int TYPE_KONG = 4;
    public static final int TYPE_OTHER_PHONE = 5;
    public static final int TYPE_CURRENT_PHONE = 6;
    private static ShareManager mInstance;
    private iAuthStatus mStatus;
    private iShareStatus mShareStatus;
    private final UMAuthListener mAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            mStatus.success(map);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            mStatus.error();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            mStatus.cancel();
        }
    };

    private final UMShareListener mShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            if (null != mShareStatus) {
                mShareStatus.success();
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            throwable.printStackTrace();
            if (null != mShareStatus) {
                mShareStatus.error();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            if (null != mShareStatus) {
                mShareStatus.cancel();
            }
        }
    };

    public interface iAuthStatus {
        void success(Map<String, String> map);

        void error();

        void cancel();
    }

    public interface iShareStatus {
        void success();

        void error();

        void cancel();
    }

    public static ShareManager getInstance() {
        if (null == mInstance) {
            mInstance = new ShareManager();
        }

        return mInstance;
    }

    public ShareManager setShareStatus(iShareStatus status) {
        mShareStatus = status;
        return this;
    }

    public void configWeixin(String id, String sec) {
        PlatformConfig.setWeixin(id, sec);
    }

    public void configWeibo(String id, String sec, String redirectUrl) {
        PlatformConfig.setSinaWeibo(id, sec, redirectUrl);
    }

    public void configQQ(String id, String sec) {
        PlatformConfig.setQQZone(id, sec);
    }

    public void verify(iAuthStatus status, Activity activity, int type) {
        mStatus = status;

        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(activity).setShareConfig(config);

        if (TYPE_QQ == type) {
            UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.QQ, mAuthListener);
        } else if (TYPE_WEIXIN == type) {
            UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, mAuthListener);
        } else if (TYPE_WEIBO == type) {
            UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.SINA, mAuthListener);
        }
    }

    public void delete(Activity activity, UMAuthListener umAuthListener) {
        UMShareAPI mShareApi = UMShareAPI.get(activity);
        mShareApi.deleteOauth(activity, SHARE_MEDIA.QQ, umAuthListener);
        mShareApi.deleteOauth(activity, SHARE_MEDIA.WEIXIN, umAuthListener);
        mShareApi.deleteOauth(activity, SHARE_MEDIA.SINA, umAuthListener);
    }

    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {

        UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
    }

    public boolean isInstalled(Activity activity, int type) {
        UMShareAPI mShareApi = UMShareAPI.get(activity);
        if (type == TYPE_WEIXIN || type == TYPE_PENG) {
            return mShareApi.isInstall(activity, SHARE_MEDIA.WEIXIN);
        } else if (type == TYPE_WEIBO) {
            return mShareApi.isInstall(activity, SHARE_MEDIA.SINA);
        } else {
            return mShareApi.isInstall(activity, SHARE_MEDIA.QQ);
        }
    }

    public void shareToPlatform(int type, String url, String imageUrl, String title, String description, Activity context) {
        UMWeb web = new UMWeb(url);
        web.setThumb(new UMImage(context, imageUrl));
        web.setTitle(title);
        web.setDescription(description);

        SHARE_MEDIA media = null;
        switch (type) {
            case 1:
                media = SHARE_MEDIA.WEIXIN;
                break;
            case 2:
                media = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
            case 5:
                media = SHARE_MEDIA.SINA;
                break;
            case 3:
                media = SHARE_MEDIA.QQ;
                break;
            case 4:
                media = SHARE_MEDIA.QZONE;
                break;
        }

        if (null != media) {
            new ShareAction(context).setPlatform(media).setCallback(mShareListener).withMedia(web).share();
        }
    }

    public void sharePicToPlatform(SHARE_MEDIA media, String picPath, Activity context) {
        File file = new File(picPath);
        UMImage thumb = new UMImage(context, file);
        thumb.setThumb(thumb);
        if (null != media) {
            new ShareAction(context).setPlatform(media).setCallback(mShareListener).withMedia(thumb).share();
        }
    }

    public void sharePicToPlatform(SHARE_MEDIA media, Bitmap bitmap, Activity context) {
//        UMWeb web = new UMWeb("https://www.baidu.com");
//        web.setThumb(new UMImage(context, bitmap));
//        web.setTitle("aaa");
//        web.setDescription("aaaa");

        UMImage thumb = new UMImage(context, bitmap);
        thumb.setThumb(thumb);
        if (null != media) {
            new ShareAction(context).setPlatform(media).setCallback(mShareListener).withMedia(thumb).share();
        }
    }

    public void shareToPlatformCallback(int type, String url, String imageUrl, String title, String description,
                                        Activity context, UMShareListener listener) {
        UMWeb web = new UMWeb(url);
        web.setThumb(new UMImage(context, imageUrl));
        web.setTitle(title);
        web.setDescription(description);

        SHARE_MEDIA media = null;
        switch (type) {
            case 1:
                media = SHARE_MEDIA.WEIXIN;
                break;
            case 2:
                media = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
            case 5:
                media = SHARE_MEDIA.SINA;
                break;
            case 3:
                media = SHARE_MEDIA.QQ;
                break;
            case 4:
                media = SHARE_MEDIA.QZONE;
                break;
        }

        if (null != media) {
            new ShareAction(context).setPlatform(media).setCallback(listener).withMedia(web).share();
        }
    }

    private ShareManager() {


    }
}
