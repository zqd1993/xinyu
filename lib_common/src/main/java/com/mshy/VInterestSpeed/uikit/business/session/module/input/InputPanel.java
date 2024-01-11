package com.mshy.VInterestSpeed.uikit.business.session.module.input;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.alibaba.fastjson.JSONObject;
import com.live.vquonline.base.utils.ToastUtils;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.bean.UserInCallEvent;
import com.mshy.VInterestSpeed.common.constant.RouteUrl;
import com.mshy.VInterestSpeed.common.constant.SpKey;
import com.mshy.VInterestSpeed.common.helper.ARouterHelperKt;
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog;
import com.mshy.VInterestSpeed.common.utils.PermissionUtils;
import com.mshy.VInterestSpeed.common.utils.UserManager;
import com.mshy.VInterestSpeed.uikit.api.NimUIKit;
import com.mshy.VInterestSpeed.uikit.api.UIKitOptions;
import com.mshy.VInterestSpeed.uikit.api.model.session.SessionCustomization;
import com.mshy.VInterestSpeed.uikit.business.ait.AitTextChangeListener;
import com.mshy.VInterestSpeed.uikit.business.session.actions.BaseAction;
import com.mshy.VInterestSpeed.uikit.business.session.emoji.EmoticonPickerView;
import com.mshy.VInterestSpeed.uikit.business.session.emoji.IEmoticonSelectedListener;
import com.mshy.VInterestSpeed.uikit.business.session.emoji.MoonUtil;
import com.mshy.VInterestSpeed.uikit.business.session.module.Container;
import com.mshy.VInterestSpeed.uikit.common.ToastHelper;
import com.mshy.VInterestSpeed.uikit.common.media.imagepicker.Constants;
import com.mshy.VInterestSpeed.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil;
import com.mshy.VInterestSpeed.uikit.common.util.string.StringUtil;
import com.mshy.VInterestSpeed.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.CustomNotificationConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.permissionx.guolindev.PermissionX;

import java.io.File;
import java.security.Permission;
import java.util.List;

/**
 * 底部文本编辑，语音等模块
 * Created by hzxuwen on 2015/6/16.
 */
public class InputPanel implements IEmoticonSelectedListener, IAudioRecordCallback, AitTextChangeListener {

    private static final String TAG = "MsgSendLayout";

    private static final int SHOW_LAYOUT_DELAY = 200;

    protected Container container;
    protected View view;
    protected Handler uiHandler;

    protected View actionPanelBottomLayout; // 更多布局
    protected LinearLayout messageActivityBottomLayout;
    protected EditText messageEditText;// 文本消息编辑框
    protected View tvSend;// 文本消息编辑框
    protected View audioAnimLayout; // 录音动画布局
    protected View emojiButtonInInputBar;// 发送消息按钮
    protected View messageInputBar;
    protected UserInCallEvent event = new UserInCallEvent(false, false);//正在通话中

    private SessionCustomization customization;

    // 表情
    protected EmoticonPickerView emoticonPickerView;  // 贴图表情控件

    // 语音
    protected AudioRecorder audioMessageHelper;
    private Chronometer time;
    private TextView timerTip;
    private boolean started = false;
    private boolean cancelled = false;
    private boolean touched = false; // 是否按着
    private boolean isKeyboardShowed = true; // 是否显示键盘
    private final boolean isShowEditBar;  //是否显示输入框  false为显示，true为不显示
    private final boolean isShowGiftAndVideo;  //是否显示礼物和视频按钮  false为显示，true为不显示

    // state
    private boolean actionPanelBottomLayoutHasSetup = false;
    private boolean isTextAudioSwitchShow = true;

    // adapter
    private final List<BaseAction> actions;

    // data
    private long typingTime = 0;

    private boolean isRobotSession;

    private TextWatcher aitTextWatcher;
    private View mEditImage;
    private View mEditGift;
    private View mEditVideo;
    private View mEditCommonMsg;
    private ImageView mEditAudio;
    private final Context mContext;
    private LinearLayout messageActivityLayout;
    private Button mTvRecord;
    private static final int INPUT_TEXT = 10001;
    private static final int INPUT_VOICE = 10002;
    //当前输入模式
    private int currentInputType = INPUT_TEXT;

