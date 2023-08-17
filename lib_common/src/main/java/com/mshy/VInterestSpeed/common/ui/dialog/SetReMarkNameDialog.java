package com.mshy.VInterestSpeed.common.ui.dialog;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mshy.VInterestSpeed.common.R;


/**
 * FileName: com.live.vquonline.view.main.dialog
 * Author: Reisen
 * Date: 2020/12/11 10:25
 * Description:
 * History:
 */
public class SetReMarkNameDialog extends BaseDialogFragment {

    private EditText mEtContent;
    private TextView mTvTitle;
    private TextView mTvLeft;
    private TextView mTvRight;

    private String mTitle;
    private String mLeftText = "取消";
    private String mRightText = "确定";
    private String mContent = "";

    private OnClickListener mClickListener;


    @Override
    protected void initView(View view) {
        mEtContent = view.findViewById(R.id.et_content);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvLeft = view.findViewById(R.id.tv_left);
        mTvRight = view.findViewById(R.id.tv_right);

        mTvTitle.setText(mTitle);
        mTvLeft.setText(mLeftText);
        mTvRight.setText(mRightText);
        mEtContent.setText(mContent);

        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mContent = s.toString();
            }
        });

        mTvLeft.setOnClickListener(v -> {
            if (mClickListener != null) {
                mClickListener.onCancel();
            }
            dismiss();
        });
        mTvRight.setOnClickListener(v -> {
            if (mClickListener != null) {
                mClickListener.onConfirm(mContent);
            }
            dismiss();
        });
    }

    public SetReMarkNameDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    public SetReMarkNameDialog setContent(String content) {
        mContent = content;
        return this;
    }

    public SetReMarkNameDialog setLeftText(String content) {
        mLeftText = content;
        return this;
    }

    public SetReMarkNameDialog setRightText(String content) {
        mRightText = content;
        return this;
    }

    public SetReMarkNameDialog setOnClickListener(OnClickListener clickListener) {
        this.mClickListener = clickListener;
        return this;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.message_vqu_dialog_set_remark;
    }

    public interface OnClickListener {
        void onCancel();

        void onConfirm(String content);
    }
}
