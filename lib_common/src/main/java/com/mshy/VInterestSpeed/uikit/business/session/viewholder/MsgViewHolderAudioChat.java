package com.mshy.VInterestSpeed.uikit.business.session.viewholder;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.utils.UserSpUtils;
import com.mshy.VInterestSpeed.uikit.attchment.MessageVquAudioAttachment;
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.holder.BaseViewHolder;


/**
 * Created by zhangbin on 2018/12/6.
 */

public class MsgViewHolderAudioChat extends MsgViewHolderBase {

    private LinearLayout mLl_left;
    private LinearLayout mLl_right;
    private TextView mTv_left_video_message;
    private TextView mTv_right_video_message;
    private ImageView mIvVideoLeft;
    private ImageView mIvVideoRight;
    private MessageVquAudioAttachment mVideoAttachment;

    public MsgViewHolderAudioChat(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.common_tanta_nim_video_custom_message;
    }

    @Override
    protected void inflateContentView() {
        mLl_left = view.findViewById(R.id.ll_left);
        mLl_right = view.findViewById(R.id.ll_right);
        mTv_left_video_message = view.findViewById(R.id.tv_left_video_message);
        mTv_right_video_message = view.findViewById(R.id.tv_right_video_message);
        mIvVideoLeft = view.findViewById(R.id.iv_video_left);
        mIvVideoRight = view.findViewById(R.id.iv_video_right);
    }

    @Override
    protected void bindHolder(BaseViewHolder holder) {
        mLl_left.setOnClickListener(view -> {
            if (getMsgAdapter().getEventListener() != null) {
                getMsgAdapter().getEventListener().onAudioClick();
            }
        });
        mLl_right.setOnClickListener(view -> {
            if (getMsgAdapter().getEventListener() != null) {
                getMsgAdapter().getEventListener().onAudioClick();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void bindContentView() {
        if (message.getAttachment() == null) {
            return;
        }
        mVideoAttachment = (MessageVquAudioAttachment) message.getAttachment();
        switch (mVideoAttachment.getType()) {
            case 73:

                if (mVideoAttachment.getCalltype() == 1) {
                    if (UserSpUtils.INSTANCE.getUserBean() != null
                            && !TextUtils.isEmpty(UserSpUtils.INSTANCE.getUserBean().getUserId())) {
                        if (mVideoAttachment.getFrom_uid().equals(UserSpUtils.INSTANCE.getUserBean().getUserId())) {  //发送的
                            mLl_left.setVisibility(View.GONE);
                            mLl_right.setVisibility(View.VISIBLE);
                            mTv_right_video_message.setTextColor(Color.parseColor("#ffffff"));
                            mTv_right_video_message.setText(mVideoAttachment.getContent() + "");
                            if (mVideoAttachment.getStatus() == 1) {
                                // mTv_right_video_message.setText("已取消");
                                mIvVideoRight.setImageResource(R.mipmap.resources_vqu_nim_ic_audio_right_un);
                            } else if (mVideoAttachment.getStatus() == 2) {
                                //  mTv_right_video_message.setText("对方已拒绝");
                                mIvVideoRight.setImageResource(R.mipmap.resources_vqu_nim_ic_audio_right_un);
                            } else if (mVideoAttachment.getStatus() == 3) {
                                // mTv_right_video_message.setText("超时未接听");
                                mIvVideoRight.setImageResource(R.mipmap.resources_vqu_nim_ic_audio_right_un);
                            } else if (mVideoAttachment.getStatus() == 4) {
                                //  mTv_right_video_message.setText("语音时长" + secondToTime(mVideoAttachment.getCall_time()));
                                mIvVideoRight.setImageResource(R.mipmap.resources_vqu_nim_ic_audio_right);
                            } else {
                                mIvVideoRight.setImageResource(R.mipmap.resources_vqu_nim_ic_audio_right);
                            }
                        } else {  //接收的
                            mLl_left.setVisibility(View.VISIBLE);
                            mLl_right.setVisibility(View.GONE);
                            mTv_left_video_message.setText(mVideoAttachment.getContent() + "");
                            if (mVideoAttachment.getStatus() == 1) {
                                // mTv_left_video_message.setText("对方已取消");
                                mIvVideoLeft.setImageResource(R.mipmap.resources_vqu_nim_ic_audio_left_un);
                            } else if (mVideoAttachment.getStatus() == 2) {
                                //   mTv_left_video_message.setText("已拒绝");
                                mIvVideoLeft.setImageResource(R.mipmap.resources_vqu_nim_ic_audio_left_un);
                            } else if (mVideoAttachment.getStatus() == 3) {
                                //   mTv_left_video_message.setText("超时未接听");
                                mIvVideoLeft.setImageResource(R.mipmap.resources_vqu_nim_ic_audio_left_un);
                            } else if (mVideoAttachment.getStatus() == 4) {
                                //   mTv_left_video_message.setText("语音时长:" + secondToTime(mVideoAttachment.getCall_time()));
                                mIvVideoLeft.setImageResource(R.mipmap.resources_vqu_nim_ic_audio_left);
                            } else {
                                mIvVideoLeft.setImageResource(R.mipmap.resources_vqu_nim_ic_audio_left);
                            }
                        }
                    }

                }
                break;
        }
    }


    private String secondToTime(long second) {
        second = second % 86400;
        long hours = second / 3600;
        second = second % 3600;
        long minutes = second / 60;
        second = second % 60;

        String strHours = String.format("%02d", hours);
        String strMinutes = String.format("%02d", minutes);
        String strSecond = String.format("%02d", second);
        return strHours + ":" + strMinutes + ":" + strSecond;
    }

    @Override
    protected boolean shouldDisplayReceipt() {
        return false;
    }

    @Override
    protected boolean shouldDisplayStatus() {
        return false;
    }

}
