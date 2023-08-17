package com.mshy.VInterestSpeed.common.ui.dialog

import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.databinding.CommonDialogTopDownButtonMessageBinding
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import com.mshy.VInterestSpeed.common.utils.ResUtils

class TopDownButtonMessageDialog : BaseDialogFragment<CommonDialogTopDownButtonMessageBinding>(),
    View.OnClickListener {

    private var mTitle: CharSequence? = null
    private var mContent: CharSequence? = null
    private var mTopText: CharSequence? = null
    private var mDownText: CharSequence? = null
    private var mIsHtml = false

    //    private var mIsSingleButton = false
    private var mOnButtonClickListener: OnButtonClickListener? = null

    private var mDownButtonColor: Int = 0
    private var mDownButtonStartColor: Int = 0
    private var mDownButtonEndColor: Int = 0
    private var mTopButtonColor: Int = 0
    private var mTopButtonStartColor: Int = 0
    private var mTopButtonEndColor: Int = 0

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
        }
    }

    private fun initDoubleButton() {
        mBinding.stvCommonDialogMessageBtnBottom.setOnClickListener(this)
        mBinding.stvCommonDialogMessageBtnTop.setOnClickListener(this)

        if (!mTopText.isNullOrEmpty()) {
            mBinding.stvCommonDialogMessageBtnTop.text = mTopText
        }

        if (!mDownText.isNullOrEmpty()) {
            mBinding.stvCommonDialogMessageBtnBottom.text = mDownText
        }


        if (mDownButtonColor != 0 && mDownButtonEndColor != 0) {
            mBinding.stvCommonDialogMessageBtnBottom.setStartColor(
                mDownButtonColor,
                mDownButtonEndColor
            )
        } else if (mDownButtonColor != 0) {
            mBinding.stvCommonDialogMessageBtnBottom.solidColor = mDownButtonColor
        }

        if (mTopButtonStartColor != 0 && mTopButtonEndColor != 0) {
            mBinding.stvCommonDialogMessageBtnTop.setStartColor(
                mTopButtonStartColor,
                mTopButtonEndColor
            )
        } else if (mTopButtonColor != 0) {
            mBinding.stvCommonDialogMessageBtnTop.solidColor = mTopButtonColor
        }
    }

//    private fun initSingleButton() {
//        mBinding.llCommonDialogMessageDoulbeBtnParent.visibility = View.GONE
//        mBinding.stvCommonDialogMessageBtnSingle.visibility = View.VISIBLE
//        mBinding.stvCommonDialogMessageBtnSingle.setOnClickListener(this)
//
//        if (!mSingleText.isNullOrEmpty()) {
//            mBinding.stvCommonDialogMessageBtnSingle.text = mSingleText
//        }
//    }

    /**
     * 设置Dialog标题
     */
    fun setTitle(title: CharSequence): TopDownButtonMessageDialog {
        mTitle = title
        return this
    }

    /**
     * 设置Dialog标题
     */
    fun setTitle(@StringRes title: Int): TopDownButtonMessageDialog {
        mTitle = ResUtils.getString(title)
        return this
    }

    /**
     * 设置Dialog内容
     */
    fun setContent(content: CharSequence): TopDownButtonMessageDialog {
        mContent = content
        return this
    }

    /**
     * 设置Dialog内容
     */
    fun setContent(@StringRes content: Int): TopDownButtonMessageDialog {
        mContent = ResUtils.getString(content)
        return this
    }

    /**
     * 设置右边按钮文字
     */
    fun setTopText(topText: CharSequence): TopDownButtonMessageDialog {
        mTopText = topText
        return this
    }

    /**
     * 设置右边按钮文字
     */
    fun setTopText(@StringRes topText: Int): TopDownButtonMessageDialog {
        mTopText = ResUtils.getString(topText)
        return this
    }

    /**
     * 设置左边按钮文字
     */
    fun setDownText(downText: CharSequence): TopDownButtonMessageDialog {
        mDownText = downText
        return this
    }

    /**
     * 设置左边按钮文字
     */
    fun setDownText(@StringRes downText: Int): TopDownButtonMessageDialog {
        mDownText = ResUtils.getString(downText)
        return this
    }

    /**
     * 是否Html文本
     * true Html文本
     */
    fun setIsHtml(isHtml: Boolean): TopDownButtonMessageDialog {
        mIsHtml = isHtml
        return this
    }


    /**
     * 设置是否可以关闭
     */
    fun setCancelAble(cancelAble: Boolean): TopDownButtonMessageDialog {
        mCancelAble = cancelAble
        return this
    }

    /**
     * 设置左边按钮颜色
     */
    fun setDownButtonColor(@ColorInt color: Int) {
        mDownButtonColor = color
    }

    /**
     * 设置左边按钮颜色，渐变色
     */
    fun setDownButtonColor(@ColorInt startColor: Int, @ColorInt endColor: Int) {
        mDownButtonStartColor = startColor
        mDownButtonEndColor = endColor
    }

    /**
     * 设置右边按钮颜色
     */
    fun setTopButtonColor(@ColorInt color: Int) {
        mTopButtonColor = color
    }

    /**
     * 设置右边按钮颜色，渐变色
     */
    fun setTopButtonColor(@ColorInt startColor: Int, @ColorInt endColor: Int) {
        mTopButtonStartColor = startColor
        mTopButtonEndColor = endColor
    }

    /**
     * 设置内容的Gravity
     */
    fun setContentGravity(gravity: Int) {
        mGravity = gravity
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.stv_common_dialog_message_btn_bottom -> {
                if (mOnButtonClickListener?.onDownClick() != true) {
                    dismiss()
                }
            }
            R.id.stv_common_dialog_message_btn_top -> {
                if (mOnButtonClickListener?.onTopClick() != true) {
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
     * 两个按钮的回调，在[onClick]方法中调用，
     */
    interface OnButtonClickListener {
        /**
         * @return true 将拦截默认处理事件，默认false
         */
        fun onTopClick(): Boolean

        /**
         * @return true 将拦截默认处理事件，默认false
         */
        fun onDownClick(): Boolean
    }

    override fun CommonDialogTopDownButtonMessageBinding.initView() {

        initTitle()

        initContent()

//        if (mIsSingleButton) {
//            initSingleButton()
//        } else {
        initDoubleButton()
//        }

    }

}