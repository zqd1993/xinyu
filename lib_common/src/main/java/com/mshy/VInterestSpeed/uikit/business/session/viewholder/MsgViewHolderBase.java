package com.mshy.VInterestSpeed.uikit.business.session.viewholder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.bean.VquUserInfo;
import com.mshy.VInterestSpeed.common.utils.UserSpUtils;
import com.mshy.VInterestSpeed.uikit.business.session.module.list.MsgAdapter;
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.mshy.VInterestSpeed.uikit.common.util.sys.TimeUtil;
import com.mshy.VInterestSpeed.uikit.common.widget.NIMImageView;
import com.mshy.VInterestSpeed.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.Map;


/**
 * 会话窗口消息列表项的ViewHolder基类，负责每个消息项的外层框架，包括头像，昵称，发送/接收进度条，重发按钮等。<br>
 * 具体的消息展示项可继承该基类，然后完成具体消息内容展示即可。
 */
public abstract class MsgViewHolderBase extends RecyclerViewHolder<BaseMultiItemFetchLoadAdapter, BaseViewHolder, IMMessage> {

    public MsgViewHolderBase(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
        this.adapter = adapter;
    }

    // basic
    protected View view;
    protected Context context;
    protected BaseMultiItemFetchLoadAdapter adapter;

    // data
    protected IMMessage message;

    // view
    protected View alertButton;
    protected View successButton;
    protected TextView timeTextView;
    protected ProgressBar progressBar;
    protected TextView nameTextView;
    protected FrameLayout contentContainer;
    protected LinearLayout nameContainer;
    protected TextView readReceiptTextView;
    protected TextView ackMsgTextView;

    private NIMImageView avatarLeft;
    private NIMImageView avatarRight;

    public ImageView nameIconView;

    //TODO tag显示扣除费用7的展示
    protected TextView mImageView;

    // contentContainerView的默认长按事件。如果子类需要不同的处理，可覆盖onItemLongClick方法
    // 但如果某些子控件会拦截触摸消息，导致contentContainer收不到长按事件，子控件也可在inflate时重新设置
    protected View.OnLongClickListener longClickListener;

    /// -- 以下接口可由子类覆盖或实现
    // 返回具体消息类型内容展示区域的layout res id
    abstract protected int getContentResId();

    // 在该接口中根据layout对各控件成员变量赋值
    abstract protected void inflateContentView();

    // 在该接口操作BaseViewHolder中的数据，进行事件绑定，可选
    protected void bindHolder(BaseViewHolder holder) {

    }

    // 将消息数据项与内容的view进行绑定
    abstract protected void bindContentView();

    // 内容区域点击事件响应处理。
    protected void onItemClick() {
    }

    // 内容区域长按事件响应处理。该接口的优先级比adapter中有长按事件的处理监听高，当该接口返回为true时，adapter的长按事件监听不会被调用到。
    protected boolean onItemLongClick() {
        return false;
    }

    // 当是接收到的消息时，内容区域背景的drawable id
    protected int leftBackground() {
        return NimUIKitImpl.getOptions().messageLeftBackground;
    }

    // 当是发送出去的消息时，内容区域背景的drawable id
    protected int rightBackground() {
        return NimUIKitImpl.getOptions().messageRightBackground;
    }

    // 返回该消息是不是居中显示
    protected boolean isMiddleItem() {
        return false;
    }

    // 返回该头像消息是不是居中显示
    protected boolean isMiddleAvatarItem() {
        return false;
    }

    // 是否显示头像，默认为显示
    protected boolean isShowHeadImage() {
        return true;
    }

    // 是否显示气泡背景，默认为显示
    protected boolean isShowBubble() {
        return true;
    }

    // 是否显示已读，默认为显示
    protected boolean shouldDisplayReceipt() {
        return true;
    }

    // 是否显示消息状态，默认为显示
    protected boolean shouldDisplayStatus() {
        return true;
    }

    /// -- 以下接口可由子类调用
    protected final MsgAdapter getMsgAdapter() {
        return (MsgAdapter) adapter;
    }

    protected boolean shouldDisplayNick() {
        return message.getSessionType() == SessionTypeEnum.Team && isReceivedMessage() && !isMiddleItem();
    }


