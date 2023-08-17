package com.mshy.VInterestSpeed.uikit.business.session.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.utils.JumpUtils;
import com.mshy.VInterestSpeed.uikit.attchment.MessageVquSysInfoAttachment;
import com.mshy.VInterestSpeed.uikit.bean.FieldListA;
import com.mshy.VInterestSpeed.uikit.bean.NIMVquSysInfoTextBean;
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

import java.util.List;

/**
 * Created by zhangbin on 2018/12/5.
 * 自定义普通系统通知
 */

public class MsgViewHolderSysInfo extends MsgViewHolderBase {

    private MessageVquSysInfoAttachment mSysInfoTextAttachment;
    private TextView mTv_text_title;
    private TextView mTv_text_context1;
    private TextView mTv_text_fields;
    private TextView mTv_text_context2;
    private NIMVquSysInfoTextBean mSysInfoTextBean;

    public MsgViewHolderSysInfo(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.common_vqu_nim_sys_info_text_attachment;
    }

    @Override
    protected void inflateContentView() {
        mTv_text_title = view.findViewById(R.id.tv_text_title);
        mTv_text_context1 = view.findViewById(R.id.tv_text_context1);
        mTv_text_fields = view.findViewById(R.id.tv_text_fields);
        mTv_text_context2 = view.findViewById(R.id.tv_text_context2);
    }

    @Override
    protected void bindContentView() {
        if (message.getAttachment() == null) {
            return;
        }
        mSysInfoTextAttachment = (MessageVquSysInfoAttachment) message.getAttachment();
        switch (mSysInfoTextAttachment.getType()) {
            case 11:   //资金流水通知

                mSysInfoTextBean = mSysInfoTextAttachment.getSysInfoTextBean();
                mTv_text_title.setText(mSysInfoTextBean.getTitle());

                if (TextUtils.isEmpty(mSysInfoTextBean.getTxt1())) {
                    mTv_text_context1.setVisibility(View.GONE);
                } else {
                    mTv_text_context1.setVisibility(View.VISIBLE);
                    mTv_text_context1.setText(mSysInfoTextBean.getTxt1());
                }

                List<FieldListA> fields = mSysInfoTextBean.getFields();
                if (null == fields || fields.size() == 0) {
                    mTv_text_fields.setVisibility(View.GONE);
                } else {
                    mTv_text_fields.setVisibility(View.VISIBLE);
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 0; i < fields.size(); i++) {
                        FieldListA field = fields.get(i);
                        String key = field.getN();
                        String value = field.getV();
                        if (i == fields.size() - 1) {
                            stringBuilder.append(key).append("：").append(value);
                        } else {
                            stringBuilder.append(key).append("：").append(value).append("\n");
                        }
                    }
                    mTv_text_fields.setText(stringBuilder);
                }
                if (TextUtils.isEmpty(mSysInfoTextBean.getTxt2())) {
                    mTv_text_context2.setVisibility(View.GONE);
                } else {
                    mTv_text_context2.setVisibility(View.VISIBLE);
                    mTv_text_context2.setText(mSysInfoTextBean.getTxt2());
                }
                break;
        }
    }

    @Override
    protected void onItemClick() {//系统消息跳转
        if (mSysInfoTextBean.getLink_type() != 0) {
            JumpUtils.jump(mSysInfoTextBean.getLink_type(), mSysInfoTextBean.getLink_url(), context);
        }
    }

    /**
     * 禁止长按
     *
     * @return
     */
    @Override
    protected boolean onItemLongClick() {
        return true;
    }

    /**
     * 居中显示
     *
     * @return
     */
    @Override
    protected boolean isMiddleItem() {
        return false;
    }

    /**
     * 不显示头像
     *
     * @return
     */
    @Override
    protected boolean isShowHeadImage() {
        return true;
    }

    /**
     * 不显示气泡
     *
     * @return
     */
    @Override
    protected boolean isShowBubble() {
        return true;
    }

    /**
     * 不显示已读
     *
     * @return
     */
    @Override
    protected boolean shouldDisplayReceipt() {
        return false;
    }
}
