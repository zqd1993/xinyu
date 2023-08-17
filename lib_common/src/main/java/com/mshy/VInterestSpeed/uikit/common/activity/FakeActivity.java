package com.mshy.VInterestSpeed.uikit.common.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mshy.VInterestSpeed.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;

/**
 * Created by zhangbin on 2018/12/7.
 */

public class FakeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<IMMessage> messages = (ArrayList<IMMessage>) getIntent().getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
//        messages.get(0).
//        P2PMessageActivity.start(this, messages.get(0).getFromAccount(), null, null);
        if (null != messages && messages.size() > 0) {
            NimUIKit.startP2PSession(this, messages.get(0).getFromAccount());
        }
        finish();
    }
}
