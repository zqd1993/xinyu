package com.mshy.VInterestSpeed.common.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage

/**
 * author: Lau
 * date: 2022/4/13
 * description:
 */
class CommonVquAvatarFrameView(context: Context, val attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(
        context,
        attrs,
        defStyleAttr
    ) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    private val DEFAULT_AVATAR_SIZE = 144
    private var avatarSize = 0
    private var anchorVSize = 0
    private var avatarFrameSize = 0
    private var isAvatarFrameStandard = false
    private val ratio = 0.61f
    private lateinit var draweeView: ImageView
    private lateinit var ivAvatarFrame: ImageView

    //女神加V的图标
    private lateinit var ivAnchorV: ImageView


    init {
        val array: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CommonVquAvatarFrameView)

        avatarSize =
            array.getDimensionPixelSize(R.styleable.CommonVquAvatarFrameView_avatarSize, 0)
        anchorVSize =
            array.getDimensionPixelSize(R.styleable.CommonVquAvatarFrameView_anchorVSize, 0)
        avatarFrameSize =
            array.getDimensionPixelSize(R.styleable.CommonVquAvatarFrameView_avatarFrameSize, 0)
        isAvatarFrameStandard =
            array.getBoolean(R.styleable.CommonVquAvatarFrameView_isAvatarFrameStandard, false)

        array.recycle()
//
        initView()
    }

    private fun initView() {
        draweeView = ImageView(context)
        if (isAvatarFrameStandard) {
            avatarSize = (avatarFrameSize * ratio).toInt()
        } else {
            avatarFrameSize = (avatarSize / ratio).toInt()
        }
        val params = LayoutParams(avatarSize, avatarSize)
        params.topToTop = LayoutParams.PARENT_ID
        params.bottomToBottom = LayoutParams.PARENT_ID
        params.startToStart = LayoutParams.PARENT_ID
        params.endToEnd = LayoutParams.PARENT_ID
        draweeView.id = R.id.common_vqu_avatar_view
        draweeView.layoutParams = params
        addView(draweeView)

        ivAvatarFrame = ImageView(context)
        val avatarFrameParams = LayoutParams(avatarFrameSize, avatarFrameSize)
        avatarFrameParams.topToTop = LayoutParams.PARENT_ID
        avatarFrameParams.bottomToBottom = LayoutParams.PARENT_ID
        avatarFrameParams.startToStart = LayoutParams.PARENT_ID
        avatarFrameParams.endToEnd = LayoutParams.PARENT_ID
        ivAvatarFrame.layoutParams = avatarFrameParams
        ivAvatarFrame.visibility = GONE
        addView(ivAvatarFrame)

        if (anchorVSize != 0) {
            ivAnchorV = ImageView(context)
            val ivAnchorVParams = LayoutParams(anchorVSize, anchorVSize)
            ivAnchorVParams.rightMargin = (avatarFrameSize * 0.05).toInt()
            ivAnchorVParams.bottomMargin = (avatarFrameSize * 0.05).toInt()
            ivAnchorVParams.bottomToBottom = R.id.common_vqu_avatar_view
            ivAnchorVParams.endToEnd = R.id.common_vqu_avatar_view
            ivAnchorV.layoutParams = ivAnchorVParams
            ivAnchorV.setBackgroundResource(R.mipmap.ic_common_vqu_anchor_v)
            ivAnchorV.visibility = GONE
            addView(ivAnchorV)
        }
    }

    fun showAnchorView(isShow: Boolean) {
//        ivAnchorV.visibility = if (isShow) VISIBLE else GONE
    }

    /**
     * 这个应该是头像框
     *
     * @param avatarFrameUrl
     */
    fun loadAvatarFrame(avatarFrameUrl: String?, hasConstUrl: Boolean = false) {
//        if (!avatarFrameUrl.isNullOrEmpty()) {
//            if (hasConstUrl) {
//                ivAvatarFrame.vquLoadImage(avatarFrameUrl)
//            } else {
//                ivAvatarFrame.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + avatarFrameUrl)
//            }
//            ivAvatarFrame.visibility = VISIBLE
//        } else {
//            ivAvatarFrame.visibility = INVISIBLE
//        }
    }

    fun loadAvatar(avatarUrl: String?, hasConstUrl: Boolean = false) {
        if (hasConstUrl) {
            draweeView.vquLoadCircleImage(
                avatarUrl,
                R.mipmap.ic_common_head_def
            )
        } else {
            draweeView.vquLoadCircleImage(
                NetBaseUrlConstant.IMAGE_URL + avatarUrl,
                R.mipmap.ic_common_head_def
            )
        }
    }
}