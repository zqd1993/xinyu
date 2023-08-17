package com.mshy.VInterestSpeed.uikit.business.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.live.vquonline.base.BaseApplication;
import com.mshy.VInterestSpeed.common.constant.RouteUrl;
import com.mshy.VInterestSpeed.common.ui.view.notification.SwipeMessageNotificationManager;
import com.mshy.VInterestSpeed.common.utils.UmUtils;
import com.mshy.VInterestSpeed.common.utils.UserManager;
import com.mshy.VInterestSpeed.uikit.business.uinfo.UserInfoHelper;
import com.mshy.VInterestSpeed.uikit.common.activity.ToolBarOptions;
import com.mshy.VInterestSpeed.uikit.common.activity.UI;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.R;
import com.mshy.VInterestSpeed.uikit.api.NimUIKit;
import com.mshy.VInterestSpeed.uikit.api.model.contact.ContactChangedObserver;
import com.mshy.VInterestSpeed.uikit.api.model.main.OnlineStateChangeObserver;
import com.mshy.VInterestSpeed.uikit.api.model.session.SessionCustomization;
import com.mshy.VInterestSpeed.uikit.api.model.user.UserInfoObserver;
import com.mshy.VInterestSpeed.uikit.api.wrapper.NimToolBarOptions;
import com.mshy.VInterestSpeed.uikit.business.session.constant.Extras;
import com.mshy.VInterestSpeed.uikit.business.session.fragment.MessageFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;
import java.util.Set;


/**
 * 点对点聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class P2PMessageActivity extends BaseMessageActivity {

    private boolean isResume = false;

    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);

        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 单聊特例话数据，包括个人信息，
        requestBuddyInfo();
        displayOnlineState();
        registerObservers(true);
        UmUtils.setUmEvent(this, UmUtils.ENTERCHATPAGE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
        String contactId = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
        UserManager.INSTANCE.setChattingId(contactId);
        if (Settings.canDrawOverlays(BaseApplication.context)) {
            SwipeMessageNotificationManager.getInstance(this).removeNotification(contactId);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        UserManager.INSTANCE.setChattingId("");
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;

    }

    private void requestBuddyInfo() {
        setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
    }

    private void displayOnlineState() {
        //TODO NIM 在线状态 这里先不要了
//        if (!NimUIKitImpl.enableOnlineState()) {
//            return;
//        }
//        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(sessionId);
//        setSubTitle(detailContent);
    }


    /**
     * 命令消息接收观察者
     */
    private Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };


    /**
     * 用户信息变更观察者
     */
    private UserInfoObserver userInfoObserver = new UserInfoObserver() {
        @Override
        public void onUserInfoChanged(List<String> accounts) {
            if (!accounts.contains(sessionId)) {
                return;
            }
            requestBuddyInfo();
        }
    };

    /**
     * 好友资料变更（eg:关系）
     */
    private ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };

    /**
     * 好友在线状态观察者
     */
    private OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            if (!accounts.contains(sessionId)) {
                return;
            }
            // 按照交互来展示
            displayOnlineState();
        }
    };

    private void registerObservers(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
        if (NimUIKit.enableOnlineState()) {
            NimUIKit.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
        }
    }


    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }
        String content = message.getContent();
        try {
            JSONObject json = JSON.parseObject(content);
            int id = json.getIntValue("id");
            //TODO NIM 取消输入状态
//            if (id == 1) {
//                // 正在输入
//                ToastHelper.showToastLong(P2PMessageActivity.this, "对方正在输入...");
//            } else {
//                ToastHelper.showToast(P2PMessageActivity.this, "command: " + content);
//            }
        } catch (Exception ignored) {

        }
    }

    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        MessageFragment fragment = (MessageFragment) ARouter.getInstance().build(RouteUrl.Message.MessageVquMsgFragment).navigation();
//        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_message_activity;
    }

    @Override
    protected void initToolBar() {
        ToolBarOptions options = new NimToolBarOptions();
        setToolBar(R.id.toolbar, options);
    }

    @Override
    protected boolean enableSensor() {
        return true;
    }

    private AppCompatTextView mTitleView;


    private int leftWidth;
    private int rightWidth;

    @Override
    protected void addRightCustomViewOnActionBar(UI activity, List<SessionCustomization.OptionsButton> buttons) {
        super.addRightCustomViewOnActionBar(activity, buttons);
        // 设置标题居中
        int childCount = getToolBar().getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getToolBar().getChildAt(i);
            // 拿到导航按钮（也叫返回按钮）
            if (view instanceof ImageButton) {
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        leftWidth = v.getWidth();
                        leftComplete = true;
                        viewComplete();
                    }
                });
            }
            // 右边宽度
            if (view instanceof LinearLayout) {
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        rightWidth = v.getWidth();
                        rightComplete = true;
                        viewComplete();
                    }
                });
            }
            //  找到标题的View
            if (view instanceof AppCompatTextView) {
                mTitleView = (AppCompatTextView) view;
                // 设置居中
                mTitleView.setGravity(Gravity.CENTER);
                mTitleView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
        }

    }

    private boolean leftComplete;
    private boolean rightComplete;

    /**
     * view计算完成
     */
    private void viewComplete() {
        if (leftComplete && rightComplete) {
            //两边宽度的差值
            int value = leftWidth - rightWidth;
            if (mTitleView != null) {
                if (value > 0) {
                    //左边宽度大于右边
                    mTitleView.setPadding(0, 0, value, 0);
                } else {
                    mTitleView.setPadding(-value, 0, 0, 0);
                }
            }
        }
    }

}