    public InputPanel(Container container, View view, List<BaseAction> actions, boolean isTextAudioSwitchShow, boolean isShowEditBar, boolean isShowGiftAndVideo, Context context) {
        this.container = container;
        this.view = view;
        this.actions = actions;
        this.uiHandler = new Handler();
        this.isTextAudioSwitchShow = isTextAudioSwitchShow;
        this.isShowEditBar = isShowEditBar;
        this.isShowGiftAndVideo = isShowGiftAndVideo;
        this.mContext = context;
        init();
    }

    public InputPanel(Container container, View view, List<BaseAction> actions, boolean isShowEditBar, boolean isShowGiftAndVideo, Context context) {
        this(container, view, actions, true, isShowEditBar, isShowGiftAndVideo, context);
    }

    public void onPause() {
        // 停止录音
        if (audioMessageHelper != null) {
            onEndAudioRecord(true);
        }
    }

    public void onDestroy() {
        // release
        if (audioMessageHelper != null) {
            audioMessageHelper.destroyAudioRecorder();
        }
    }

    public boolean collapse(boolean immediately) {
        boolean respond = (emoticonPickerView != null && emoticonPickerView.getVisibility() == View.VISIBLE
                || actionPanelBottomLayout != null && actionPanelBottomLayout.getVisibility() == View.VISIBLE);

        hideAllInputLayout(immediately);

        return respond;
    }

    /**
     * @param immediately
     * @param isInputTextHide 是否在输入文字模式下才全部隐藏
     * @return
     */
    public boolean collapse(boolean immediately, boolean isInputTextHide) {
        boolean respond = (emoticonPickerView != null && emoticonPickerView.getVisibility() == View.VISIBLE
                || actionPanelBottomLayout != null && actionPanelBottomLayout.getVisibility() == View.VISIBLE);
        if (isInputTextHide) {
            if (currentInputType == INPUT_TEXT) {
                hideAllInputLayout(immediately);
            }
        } else {
            hideAllInputLayout(immediately);
        }
        return respond;
    }

    public void addAitTextWatcher(TextWatcher watcher) {
        aitTextWatcher = watcher;
    }

    private void init() {
        initViews();
        initInputBarListener();
        initTextEdit();
        //TODO tag
        initAudioRecordButton();
        restoreText(false);

        for (int i = 0; i < actions.size(); ++i) {
            actions.get(i).setIndex(i);
            actions.get(i).setContainer(container);
        }
    }

    public void setCustomization(SessionCustomization customization) {
        this.customization = customization;
        if (customization != null) {
            emoticonPickerView.setWithSticker(customization.withSticker);
        }
    }

    public void reload(Container container, SessionCustomization customization) {
        this.container = container;
        setCustomization(customization);
    }

    private void initViews() {
        messageActivityLayout = view.findViewById(R.id.messageActivityLayout);
        //录音
        mTvRecord = view.findViewById(R.id.tv_record);
        audioAnimLayout = view.findViewById(R.id.layoutPlayAudio);
        time = view.findViewById(R.id.timer);
        timerTip = view.findViewById(R.id.timer_tip);

//        messageActivityLayout.setOnClickListener(v -> switchToTextLayout(false));
        // input bar
        messageActivityBottomLayout = view.findViewById(R.id.messageActivityBottomLayout);
        messageInputBar = view.findViewById(R.id.sv_input);
        emojiButtonInInputBar = view.findViewById(R.id.emoji_button);
        messageEditText = view.findViewById(R.id.editTextMessage);
        tvSend = view.findViewById(R.id.tv_send);
        mEditImage = view.findViewById(R.id.edit_image);
        mEditGift = view.findViewById(R.id.edit_gift);
        mEditVideo = view.findViewById(R.id.edit_video);
        mEditCommonMsg = view.findViewById(R.id.edit_common_msg);

        //录音按钮
        mEditAudio = view.findViewById(R.id.edit_audio);

        // 表情
        emoticonPickerView = view.findViewById(R.id.emoticon_picker_view);

        if (isShowEditBar) {
            messageInputBar.setVisibility(View.GONE);
        } else {
            messageInputBar.setVisibility(View.VISIBLE);
        }

        if (isShowGiftAndVideo) {
            mEditGift.setVisibility(View.GONE);
            mEditVideo.setVisibility(View.GONE);
        } else {
            mEditGift.setVisibility(View.VISIBLE);
            mEditVideo.setVisibility(View.VISIBLE);
        }

    }

