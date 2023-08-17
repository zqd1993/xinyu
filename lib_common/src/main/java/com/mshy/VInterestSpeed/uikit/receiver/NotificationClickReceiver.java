package com.mshy.VInterestSpeed.uikit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.mshy.VInterestSpeed.common.bean.video.VideoRequestBean;
import com.mshy.VInterestSpeed.common.utils.CallUtil;
import com.mshy.VInterestSpeed.common.utils.JumpUtils;
import com.mshy.VInterestSpeed.common.utils.UserManager;


/**
 * Created by zhangbin on 2018/12/26.
 */

public class NotificationClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int linkType = intent.getIntExtra("linkType", 0);
        String linkUrl = intent.getStringExtra("linkUrl");
        if (!TextUtils.isEmpty(linkUrl) && linkType != 0) {
            JumpUtils.jump(linkType, linkUrl, context);
        }else{
            VideoRequestBean bean= (VideoRequestBean) intent.getSerializableExtra("VideoRequestBean");
            CallUtil.Companion.call(bean,true);
            UserManager.INSTANCE.setVideoRequestBean(null);
        }
    }
}