    /**
     * 下载附件/缩略图
     */
    protected void downloadAttachment() {
        if (message.getAttachment() != null && message.getAttachment() instanceof FileAttachment)
            NIMClient.getService(MsgService.class).downloadAttachment(message, true);
    }

    // 设置FrameLayout子控件的gravity参数
    protected final void setGravity(View view, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = gravity;
    }

    // 设置控件的长宽
    protected void setLayoutParams(int width, int height, View... views) {
        for (View view : views) {
            ViewGroup.LayoutParams maskParams = view.getLayoutParams();
            maskParams.width = width;
            maskParams.height = height;
            view.setLayoutParams(maskParams);
        }
    }

    // 根据layout id查找对应的控件
    protected <T extends View> T findViewById(int id) {
        return (T) view.findViewById(id);
    }

    // 判断消息方向，是否是接收到的消息
    protected boolean isReceivedMessage() {
        return message.getDirect() == MsgDirectionEnum.In;
    }

    /// -- 以下是基类实现代码
    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        view = holder.getConvertView();
        context = holder.getContext();
        message = data;

        inflate();
        refresh();
        bindHolder(holder);
    }

    protected final void inflate() {
        timeTextView = findViewById(R.id.message_item_time);
        avatarLeft = findViewById(R.id.message_item_portrait_left);
        avatarRight = findViewById(R.id.message_item_portrait_right);
        alertButton = findViewById(R.id.message_item_alert);
        successButton = findViewById(R.id.message_item_success);
        progressBar = findViewById(R.id.message_item_progress);
        nameTextView = findViewById(R.id.message_item_nickname);
        contentContainer = findViewById(R.id.message_item_content);
        nameIconView = findViewById(R.id.message_item_name_icon);
        nameContainer = findViewById(R.id.message_item_name_layout);
        readReceiptTextView = findViewById(R.id.textViewAlreadyRead);
        ackMsgTextView = findViewById(R.id.team_ack_msg);

        mImageView = findViewById(R.id.coin_text);

        // 这里只要inflate出来后加入一次即可
        if (contentContainer.getChildCount() == 0) {
            View.inflate(view.getContext(), getContentResId(), contentContainer);
        }
        inflateContentView();
    }

    protected final void refresh() {
        setHeadImageView();
        setNameTextView();
        setTimeTextView();
        setStatus();
        setOnClickListener();
        setLongClickListener();
        setContent();
        setReadReceipt();
        setAckMsg();
        setCoinText();

        bindContentView();

    }


    /**
     * 显示扣除7费用的展示
     */
    private void setCoinText() {//这里是扣费消息
        if (isReceivedMessage()) {
            Map<String, Object> remoteExtension = message.getRemoteExtension();
            if (null != remoteExtension) {
                if (null != remoteExtension.get("is_cut")) {
                    int data = (int) remoteExtension.get("is_cut");
                    Log.i("tyy",data+"----is_cut");
                    if (data == 1) {
                        String money = (String) remoteExtension.get("money");
                        if (!TextUtils.isEmpty(money)) {
                            Drawable dra = context.getResources().getDrawable(R.mipmap.resources_tanta_nim_ic_msg_earn);
                            dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
                            mImageView.setCompoundDrawables(dra, null, null, null);
                            mImageView.setText("+" + money + "元");
                            //TODO 暂时不显示收益金额
                            mImageView.setVisibility(View.VISIBLE);
//                            mImageView.setVisibility(View.GONE);`
                        } else {
                            mImageView.setVisibility(View.GONE);
                        }

                    } else {
                        if (null != remoteExtension.get("is_free")) {
                            int isFree = (int) remoteExtension.get("is_free");
                            Log.i("tyy",isFree+"----isFree");
                            if (isFree == 1) {
                                mImageView.setVisibility(View.VISIBLE);
                                Drawable dra = context.getResources().getDrawable(R.mipmap.ic_free_chat);
                                dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
                                mImageView.setCompoundDrawables(dra, null, null, null);
                                mImageView.setText("聊天卡免费消息");
                            }else{
                                if (null != remoteExtension.get("vip")) {
                                    int vip = (int) remoteExtension.get("vip");
                                    if (vip == 0) {
                                        mImageView.setVisibility(View.GONE);
                                    } else {
                                        mImageView.setVisibility(View.VISIBLE);
                                        Drawable dra = context.getResources().getDrawable(R.mipmap.resources_tanta_nim_ic_msg_earn);
                                        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
                                        mImageView.setCompoundDrawables(dra, null, null, null);
                                        mImageView.setText("会员免费消息");
                                    }
                                } else {
                                    mImageView.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            if (null != remoteExtension.get("vip")) {
                                int vip = (int) remoteExtension.get("vip");
                                if (vip == 0) {
                                    mImageView.setVisibility(View.GONE);
                                } else {
                                    mImageView.setVisibility(View.VISIBLE);
                                    Drawable dra = context.getResources().getDrawable(R.mipmap.resources_tanta_nim_ic_msg_earn);
                                    dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
                                    mImageView.setCompoundDrawables(dra, null, null, null);
                                    mImageView.setText("会员免费消息");
                                }
                            } else {
                                mImageView.setVisibility(View.GONE);
                            }
                        }


                    }
                }
            } else {
                mImageView.setVisibility(View.GONE);
            }
        } else {
            mImageView.setVisibility(View.GONE);
        }

    }


//    public void refreshCurrentItem() {
//        if (message != null) {
//            refresh();
//        }
//    }

    /**
     * 设置时间显示
     */
    private void setTimeTextView() {
        if (getMsgAdapter().needShowTime(message)) {
            timeTextView.setVisibility(View.VISIBLE);
        } else {
            timeTextView.setVisibility(View.GONE);
            return;
        }

        String text = TimeUtil.getTimeShowString(message.getTime(), false);
        timeTextView.setText(text);
    }

    /**
     * 设置消息发送状态
     */
    private void setStatus() {
        if (isReceivedMessage() || !shouldDisplayStatus()||message.getMsgType()==MsgTypeEnum.tip) {
            progressBar.setVisibility(View.GONE);
            alertButton.setVisibility(View.GONE);
            successButton.setVisibility(View.GONE);
            return;
        }
        MsgStatusEnum status = message.getStatus();
        switch (status) {
            case fail:
                progressBar.setVisibility(View.GONE);
                alertButton.setVisibility(View.VISIBLE);
                successButton.setVisibility(View.GONE);
                break;
            case sending:
                progressBar.setVisibility(View.VISIBLE);
                alertButton.setVisibility(View.GONE);
                successButton.setVisibility(View.GONE);
                break;
            case success:
                successButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                alertButton.setVisibility(View.GONE);
            default:
                progressBar.setVisibility(View.GONE);
                alertButton.setVisibility(View.GONE);
                break;
        }
    }


    private void setHeadImageView() {
        NIMImageView show = isReceivedMessage() ? avatarLeft : avatarRight;
        NIMImageView hide = isReceivedMessage() ? avatarRight : avatarLeft;
        hide.setVisibility(View.GONE);
        if (!isShowHeadImage()) {
            show.setVisibility(View.GONE);
            return;
        }
        if (isMiddleItem()) {
            show.setVisibility(View.GONE);
        } else {
            show.setVisibility(View.VISIBLE);
            show.loadBuddyAvatar(message);
            if (isMiddleAvatarItem()) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) show.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                show.setLayoutParams(layoutParams);
            }
        }

    }

    private void setOnClickListener() {
        // 重发/重收按钮响应事件
        if (getMsgAdapter().getEventListener() != null) {
            alertButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getMsgAdapter().getEventListener().onFailedBtnClick(message);
                }
            });
        }

        // 内容区域点击事件响应， 相当于点击了整项
        contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });

        // 头像点击事件响应
        if (NimUIKitImpl.getSessionListener() != null) {
            View.OnClickListener portraitListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NimUIKitImpl.getSessionListener().onAvatarClicked(context, message);
                }
            };

            //TODO tag,添加点击自己头像
            View.OnClickListener avatarRightListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NimUIKitImpl.getSessionListener().onAvatarRightClicked(context, message);
                }
            };
            avatarLeft.setOnClickListener(portraitListener);