    private void initInputBarListener() {
        emojiButtonInInputBar.setOnClickListener(clickListener);
        mEditImage.setOnClickListener(clickListener);
        mEditGift.setOnClickListener(clickListener);
        mEditVideo.setOnClickListener(clickListener);
        mEditAudio.setOnClickListener(clickListener);
        mEditCommonMsg.setOnClickListener(clickListener);
        tvSend.setOnClickListener(clickListener);
    }

    private void initTextEdit() {
        messageEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        messageEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                switchToTextLayout(true);
            }
            return false;
        });

        messageEditText.setOnFocusChangeListener((v, hasFocus) -> {
            messageEditText.setHint("");
        });

        messageEditText.addTextChangedListener(new TextWatcher() {
            private int start;
            private int count;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.count = count;
                if (aitTextWatcher != null) {
                    aitTextWatcher.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (aitTextWatcher != null) {
                    aitTextWatcher.beforeTextChanged(s, start, count, after);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    tvSend.setVisibility(View.GONE);
                } else {
                    tvSend.setVisibility(View.VISIBLE);
                }
                MoonUtil.replaceEmoticons(container.activity, s, start, count);

                int editEnd = messageEditText.getSelectionEnd();
                messageEditText.removeTextChangedListener(this);
                while (StringUtil.counterChars(s.toString()) > NimUIKitImpl.getOptions().maxInputTextLength && editEnd > 0) {
                    s.delete(editEnd - 1, editEnd);
                    editEnd--;
                }
                messageEditText.setSelection(editEnd);
                messageEditText.addTextChangedListener(this);

                if (aitTextWatcher != null) {
                    aitTextWatcher.afterTextChanged(s);
                }

                sendTypingCommand();
            }
        });
    }


    /**
     * 发送“正在输入”通知
     */
    private void sendTypingCommand() {
        if (container.account.equals(NimUIKit.getAccount())) {
            return;
        }

        if (container.sessionType == SessionTypeEnum.Team || container.sessionType == SessionTypeEnum.ChatRoom) {
            return;
        }

        if (System.currentTimeMillis() - typingTime > 5000L) {
            typingTime = System.currentTimeMillis();
            CustomNotification command = new CustomNotification();
            command.setSessionId(container.account);
            command.setSessionType(container.sessionType);
            CustomNotificationConfig config = new CustomNotificationConfig();
            config.enablePush = false;
            config.enableUnreadCount = false;
            command.setConfig(config);

            JSONObject json = new JSONObject();
            json.put("id", "1");
            command.setContent(json.toString());
            /**
             * 测试环境回调
             */
//            if (!BuildConfig.VERSION_TYPE.equals("VERSION_STATUS_RELEASE")) {
//                command.setEnv("pre");
//            }

            NIMClient.getService(MsgService.class).sendCustomNotification(command);
        }
    }

    /**
     * ************************* 键盘布局切换 *******************************
     */

    private final View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == emojiButtonInInputBar) {
                toggleEmojiLayout();
            } else if (v == mEditImage) {  //点击图片按钮
                if (null != mOnClickImage) {
                    mOnClickImage.onClickImage();
                }
            } else if (v == mEditGift) {  //点击礼物按钮
                if (null != mOnClickImage) {
                    mOnClickImage.onClickGift();
                }
            } else if (v == mEditVideo) {  //点击视频
                if (null != mOnClickImage) {
                    mOnClickImage.onClickVideo();
                }
            } else if (v == mEditAudio) {  //点击我加的录音按钮
                if (currentInputType == INPUT_TEXT) {
                    if (event.isUserInCall()) {
                        String typeAudio = event.isAudio() ? "语音" : "视频";
                        ToastHelper.showToast(mContext, "正在" + typeAudio + "通话中，请稍后再试...");
                        return;
                    }
                    String[] voicePermission = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS};
                    for (String permission : voicePermission) {
                        if (!PermissionX.isGranted((FragmentActivity) mContext, permission)) {
                            MessageDialog dialog = new MessageDialog();
                            dialog.setTitle("权限申请通知");
                            dialog.setContent("要使用麦克风录音权限，以正常使用上传录音、发送语音、音视频聊天等功能。");
                            dialog.setCancelAble(false);
                            dialog.setOnButtonClickListener(new MessageDialog.OnButtonClickListener() {
                                @Override
                                public boolean onLeftClick() {
                                    dialog.dismiss();
                                    return false;
                                }

                                @Override
                                public boolean onRightClick() {
                                    //权限允许
                                    PermissionX.init((FragmentActivity) mContext)
                                            .permissions(permission)
                                            .request((allGranted, grantedList, deniedList) -> {
                                                if (allGranted) {
                                                    toggleRecordAudioLayout();
                                                }
                                            });
                                    return false;
                                }
                            });
                            dialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "");
                            return;
                        }
                    }
                    PermissionX.init((FragmentActivity) mContext)
                            .permissions(voicePermission)
                            .request((allGranted, grantedList, deniedList) -> {
                                if (allGranted) {
                                    toggleRecordAudioLayout();
                                }
                            });
                } else if (currentInputType == INPUT_VOICE) {
                    switchToTextLayout(true);
                }

            } else if (v == mEditCommonMsg) {//常用语
                //TODO CommonWord
                try {
                    ARouterHelperKt.startARouterActivityForResult(RouteUrl.Message.MessageVquCommonWordActivity, (FragmentActivity) mContext,
                            Constants.RESULT_CODE_COMMON_WORD);
                } catch (ClassCastException e) {
                    Log.e(TAG, "onClick: 类型转换异常");
                }
            } else if (v == tvSend) {
                onTextMessageSendButtonPressed(messageEditText.getText().toString());
            }
        }
    };


    private OnClickImage mOnClickImage;

    public void setOnClickImage(OnClickImage onClickImage) {
        mOnClickImage = onClickImage;
    }

    public interface OnClickImage {   //把图片点击接口回调到messageFragment
        void onClickImage();

        void onClickGift();

        void onClickVideo();

        void onEmptyText();
    }

    // 点击edittext，切换键盘和更多布局
    private void switchToTextLayout(boolean needShowInput) {
        hideEmojiLayout();
        hideActionPanelLayout();
        hideRecordAudioLayout();

        messageEditText.setVisibility(View.VISIBLE);

        if (needShowInput) {
            uiHandler.postDelayed(showTextRunnable, SHOW_LAYOUT_DELAY);
        } else {
            hideInputMethod();
        }
    }

    // 发送文本消息
    public void onTextMessageSendButtonPressed(String content) {
        //TODO tag判断消息是否为空
        if (!TextUtils.isEmpty(content)) {
            if (!TextUtils.isEmpty(content.trim())) {
                IMMessage textMessage = createTextMessage(content);
                if (container.proxy.sendMessage(textMessage)) {
                    restoreText(true);
                }
            } else {
                if (null != mOnClickImage) {
                    mOnClickImage.onEmptyText();
                }
            }
        } else {
            if (null != mOnClickImage) {
                mOnClickImage.onEmptyText();
            }
        }
    }


    protected IMMessage createTextMessage(String text) {
        return MessageBuilder.createTextMessage(container.account, container.sessionType, text);
    }


    // 点击“+”号按钮，切换更多布局和键盘
    private void toggleActionPanelLayout() {
        if (actionPanelBottomLayout == null || actionPanelBottomLayout.getVisibility() == View.GONE) {
            showActionPanelLayout();
        } else {
            hideActionPanelLayout();
        }
    }

    //点击下面的语言按钮。展示录音按钮
    private void toggleRecordAudioLayout() {
        if (mTvRecord == null || mTvRecord.getVisibility() == View.GONE) {
            showAudioRecordLayout();
        } else {
            hideRecordAudioLayout();
        }
    }

    // 点击表情，切换到表情布局
    private void toggleEmojiLayout() {
        if (emoticonPickerView == null || emoticonPickerView.getVisibility() == View.GONE) {
            showEmojiLayout();
        } else {
            hideEmojiLayout();
        }
    }


    //隐藏录音布局
    private void hideRecordAudioLayout() {
        uiHandler.removeCallbacks(showAudioRecordLayoutRunable);
        if (mTvRecord != null) {
            mTvRecord.setVisibility(View.GONE);
        }
        messageEditText.setVisibility(View.VISIBLE);
        currentInputType = INPUT_TEXT;
        mEditAudio.setImageResource(R.mipmap.resources_tanta_nim_ic_click_sound);
    }


    // 隐藏表情布局
    private void hideEmojiLayout() {
        uiHandler.removeCallbacks(showEmojiRunnable);
        if (emoticonPickerView != null) {
            emoticonPickerView.setVisibility(View.GONE);
        }
    }

    // 隐藏更多布局
    private void hideActionPanelLayout() {
        uiHandler.removeCallbacks(showMoreFuncRunnable);
        if (actionPanelBottomLayout != null) {
            actionPanelBottomLayout.setVisibility(View.GONE);
        }
    }

    // 隐藏键盘布局
    private void hideInputMethod() {
        isKeyboardShowed = false;
        uiHandler.removeCallbacks(showTextRunnable);
        InputMethodManager imm = (InputMethodManager) container.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(messageEditText.getWindowToken(), 0);
        messageEditText.clearFocus();
    }

    // 隐藏语音布局
    private void hideAudioLayout() {
        messageEditText.setVisibility(View.VISIBLE);
    }

    // 显示表情布局
    private void showEmojiLayout() {
        hideInputMethod();
        hideActionPanelLayout();
        hideAudioLayout();

        //TODO tag
        hideRecordAudioLayout();

        messageEditText.requestFocus();
        uiHandler.postDelayed(showEmojiRunnable, 200);
        emoticonPickerView.setVisibility(View.VISIBLE);
        emoticonPickerView.show(this);
        container.proxy.onInputPanelExpand();
    }

    // 初始化更多布局
    private void addActionPanelLayout() {
        if (actionPanelBottomLayout == null) {
            View.inflate(container.activity, R.layout.nim_message_activity_actions_layout, messageActivityBottomLayout);
            actionPanelBottomLayout = view.findViewById(R.id.actionsLayout);
            actionPanelBottomLayoutHasSetup = false;
        }
        initActionPanelLayout();
    }

    // 显示键盘布局
    private void showInputMethod(EditText editTextMessage) {
        editTextMessage.requestFocus();
        //如果已经显示,则继续操作时不需要把光标定位到最后
        if (!isKeyboardShowed) {
            editTextMessage.setSelection(editTextMessage.getText().length());
            isKeyboardShowed = true;
        }

        InputMethodManager imm = (InputMethodManager) container.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextMessage, 0);

        container.proxy.onInputPanelExpand();
    }

    // 显示更多布局
    private void showActionPanelLayout() {
        addActionPanelLayout();
        hideEmojiLayout();
        hideInputMethod();

        uiHandler.postDelayed(showMoreFuncRunnable, SHOW_LAYOUT_DELAY);
        container.proxy.onInputPanelExpand();
    }

    //显示录音按钮
    private void showAudioRecordLayout() {
        recordAudioLayout();
        hideEmojiLayout();
        hideInputMethod();
        hideActionPanelLayout();
    }


    private void recordAudioLayout() {
        uiHandler.postDelayed(showAudioRecordLayoutRunable, SHOW_LAYOUT_DELAY);
        container.proxy.onInputPanelExpand();
        messageEditText.setVisibility(View.GONE);
        currentInputType = INPUT_VOICE;
        mEditAudio.setImageResource(R.mipmap.resources_vqu_nim_ic_click_text);
    }


    // 初始化具体more layout中的项目
    private void initActionPanelLayout() {
        if (actionPanelBottomLayoutHasSetup) {
            return;
        }

        ActionsPanel.init(view, actions);
        actionPanelBottomLayoutHasSetup = true;
    }

    private final Runnable showEmojiRunnable = new Runnable() {
        @Override
        public void run() {
            emoticonPickerView.setVisibility(View.VISIBLE);
        }
    };

    private final Runnable showMoreFuncRunnable = new Runnable() {
        @Override
        public void run() {
            actionPanelBottomLayout.setVisibility(View.VISIBLE);
        }
    };

    private final Runnable showAudioRecordLayoutRunable = new Runnable() {
        @Override
        public void run() {
            if (mTvRecord != null) {
                mTvRecord.setVisibility(View.VISIBLE);
            }
        }
    };

    private final Runnable showTextRunnable = new Runnable() {
        @Override
        public void run() {
            showInputMethod(messageEditText);
        }
    };

    private void restoreText(boolean clearText) {
        if (clearText) {
            messageEditText.setText("");
        }

    }


    /**
     * *************** IEmojiSelectedListener ***************
     */
    @Override
    public void onEmojiSelected(String key) {
        Editable mEditable = messageEditText.getText();
        if (key.equals("/DEL")) {
            messageEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            int start = messageEditText.getSelectionStart();
            int end = messageEditText.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            mEditable.replace(start, end, key);
        }
    }

    private Runnable hideAllInputLayoutRunnable;

    @Override
    public void onStickerSelected(String category, String item) {
        Log.i("InputPanel", "onStickerSelected, category =" + category + ", sticker =" + item);

        if (customization != null) {
            MsgAttachment attachment = customization.createStickerAttachment(category, item);
            IMMessage stickerMessage = MessageBuilder.createCustomMessage(container.account, container.sessionType, "贴图消息", attachment);
            container.proxy.sendMessage(stickerMessage);
        }
    }

    @Override
    public void onTextAdd(String content, int start, int length) {
        if (messageEditText.getVisibility() != View.VISIBLE ||
                (emoticonPickerView != null && emoticonPickerView.getVisibility() == View.VISIBLE)) {
            switchToTextLayout(true);
        } else {
            uiHandler.postDelayed(showTextRunnable, SHOW_LAYOUT_DELAY);
        }
        messageEditText.getEditableText().insert(start, content);
    }

    @Override
    public void onTextDelete(int start, int length) {
        if (messageEditText.getVisibility() != View.VISIBLE) {
            switchToTextLayout(true);
        } else {
            uiHandler.postDelayed(showTextRunnable, SHOW_LAYOUT_DELAY);
        }
        int end = start + length - 1;
        messageEditText.getEditableText().replace(start, end, "");
    }

    public int getEditSelectionStart() {
        return messageEditText.getSelectionStart();
    }


    /**
     * 隐藏所有输入布局
     */
    private void hideAllInputLayout(boolean immediately) {
        if (hideAllInputLayoutRunnable == null) {
            hideAllInputLayoutRunnable = () -> {
                hideInputMethod();
                hideActionPanelLayout();
                hideEmojiLayout();
                //TODO tag
                hideRecordAudioLayout();
            };
        }
        long delay = immediately ? 0 : ViewConfiguration.getDoubleTapTimeout();
        uiHandler.postDelayed(hideAllInputLayoutRunnable, delay);
    }

    /**
     * ****************************** 语音 ***********************************
     */
    private void initAudioRecordButton() {
        mTvRecord.setOnTouchListener((View.OnTouchListener) (v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                touched = true;
                initAudioRecord();
                onStartAudioRecord();
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL
                    || event.getAction() == MotionEvent.ACTION_UP) {
                touched = false;
                onEndAudioRecord(isCancelled(v, event));
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                touched = true;
                cancelAudioRecord(isCancelled(v, event));
            }

            return false;
        });
    }

    // 上滑取消录音判断
    private static boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        return event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth()
                || event.getRawY() < location[1] - 5;
    }

    /**
     * 初始化AudioRecord
     */
    private void initAudioRecord() {
        if (audioMessageHelper == null) {
            UIKitOptions options = NimUIKitImpl.getOptions();
            audioMessageHelper = new AudioRecorder(container.activity, options.audioRecordType, options.audioRecordMaxTime, this);
        }
    }

    /**
     * 开始语音录制
     */
    private void onStartAudioRecord() {
        if (UserManager.INSTANCE.isVideo()) {
            ToastUtils.showToast("正在语音/视频通话中，请稍后再试...", Toast.LENGTH_SHORT);
            return;
        }
        container.activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        audioMessageHelper.startRecord();
        cancelled = false;
    }

    /**
     * 结束语音录制
     *
     * @param cancel
     */
    private void onEndAudioRecord(boolean cancel) {
        started = false;
        container.activity.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        audioMessageHelper.completeRecord(cancel);

        stopAudioRecordAnim();
    }

    /**
     * 取消语音录制
     *
     * @param cancel
     */
    private void cancelAudioRecord(boolean cancel) {
        // reject
        if (!started) {
            return;
        }
        // no change
        if (cancelled == cancel) {
            return;
        }

        cancelled = cancel;
        updateTimerTip(cancel);
    }

    /**
     * 正在进行语音录制和取消语音录制，界面展示
     *
     * @param cancel
     */
    private void updateTimerTip(boolean cancel) {
        mTvRecord.setText("松开 发送");
        mTvRecord.setBackgroundResource(R.drawable.resources_vqu_nim_bg_pressed_edit);
        if (cancel) {
            timerTip.setText(R.string.recording_cancel_tip);
        } else {
            timerTip.setText(R.string.recording_cancel);
        }
    }

    /**
     * 开始语音录制动画
     */
    private void playAudioRecordAnim() {
        audioAnimLayout.setVisibility(View.VISIBLE);
        time.setBase(SystemClock.elapsedRealtime());
        time.start();
    }

    /**
     * 结束语音录制动画
     */
    private void stopAudioRecordAnim() {
        audioAnimLayout.setVisibility(View.GONE);
        mTvRecord.setText("按住 说话");
        mTvRecord.setBackgroundResource(R.drawable.resources_vqu_nim_bg_edit);
        time.stop();
        time.setBase(SystemClock.elapsedRealtime());
    }

    // 录音状态回调
    @Override
    public void onRecordReady() {

    }

    @Override
    public void onRecordStart(File audioFile, RecordType recordType) {
        started = true;
        if (!touched) {
            return;
        }
        updateTimerTip(false); // 初始化语音动画状态
        playAudioRecordAnim();
    }

    @Override
    public void onRecordSuccess(File audioFile, long audioLength, RecordType recordType) {
        IMMessage audioMessage = MessageBuilder.createAudioMessage(container.account, container.sessionType, audioFile, audioLength);
        container.proxy.sendMessage(audioMessage);
    }

    @Override
    public void onRecordFail() {
        if (started) {
            ToastHelper.showToast(container.activity, R.string.recording_error);
        }
    }

    @Override
    public void onRecordCancel() {

    }

    @Override
    public void onRecordReachedMaxTime(final int maxTime) {
        stopAudioRecordAnim();
        EasyAlertDialogHelper.createOkCancelDiolag(container.activity, "", container.activity.getString(R.string.recording_max_time), false, new EasyAlertDialogHelper.OnDialogActionListener() {
            @Override
            public void doCancelAction() {
            }

            @Override
            public void doOkAction() {
                audioMessageHelper.handleEndRecord(true, maxTime);
            }
        }).show();
    }


    public boolean isRecording() {
        return audioMessageHelper != null && audioMessageHelper.isRecording();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        //TODO CommonWord
        if (requestCode == Constants.RESULT_CODE_COMMON_WORD) {
            onTextMessageSendButtonPressed(data.getStringExtra(SpKey.KEY_COMMON_WORD));
            return;
        }
        int index = (requestCode << 16) >> 24;
        if (index != 0) {
            index--;
            if (index < 0 | index >= actions.size()) {
                LogUtil.d(TAG, "request code out of actions' range");
                return;
            }
            BaseAction action = actions.get(index);
            if (action != null) {
                action.onActivityResult(requestCode & 0xff, resultCode, data);
            }
        }
    }

    public void switchRobotMode(boolean isRobot) {
        isRobotSession = isRobot;
        if (isRobot) {
            emojiButtonInInputBar.setVisibility(View.GONE);
        } else {
            emojiButtonInInputBar.setVisibility(View.VISIBLE);
        }

    }

    public void setUserInCall(UserInCallEvent userInCall) {
        this.event = userInCall;
    }
}
