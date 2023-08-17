package com.mshy.VInterestSpeed.uikit.business.session.fragment;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.live.vquonline.base.utils.SpUtils;
import com.live.vquonline.base.utils.UtilsKt;
import com.mshy.VInterestSpeed.common.CommonApplication;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.bean.gift.DialogGiftBean;
import com.mshy.VInterestSpeed.common.constant.SpKey;
import com.mshy.VInterestSpeed.common.livedata.AppViewModel;
import com.mshy.VInterestSpeed.common.utils.UserSpUtils;
import com.mshy.VInterestSpeed.uikit.api.NimUIKit;
import com.mshy.VInterestSpeed.common.bean.UserInCallEvent;
import com.mshy.VInterestSpeed.uikit.business.ait.AitManager;
import com.mshy.VInterestSpeed.uikit.business.session.actions.BaseAction;
import com.mshy.VInterestSpeed.uikit.business.session.actions.ImageAction;
import com.mshy.VInterestSpeed.uikit.business.session.actions.VideoAction;
import com.mshy.VInterestSpeed.uikit.business.session.module.Container;
import com.mshy.VInterestSpeed.uikit.business.session.module.ModuleProxy;
import com.mshy.VInterestSpeed.uikit.business.session.module.input.InputPanel;
import com.mshy.VInterestSpeed.uikit.business.session.module.list.MessageListPanelEx;
import com.mshy.VInterestSpeed.uikit.common.fragment.TFragment;
import com.mshy.VInterestSpeed.uikit.api.UIKitOptions;
import com.mshy.VInterestSpeed.uikit.api.model.main.CustomPushContentProvider;
import com.mshy.VInterestSpeed.uikit.api.model.session.SessionCustomization;
import com.mshy.VInterestSpeed.uikit.business.session.constant.Extras;
import com.mshy.VInterestSpeed.uikit.common.CommonUtil;
import com.mshy.VInterestSpeed.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MemberPushOption;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.robot.model.NimRobotInfo;
import com.netease.nimlib.sdk.robot.model.RobotAttachment;
import com.netease.nimlib.sdk.robot.model.RobotMsgType;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天界面基类
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class MessageFragment extends TFragment implements ModuleProxy {
    /**
     * 2是系统消息
     */
    protected View rootView;

    protected SessionCustomization customization;

    protected static final String TAG = "MessageActivity";

    // p2p对方Account或者群id
    protected String sessionId;

    protected SessionTypeEnum sessionType;

    // modules
    protected InputPanel inputPanel;
    protected MessageListPanelEx messageListPanel;

    protected AitManager aitManager;

    protected boolean isShowEditBar;
    protected boolean isShowGiftAndVideo;
    protected ImageAction mImageAction;
    protected VideoAction mVideoAction;
    protected Container container;
    protected RecyclerView messageListView;
    protected TextView tvTip;
    protected ImageView ivNext;
    AppViewModel appViewModel = CommonApplication.mCommonApplication.getAppViewModelProvider().get(AppViewModel.class);

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parseIntent();
        initView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.common_tanta_nim_message_fragment, container, false);
        return rootView;
    }

    private void initView() {
        tvTip = rootView.findViewById(R.id.tv_tip);
        ivNext = rootView.findViewById(R.id.iv_next);
        messageListView = rootView.findViewById(R.id.messageListView);
        messageListView.setItemAnimator(null);
        messageListView.setOnTouchListener((view, motionEvent) -> {
            if (inputPanel != null) {
                inputPanel.collapse(true, true);
            }
            return false;
        });
        if (appViewModel.getUnreadConversationList().getValue() != null) {
            if (appViewModel.getUnreadConversationList().getValue().size() > 0) {
                ivNext.setVisibility(View.VISIBLE);
                ivNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (appViewModel.getUnreadConversationList().getValue() != null && appViewModel.getUnreadConversationList().getValue().size() > 0) {
                            NimUIKit.startP2PSession(
                                    getActivity(),
                                    appViewModel.getUnreadConversationList().getValue().get(0).getContactId()
                            );
                            getActivity().finish();
                        }
                    }
                });
            } else {
                ivNext.setVisibility(View.GONE);
            }
        } else {
            ivNext.setVisibility(View.GONE);
        }
        appViewModel.getUnreadConversationList().observe(getActivity(), new androidx.lifecycle.Observer<List<RecentContact>>() {
            @Override
            public void onChanged(List<RecentContact> recentContacts) {
                if (recentContacts.size() > 0) {
                    ivNext.setVisibility(View.VISIBLE);
                    ivNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NimUIKit.startP2PSession(
                                    getActivity(),
                                    recentContacts.get(0).getContactId()
                            );
                            getActivity().finish();
                        }
                    });
                } else {
                    ivNext.setVisibility(View.GONE);
                }

            }
        });
        if ("2".equals(sessionId)) {//系统消息
            clearMessageAMonthAgo();
        }
    }

    private void clearMessageAMonthAgo() {
        Log.i("tyy", "清除消息");
        long startTime = 0;
//        long endTime=System.currentTimeMillis()-60*60*1000*24*30;//30天前的删掉
        long endTime = System.currentTimeMillis() - 60 * 1000*2;//2分钟前的删掉
        NIMClient.getService(MsgService.class).deleteRangeHistory(sessionId, sessionType, startTime, endTime);
        searchMessage(sessionId);
    }

    /**
     * ***************************** life cycle *******************************
     */

    @Override
    public void onPause() {
        super.onPause();

        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        inputPanel.onPause();
        messageListPanel.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        messageListPanel.onResume();
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC); // 默认使用听筒播放
    }

    @Subscribe
    public void userInCallEvent(UserInCallEvent event) {
        UserInCallEvent userInCall = SpUtils.INSTANCE.getBean(SpKey.user_in_call, UserInCallEvent.class);
        if (userInCall != null) {
            inputPanel.setUserInCall(userInCall);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        messageListPanel.onDestroy();
        registerObservers(false);
        if (inputPanel != null) {
            inputPanel.onDestroy();
        }
        if (aitManager != null) {
            aitManager.reset();
        }
    }

    public boolean onBackPressed() {
        return inputPanel.collapse(true) || messageListPanel.onBackPressed();
    }

    public void refreshMessageList() {
        messageListPanel.refreshMessageList();
    }

    private void parseIntent() {
        Bundle arguments = getArguments();
        sessionId = arguments.getString(Extras.EXTRA_ACCOUNT);
        sessionType = (SessionTypeEnum) arguments.getSerializable(Extras.EXTRA_TYPE);
        IMMessage anchor = (IMMessage) arguments.getSerializable(Extras.EXTRA_ANCHOR);

        customization = (SessionCustomization) arguments.getSerializable(Extras.EXTRA_CUSTOMIZATION);
        container = new Container(getActivity(), sessionId, sessionType, this, true);

        if (messageListPanel == null) {
            messageListPanel = new MessageListPanelEx(container, rootView, anchor, false, false);
        } else {
            messageListPanel.reload(container, anchor);
        }

        if ("2".equals(sessionId) || "11".equals(sessionId)) {  //系统通知,不显示下面的输入框
            isShowEditBar = true;
        }

        if ("100091".equals(sessionId) || isService()) {  //如果是客服，不显示礼物和视频按钮
            isShowGiftAndVideo = true;
        }

        if (inputPanel == null) {
            inputPanel = new InputPanel(container, rootView, getActionList(), isShowEditBar, isShowGiftAndVideo, getContext());
            inputPanel.setCustomization(customization);

            //点击按钮
            inputPanel.setOnClickImage(new InputPanel.OnClickImage() {
                @Override
                public void onClickImage() { //图片
//                    if (null != mImageAction) {
//                        mImageAction.onClick();
//                    }
                    selectImage();
                }

                @Override
                public void onClickGift() {  //礼物
                    getGiftData();
                }

                @Override
                public void onClickVideo() {  //视频
                    if (!isFastClick()) {
                        if (!TextUtils.isEmpty(sessionId)) {
                            getCallInfo();
                        }
                    } else {
                        UtilsKt.toast("短时间内重复操作次数过多", Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onEmptyText() {
                    UtilsKt.toast("消息不能为空", Toast.LENGTH_SHORT);
                }

            });
        } else {
            inputPanel.reload(container, customization);
        }


        initAitManager();

        inputPanel.switchRobotMode(NimUIKitImpl.getRobotInfoProvider().getRobotByAccount(sessionId) != null);

        UserInCallEvent userInCall = SpUtils.INSTANCE.getBean(SpKey.user_in_call, UserInCallEvent.class);

        if (userInCall != null) {
            inputPanel.setUserInCall(userInCall);
        }
        registerObservers(true);

        if (customization != null) {
            messageListPanel.setChattingBackground(customization.backgroundUri, customization.backgroundColor);
        }

    }

    private void initAitManager() {

        UIKitOptions options = NimUIKitImpl.getOptions();
        if (options.aitEnable) {
            aitManager = new AitManager(getContext(), options.aitTeamMember && sessionType == SessionTypeEnum.Team ? sessionId : null, options.aitIMRobot);
            inputPanel.addAitTextWatcher(aitManager);
            aitManager.setTextChangeListener(inputPanel);
        }
    }

    /**
     * ************************* 消息收发 **********************************
     */
    // 是否允许发送消息
    protected boolean isAllowSendMessage(final IMMessage message) {
        if (null == customization) {
            return true;
        } else {
            return customization.isAllowSendMessage(message);
        }
    }


    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(incomingMessageObserver, register);
        // 已读回执监听
        if (NimUIKitImpl.getOptions().shouldHandleReceipt) {
            service.observeMessageReceipt(messageReceiptObserver, register);
        }
    }

    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            onMessageIncoming(messages);
        }
    };

    private void onMessageIncoming(List<IMMessage> messages) {
        if (CommonUtil.isEmpty(messages)) {
            return;
        }
        onMessageIncomingSvg(messages);
        messageListPanel.onIncomingMessage(messages);
        // 发送已读回执
        messageListPanel.sendReceipt();
    }

    /**
     * 已读回执观察者
     */
    private Observer<List<MessageReceipt>> messageReceiptObserver = new Observer<List<MessageReceipt>>() {
        @Override
        public void onEvent(List<MessageReceipt> messageReceipts) {
            messageListPanel.receiveReceipt();
        }
    };


    /**
     * ********************** implements ModuleProxy *********************
     */
    @Override
    public boolean sendMessage(IMMessage message) {
        message.setEnv("tchat");
        sendMsg(message);
        return true;
    }

    protected void sendImMessage(IMMessage message) {
        if (isAllowSendMessage(message)) {
            appendTeamMemberPush(message);
            message = changeToRobotMsg(message);
            final IMMessage msg = message;
            appendPushConfig(message);
            // send message to server and save to db
            NIMClient.getService(MsgService.class).sendMessage(message, false).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    onSendTipMsg();
                }

                @Override
                public void onFailed(int code) {
                    sendFailWithBlackList(code, msg);
                }

                @Override
                public void onException(Throwable exception) {

                }
            });

        } else {
            // 替换成tip
            message = MessageBuilder.createTipMessage(message.getSessionId(), message.getSessionType());
            message.setContent("该消息无法发送");
            message.setStatus(MsgStatusEnum.success);
            NIMClient.getService(MsgService.class).saveMessageToLocal(message, false);
        }

        messageListPanel.onMsgSend(message);
        if (aitManager != null) {
            aitManager.reset();
        }
    }

    // 被对方拉入黑名单后，发消息失败的交互处理
    private void sendFailWithBlackList(int code, IMMessage msg) {
        if (code == ResponseCode.RES_IN_BLACK_LIST) {
            // 如果被对方拉入黑名单，发送的消息前不显示重发红点
            msg.setStatus(MsgStatusEnum.success);
            NIMClient.getService(MsgService.class).updateIMMessageStatus(msg);
            messageListPanel.refreshMessageList();
            // 同时，本地插入被对方拒收的tip消息
            IMMessage tip = MessageBuilder.createTipMessage(msg.getSessionId(), msg.getSessionType());
            tip.setContent(getActivity().getString(R.string.black_list_send_tip));
            tip.setStatus(MsgStatusEnum.success);
            CustomMessageConfig config = new CustomMessageConfig();
            config.enableUnreadCount = false;
            tip.setConfig(config);
            NIMClient.getService(MsgService.class).saveMessageToLocal(tip, true);
        }
    }

    private void appendTeamMemberPush(IMMessage message) {
        if (aitManager == null) {
            return;
        }
        if (sessionType == SessionTypeEnum.Team) {
            List<String> pushList = aitManager.getAitTeamMember();
            if (pushList == null || pushList.isEmpty()) {
                return;
            }
            MemberPushOption memberPushOption = new MemberPushOption();
            memberPushOption.setForcePush(true);
            memberPushOption.setForcePushContent(message.getContent());
            memberPushOption.setForcePushList(pushList);
            message.setMemberPushOption(memberPushOption);
        }
    }

    private IMMessage changeToRobotMsg(IMMessage message) {
        if (aitManager == null) {
            return message;
        }
        if (message.getMsgType() == MsgTypeEnum.robot) {
            return message;
        }
        if (isChatWithRobot()) {
            if (message.getMsgType() == MsgTypeEnum.text && message.getContent() != null) {
                String content = message.getContent().equals("") ? " " : message.getContent();
                message = MessageBuilder.createRobotMessage(message.getSessionId(), message.getSessionType(), message.getSessionId(), content, RobotMsgType.TEXT, content, null, null);
            }
        } else {
            String robotAccount = aitManager.getAitRobot();
            if (TextUtils.isEmpty(robotAccount)) {
                return message;
            }
            String text = message.getContent();
            String content = aitManager.removeRobotAitString(text, robotAccount);
            content = content.equals("") ? " " : content;
            message = MessageBuilder.createRobotMessage(message.getSessionId(), message.getSessionType(), robotAccount, text, RobotMsgType.TEXT, content, null, null);

        }
        return message;
    }

    private boolean isChatWithRobot() {
        return NimUIKitImpl.getRobotInfoProvider().getRobotByAccount(sessionId) != null;
    }

    private void appendPushConfig(IMMessage message) {
        CustomPushContentProvider customConfig = NimUIKitImpl.getCustomPushContentProvider();
        if (customConfig == null) {
            return;
        }
        String content = customConfig.getPushContent(message);
        Map<String, Object> payload = customConfig.getPushPayload(message);
        Map<String, Object> vivoField = new HashMap<>();
        vivoField.put("classification", 1);
        payload.put("vivoField", vivoField);//vivo运营消息
        if (!TextUtils.isEmpty(content)) {
            message.setPushContent(content);
        }
        if (payload != null) {
            message.setPushPayload(payload);
        }

    }

    @Override
    public void onInputPanelExpand() {
        messageListPanel.scrollToBottom();
    }

    @Override
    public void shouldCollapseInputPanel() {
        inputPanel.collapse(false);
    }

    @Override
    public boolean isLongClickEnabled() {
        return !inputPanel.isRecording();
    }

    @Override
    public void onItemFooterClick(IMMessage message) {
        if (aitManager == null) {
            return;
        }
        if (messageListPanel.isSessionMode()) {
            RobotAttachment attachment = (RobotAttachment) message.getAttachment();
            NimRobotInfo robot = NimUIKitImpl.getRobotInfoProvider().getRobotByAccount(attachment.getFromRobotAccount());
            aitManager.insertAitRobot(robot.getAccount(), robot.getName(), inputPanel.getEditSelectionStart());
        }
    }

    @Override
    public void onSendTipMsg() {
        if (UserSpUtils.INSTANCE.getUserBean()
                != null) {
            if (UserSpUtils.INSTANCE.getUserBean().getGender() != 1) {
                //不是女生
                searchMessage(UserSpUtils.INSTANCE.getUserBean().getUserId());
            }
        }
    }

    @Override
    public void getChatInfo() {

    }

    @Override
    public void callAudio(String account) {

    }

    @Override
    public void callVideo(String account) {

    }


    private final List<IMMessage> myMessages = new ArrayList<>();

    /**
     * 搜索指定的消息记录
     * 查询是否要发送扣费提示消息
     */
    private void searchMessage(final String account) {
        myMessages.clear();
        NIMClient.getService(MsgService.class)
                .queryMessageList(container.account, container.sessionType, 0, 20)
                .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
                    @Override
                    public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                        if (code != ResponseCode.RES_SUCCESS || exception != null) {
                            return;
                        }
                        if (messages != null) {
                            for (IMMessage message : messages) {
                                if (message.getFromAccount().equals(account)) {
                                    myMessages.add(message);
                                }
                            }
                            if (myMessages.size() == 1) {
                                sendTipMsg();
                            }
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (aitManager != null) {
            aitManager.onActivityResult(requestCode, resultCode, data);
        }
        inputPanel.onActivityResult(requestCode, resultCode, data);
        messageListPanel.onActivityResult(requestCode, resultCode, data);
    }

    // 操作面板集合
    protected List<BaseAction> getActionList() {
        List<BaseAction> actions = new ArrayList<>();
        mImageAction = new ImageAction();
        mVideoAction = new VideoAction();

        actions.add(mImageAction);
        actions.add(mVideoAction);
        //删除位置菜单栏
//        actions.add(new LocationAction());

        if (customization != null && customization.actions != null) {
            actions.addAll(customization.actions);
        }
        return actions;
    }

    /*----------------------------------新增方法及属性------------------------------------------------*/

    /**
     * 是否需要显示  sessionId 客服之类的不显示常用语和亲密度
     */
    protected boolean isShow() {
        if (!TextUtils.isEmpty(sessionId)) {
            if (!"2".equals(sessionId) && !"4".equals(sessionId) && !"11".equals(sessionId) && !"3".equals(sessionId) && !"5".equals(sessionId)
                    && !"6".equals(sessionId) && !"7".equals(sessionId) && !"8".equals(sessionId) && !"9".equals(sessionId) && !"10".equals(sessionId)
                    && !isService()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 是否是客服
     */
    protected boolean isService() {
        String service = SpUtils.INSTANCE.getString(SpKey.SERVICE_ID, "");
        if (!TextUtils.isEmpty(service)) {
            for (String ser : service.split(",")) {
                if (ser.equals(sessionId)) {
                    return true;
                }
            }
        }
        return false;
    }


    private final int MIN_DELAY_TIME = 1000; // 两次点击间隔不能少于1000ms
    private long lastClickTime;

    protected boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == container.sessionType
                && message.getSessionId() != null
                && message.getSessionId().equals(container.account);
    }

    protected boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    protected void sendMsg(IMMessage message) {
        if (null != message) {
            if (message.getMsgType().getValue() == 0) {  //文本
                imCost(1, message.getSessionId(), message.getContent(), message);
            } else if (message.getMsgType().getValue() == 1) {  //图片
                imCost(3, message.getSessionId(), message.getContent(), message);
            } else if (message.getMsgType().getValue() == 3) {  //视频
                imCost(4, message.getSessionId(), message.getContent(), message);
            } else if (message.getMsgType().getValue() == 2) {  //语音
                Log.e(TAG, "sendMsg: " + message.getSessionId() + "-----" + message.getContent());
                imCost(2, message.getSessionId(), message.getContent(), message);
            } else if (message.getMsgType().getValue() == 63 || message.getMsgType().getValue() == 64) {  //防骗提示
                imCost(message.getMsgType().getValue(), message.getSessionId(), message.getContent(), message);
            }
        }
    }

    /*----------------------------------需要子类重写的方法------------------------------------------*/

    /**
     * 获取礼物
     */
    protected void getGiftData() {

    }

    /**
     * 拨打视频
     */
    protected void getCallInfo() {

    }

    /**
     * 赠送礼物
     *
     * @param toUid
     */
    protected void sendGift(int toUid, final DialogGiftBean bean, int number) {

    }

    /**
     * 添加svg动画
     */
    protected void addSvg(String svgUrl) {

    }

    /**
     * 新消息的message是否有svg动画
     *
     * @param messages
     */
    protected void onMessageIncomingSvg(List<IMMessage> messages) {

    }

    /**
     * 发送提示消息
     */
    protected void sendTipMsg() {

    }

    /**
     * im扣费
     *
     * @param type
     * @param id
     * @param content
     * @param message
     */
    protected void imCost(final int type, String id, final String content, final IMMessage message) {

    }

    /**
     * 选择图片
     */
    protected void selectImage() {

    }

}