//            avatarRight.setOnClickListener(portraitListener);
            avatarRight.setOnClickListener(avatarRightListener);
        }
        // 已读回执响应事件
        if (NimUIKitImpl.getSessionListener() != null) {
            ackMsgTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NimUIKitImpl.getSessionListener().onAckMsgClicked(context, message);
                }
            });
        }
    }

    /**
     * item长按事件监听
     */
    private void setLongClickListener() {
        longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 优先派发给自己处理，
                if (!onItemLongClick()) {
                    if (getMsgAdapter().getEventListener() != null) {
                        getMsgAdapter().getEventListener().onViewHolderLongClick(contentContainer, view, message);
                        return true;
                    }
                }
                return false;
            }
        };
        // 消息长按事件响应处理
        contentContainer.setOnLongClickListener(longClickListener);

        // 头像长按事件响应处理
        if (NimUIKitImpl.getSessionListener() != null) {
            View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    NimUIKitImpl.getSessionListener().onAvatarLongClicked(context, message);
                    return true;
                }
            };
            avatarLeft.setOnLongClickListener(longClickListener);
            avatarRight.setOnLongClickListener(longClickListener);
        }
    }

    private void setNameTextView() {
        if (!shouldDisplayNick()) {
            nameTextView.setVisibility(View.GONE);
            return;
        }
        nameTextView.setVisibility(View.VISIBLE);
        nameTextView.setText(getNameText());
    }


    protected String getNameText() {
        if (message.getSessionType() == SessionTypeEnum.Team) {
        }
        return "";
    }

    private void setContent() {
        if (!isShowBubble() && !isMiddleItem()) {
            return;
        }

        LinearLayout bodyContainer = (LinearLayout) view.findViewById(R.id.message_item_body);

        // 调整container的位置
        int index = isReceivedMessage() ? 0 : bodyContainer.getChildCount() - 1;
        if (bodyContainer.getChildAt(index) != contentContainer) {
            bodyContainer.removeView(contentContainer);
            bodyContainer.addView(contentContainer, index);
        }

        if (isMiddleItem()) {
            setGravity(bodyContainer, Gravity.CENTER);
        } else {
            if (isReceivedMessage()) {
                setGravity(bodyContainer, Gravity.LEFT);
                if (null != message) {
                    MsgTypeEnum msgType = message.getMsgType();
                    if (msgType.getValue() != 1 && msgType.getValue() != 3) {  //图片和视频不显示背景
                        contentContainer.setBackgroundResource(leftBackground());
                    }
                }
            } else {
                setGravity(bodyContainer, Gravity.RIGHT);
                if (null != message) {
                    MsgTypeEnum msgType = message.getMsgType();
                    if (msgType.getValue() != 1 && msgType.getValue() != 3) {
                        contentContainer.setBackgroundResource(rightBackground());
                    }
                }
            }
        }
    }

    private void setReadReceipt() {
        final VquUserInfo loginBean = UserSpUtils.INSTANCE.getUserBean();

        if (shouldDisplayReceipt()
                && !TextUtils.isEmpty(getMsgAdapter().getUuid())
                && loginBean != null
                && (message.getFromAccount() != null && message.getFromAccount().equals(loginBean.getUserId()))
                && message.isRemoteRead()
                && message.getMsgType() != MsgTypeEnum.tip) {
            readReceiptTextView.setVisibility(View.VISIBLE);
            successButton.setVisibility(View.GONE);
        } else {
            readReceiptTextView.setVisibility(View.GONE);
        }
//        if (shouldDisplayReceipt()
//                && !TextUtils.isEmpty(getMsgAdapter().getUuid())
//                && message.getUuid().equals(getMsgAdapter().getUuid())) {
//            readReceiptTextView.setVisibility(View.VISIBLE);
//        } else {
//            readReceiptTextView.setVisibility(View.GONE);
//        }
    }

    private void setAckMsg() {
        if (message.getSessionType() == SessionTypeEnum.Team && message.needMsgAck()) {
            if (isReceivedMessage()) {
                // 收到的需要已读回执的消息，需要给个反馈
                ackMsgTextView.setVisibility(View.GONE);
                NIMSDK.getTeamService().sendTeamMessageReceipt(message);
            } else {
                // 自己发的需要已读回执的消息，显示未读人数
                ackMsgTextView.setVisibility(View.VISIBLE);
                if (message.getTeamMsgAckCount() == 0 && message.getTeamMsgUnAckCount() == 0) {
                    ackMsgTextView.setText("还未查看");
                } else {
                    ackMsgTextView.setText(message.getTeamMsgUnAckCount() + "人未读");
                }
            }
        } else {
            ackMsgTextView.setVisibility(View.GONE);
        }
    }
}
