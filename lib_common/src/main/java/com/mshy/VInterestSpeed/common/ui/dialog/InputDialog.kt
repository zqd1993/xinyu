package com.mshy.VInterestSpeed.common.ui.dialog

import android.text.InputFilter
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.databinding.CommonDialogInputBinding
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import com.mshy.VInterestSpeed.common.utils.ResUtils

/**
 * author: Lau
 * date: 2022/4/25
 * description:
 */
class InputDialog : BaseDialogFragment<CommonDialogInputBinding>(), View.OnClickListener {
    private var mTitle: CharSequence? = null
    private var mContent: CharSequence? = null
    private var mHint: CharSequence? = null
    private var mRightText: CharSequence? = null
    private var mLeftText: CharSequence? = null
    private var mSingleText: CharSequence? = null
    private var mIsHtml = false
    private var mIsSingleButton = false
    private var mOnButtonClickListener: OnButtonClickListener? = null
    private var mOnSingleButtonClickListener: OnSingleButtonClickListener? = null
    private var mLeftButtonColor: Int = 0
    private var mLeftButtonStartColor: Int = 0
    private var mLeftButtonEndColor: Int = 0
    private var mRightButtonColor: Int = 0
    private var mRightButtonStartColor: Int = 0
    private var mRightButtonEndColor: Int = 0
    private var mHintColor: Int = 0
    private var mContextMaxLength: Int = 0

    private var mGravity: Int? = null


    private fun initContent() {
        if (mGravity != null) {
            mBinding.tvCommonDialogMessageContent.gravity = mGravity ?: 0
        }

        if (!mContent.isNullOrEmpty()) {
            mBinding.tvCommonDialogMessageContent.setText(mContent)
        }

        if (!mHint.isNullOrEmpty()) {
            mBinding.tvCommonDialogMessageContent.hint = mHint
        }

        if (mHintColor != 0) {
            mBinding.tvCommonDialogMessageContent.setHintTextColor(mHintColor)
        }

        if (mContextMaxLength > 0) {
            mBinding.tvCommonDialogMessageContent.filters = arrayOf(
                InputFilter.LengthFilter(mContextMaxLength)
            )
            mBinding.tvCommonDialogMessageLength.text =
                "${mBinding.tvCommonDialogMessageContent.text.length}/$mContextMaxLength"
            mBinding.tvCommonDialogMessageLength.visibility = View.VISIBLE

            mBinding.tvCommonDialogMessageContent.addTextChangedListener {
                mBinding.tvCommonDialogMessageLength.text = "${it?.length ?: 0}/$mContextMaxLength"
            }
        }
    }

    private fun initTitle() {
        if (mTitle.isNullOrEmpty()) {
            mBinding.tvCommonDialogMessageTitle.visibility = View.GONE
        } else {
            mBinding.tvCommonDialogMessageTitle.text = mTitle
        }
    }

    private fun initDoubleButton() {
        mBinding.stvCommonDialogMessageBtnLeft.setOnClickListener(this)
        mBinding.stvCommonDialogMessageBtnRight.setOnClickListener(this)

        if (!mRightText.isNullOrEmpty()) {
            mBinding.stvCommonDialogMessageBtnRight.text = mRightText
        }

        if (!mLeftText.isNullOrEmpty()) {
            mBinding.stvCommonDialogMessageBtnLeft.text = mLeftText
        }


        if (mLeftButtonStartColor != 0 && mLeftButtonEndColor != 0) {
            mBinding.stvCommonDialogMessageBtnLeft.setStartColor(
                mLeftButtonStartColor,
                mLeftButtonEndColor
            )
        } else if (mLeftButtonColor != 0) {
            mBinding.stvCommonDialogMessageBtnLeft.solidColor = mLeftButtonColor
        }

        if (mRightButtonStartColor != 0 && mRightButtonEndColor != 0) {
            mBinding.stvCommonDialogMessageBtnRight.setStartColor(
                mRightButtonStartColor,
                mRightButtonEndColor
            )
        } else if (mRightButtonColor != 0) {
            mBinding.stvCommonDialogMessageBtnRight.solidColor = mRightButtonColor
        }
    }

    private fun initSingleButton() {
        mBinding.llCommonDialogMessageDoulbeBtnParent.visibility = View.GONE
        mBinding.stvCommonDialogMessageBtnSingle.visibility = View.VISIBLE
        mBinding.stvCommonDialogMessageBtnSingle.setOnClickListener(this)

        if (!mSingleText.isNullOrEmpty()) {
            mBinding.stvCommonDialogMessageBtnSingle.text = mSingleText
        }
    }

    /**
     * 设置Dialog标题
     */
    fun setTitle(title: CharSequence): InputDialog {
        mTitle = title
        return this
    }

    /**
     * 设置Dialog标题
     */
    fun setTitle(@StringRes title: Int): InputDialog {
        mTitle = ResUtils.getString(title)
        return this
    }

    /**
     * 设置Dialog内容
     */
    fun setContent(content: CharSequence): InputDialog {
        mContent = content
        return this
    }

