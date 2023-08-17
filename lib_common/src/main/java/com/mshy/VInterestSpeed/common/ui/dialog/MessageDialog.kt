package com.mshy.VInterestSpeed.common.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.databinding.CommonDialogMessageBinding
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import com.mshy.VInterestSpeed.common.utils.ResUtils


class MessageDialog : BaseDialogFragment<CommonDialogMessageBinding>(), View.OnClickListener {

    private var mTitle: CharSequence? = null
    private var mTitleSize: Float = 0f
    private var mContent: CharSequence? = null
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

    private var mGravity: Int? = null


    private fun initContent() {
        if (mGravity != null) {
            mBinding.tvCommonDialogMessageContent.gravity = mGravity ?: 0
        }

        if (mContent.isNullOrEmpty()) {
            mBinding.tvCommonDialogMessageContent.visibility = View.GONE
        } else {
            if (mIsHtml) {
                mBinding.tvCommonDialogMessageContent.isVerticalScrollBarEnabled = true
                mBinding.tvCommonDialogMessageContent.autoLinkMask = Linkify.WEB_URLS
                mBinding.tvCommonDialogMessageContent.movementMethod =
                    LinkMovementMethod.getInstance()
            }
            mBinding.tvCommonDialogMessageContent.text = mContent
        }
    }

    private fun initTitle() {
        if (mTitle.isNullOrEmpty()) {
            mBinding.tvCommonDialogMessageTitle.visibility = View.GONE
        } else {
            mBinding.tvCommonDialogMessageTitle.text = mTitle
            if (mTitleSize > 0) {
                mBinding.tvCommonDialogMessageTitle.textSize = mTitleSize
            }
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
    fun setTitle(title: CharSequence): MessageDialog {
        mTitle = title
        return this
    }

    fun setTitleSize(titleSize: Float): MessageDialog {
        mTitleSize = titleSize
        return this
    }

    /**
     * 设置Dialog标题
     */
    fun setTitle(@StringRes title: Int): MessageDialog {
        mTitle = ResUtils.getString(title)
        return this
    }

    /**
     * 设置Dialog内容
     */
    fun setContent(content: CharSequence): MessageDialog {
        mContent = content
        return this
    }

    /**
     * 设置Dialog内容
     */
    fun setContent(@StringRes content: Int): MessageDialog {
        mContent = ResUtils.getString(content)
        return this
    }

    /**
     * 设置右边按钮文字
     */
    fun setRightText(rightText: CharSequence): MessageDialog {
        mRightText = rightText
        return this
    }

    /**
     * 设置右边按钮文字
     */
    fun setRightText(@StringRes rightText: Int): MessageDialog {
        mRightText = ResUtils.getString(rightText)
        return this
    }

    /**
     * 设置左边按钮文字
     */
    fun setLeftText(leftText: CharSequence): MessageDialog {
        mLeftText = leftText
        return this
    }

    /**
     * 设置左边按钮文字
     */
    fun setLeftText(@StringRes leftText: Int): MessageDialog {
        mLeftText = ResUtils.getString(leftText)
        return this
    }

    /**
     * 设置单个按钮的文字
     */
    fun setSingleText(@StringRes singleText: Int): MessageDialog {
        mSingleText = ResUtils.getString(singleText)
        return this
    }

    /**
     * 设置单个按钮的文字
     */
    fun setSingleText(singleText: String): MessageDialog {
        mSingleText = singleText
        return this
    }

    /**
     * 是否Html文本
     * true Html文本
     */
    fun setIsHtml(isHtml: Boolean): MessageDialog {
        mIsHtml = isHtml
        return this
    }

    /**
     * 是否单个按钮
     * true 单个按钮
     */
    fun setIsSingleButton(isSingleButton: Boolean): MessageDialog {
        mIsSingleButton = isSingleButton
        return this
    }

    /**
     * 设置是否可以关闭
     */
    fun setCancelAble(cancelAble: Boolean): MessageDialog {
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
                if (mOnButtonClickListener?.onLeftClick() != true) {
                    dismiss()
                }
            }
            R.id.stv_common_dialog_message_btn_right -> {
                if (mOnButtonClickListener?.onRightClick() != true) {
                    dismiss()
                }
            }
            R.id.stv_common_dialog_message_btn_single -> {
                if (mOnSingleButtonClickListener?.onClick() != true) {
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
        fun onLeftClick(): Boolean

        /**
         * @return true 将拦截默认处理事件，默认false
         */
        fun onRightClick(): Boolean
    }

    /**
     * 单个按钮的回调，在[onClick]方法中调用，
     */
    interface OnSingleButtonClickListener {

        /**
         * @return true 将拦截默认处理事件，默认false
         */
        fun onClick(): Boolean
    }

    override fun CommonDialogMessageBinding.initView() {

        initTitle()

        initContent()

        if (mIsSingleButton) {
            initSingleButton()
        } else {
            initDoubleButton()
        }

    }

    override fun initWindow() {
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.CENTER
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.setCanceledOnTouchOutside(mCancelAble)
        dialog?.setCancelable(mCancelAble)
    }
}