    /**
     * 设置Dialog内容
     */
    fun setContent(@StringRes content: Int): InputDialog {
        mContent = ResUtils.getString(content)
        return this
    }

    /**
     * 设置Dialog内容
     */
    fun setContentMaxLength(maxLength: Int): InputDialog {
        mContextMaxLength = maxLength
        return this
    }

    /**
     * 设置Dialog输入框提示文案
     */
    fun setHint(hint: CharSequence): InputDialog {
        mHint = hint
        return this
    }

    /**
     * 设置Dialog输入框提示文案
     */
    fun setHint(@StringRes hint: Int): InputDialog {
        mHint = ResUtils.getString(hint)
        return this
    }

    /**
     * 设置Dialog输入框提示文案颜色
     */
    fun setHintColor(@ColorInt color: Int): InputDialog {
        mHintColor = color
        return this
    }

    /**
     * 设置右边按钮文字
     */
    fun setRightText(rightText: CharSequence): InputDialog {
        mRightText = rightText
        return this
    }

    /**
     * 设置右边按钮文字
     */
    fun setRightText(@StringRes rightText: Int): InputDialog {
        mRightText = ResUtils.getString(rightText)
        return this
    }

    /**
     * 设置左边按钮文字
     */
    fun setLeftText(leftText: CharSequence): InputDialog {
        mLeftText = leftText
        return this
    }

    /**
     * 设置左边按钮文字
     */
    fun setLeftText(@StringRes leftText: Int): InputDialog {
        mLeftText = ResUtils.getString(leftText)
        return this
    }

    /**
     * 设置单个按钮的文字
     */
    fun setSingleText(@StringRes leftText: Int): InputDialog {
        mSingleText = ResUtils.getString(leftText)
        return this
    }

    /**
     * 是否Html文本
     * true Html文本
     */
    fun setIsHtml(isHtml: Boolean): InputDialog {
        mIsHtml = isHtml
        return this
    }

    /**
     * 是否单个按钮
     * true 单个按钮
     */
    fun setIsSingleButton(isSingleButton: Boolean): InputDialog {
        mIsSingleButton = isSingleButton
        return this
    }

    /**
     * 设置是否可以关闭
     */
    fun setCancelAble(cancelAble: Boolean): InputDialog {
        mCancelAble = cancelAble
        return this
    }

    /**
     * 设置左边按钮颜色
     */
    fun setLeftButtonColor(@ColorInt color: Int) {
        mLeftButtonColor = color
    }

    /**
     * 设置左边按钮颜色，渐变色
     */
    fun setLeftButtonColor(@ColorInt startColor: Int, @ColorInt endColor: Int) {
        mLeftButtonStartColor = startColor
        mLeftButtonEndColor = endColor
    }

    /**
     * 设置右边按钮颜色
     */
    fun setRightButtonColor(@ColorInt color: Int) {
        mRightButtonColor = color
    }

    /**
     * 设置右边按钮颜色，渐变色
     */
    fun setRightButtonColor(@ColorInt startColor: Int, @ColorInt endColor: Int) {
        mRightButtonStartColor = startColor
        mRightButtonEndColor = endColor
    }

    /**
     * 设置内容的Gravity
     */
    fun setContentGravity(gravity: Int) {
        mGravity = gravity
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.stv_common_dialog_message_btn_left -> {
                if (mOnButtonClickListener?.onLeftClick(mBinding.tvCommonDialogMessageContent.text.toString()) != true) {
                    dismiss()
                }
            }
            R.id.stv_common_dialog_message_btn_right -> {
                if (mOnButtonClickListener?.onRightClick(mBinding.tvCommonDialogMessageContent.text.toString()) != true) {
                    dismiss()
                }
            }
            R.id.stv_common_dialog_message_btn_single -> {
                if (mOnSingleButtonClickListener?.onClick(mBinding.tvCommonDialogMessageContent.text.toString()) != true) {
                    dismiss()
                }
            }
        }
    }

    /**
     * 两个按钮的回调
     */
    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        mOnButtonClickListener = listener
    }

    /**
     * 单个按钮的回调
     * @param listener [OnSingleButtonClickListener]对象
     */
    fun setOnButtonClickListener(listener: OnSingleButtonClickListener) {
        mOnSingleButtonClickListener = listener
    }

    /**
     * 两个按钮的回调，在[onClick]方法中调用，
     */
    interface OnButtonClickListener {
        /**
         * @return true 将拦截默认处理事件，默认false
         */
        fun onLeftClick(content: String?): Boolean

        /**
         * @return true 将拦截默认处理事件，默认false
         */
        fun onRightClick(content: String?): Boolean
    }

    /**
     * 单个按钮的回调，在[onClick]方法中调用，
     */
    interface OnSingleButtonClickListener {

        /**
         * @return true 将拦截默认处理事件，默认false
         */
        fun onClick(content: String?): Boolean
    }

    override fun CommonDialogInputBinding.initView() {

        initTitle()

        initContent()

        if (mIsSingleButton) {
            initSingleButton()
        } else {
            initDoubleButton()
        }
    }
}