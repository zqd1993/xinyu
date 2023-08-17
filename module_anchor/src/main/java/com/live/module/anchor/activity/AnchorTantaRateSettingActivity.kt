package com.live.module.anchor.activity

import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.live.module.anchor.R
import com.live.module.anchor.bean.AnchorTantaSettingBean
import com.live.module.anchor.databinding.AnchorVquActivityRateSettingBinding
import com.live.module.anchor.vm.AnchorTantaRateSettingViewModel
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.html
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.utils.ResUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import dagger.hilt.android.AndroidEntryPoint


/**
 * author: Lau
 * date: 2022/4/24
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Anchor.AnchorTantaRateSettingActivity)
class AnchorTantaRateSettingActivity :
    BaseActivity<AnchorVquActivityRateSettingBinding, AnchorTantaRateSettingViewModel>() {
    override val mViewModel: AnchorTantaRateSettingViewModel by viewModels()

    private var mVquStarGrade = 0
    private var mSkillId = ""

    private lateinit var mVquChatPicker: OptionsPickerView<AnchorTantaSettingBean.Selection>
    private lateinit var mVquVideoPicker: OptionsPickerView<AnchorTantaSettingBean.Selection>
    private lateinit var mVquVoicePicker: OptionsPickerView<AnchorTantaSettingBean.Selection>

    private val mVideoPriceList = mutableListOf<AnchorTantaSettingBean.Selection>()
    private val mChatPriceList = mutableListOf<AnchorTantaSettingBean.Selection>()
    private val mVoicePriceList = mutableListOf<AnchorTantaSettingBean.Selection>()

    override fun AnchorVquActivityRateSettingBinding.initView() {

        mBinding.tbAnchorVquRateSettingBar.toolbar.initClose(getString(R.string.contact_vqu_setting_rate)) { finish() }

        initExplain()

        initChatPicker()

        initVideoPicker()

        initVoicePicker()

        initEvent()
    }

    private fun initVoicePicker() {
        mVquVoicePicker = OptionsPickerBuilder(this) { options1, _, _, _ ->
            mVquVoicePicker.dismiss()
            if (UserManager.userInfo?.gender == 1) {
                mViewModel.parsingAndSetPrice(mSkillId, mVoicePriceList, options1, "1")
            } else {
                mViewModel.parsingAndSetPrice(mSkillId, mVoicePriceList, options1, "voice_price")
            }
        }.isDialog(true)
            .setLayoutRes(R.layout.anchor_vqu_picker_rates) {
                it.findViewById<Button>(R.id.btnSubmit)
                    .setViewClickListener {
                        mVquVoicePicker.returnData()

                    }
                it.findViewById<Button>(R.id.btnCancel)
                    .setViewClickListener {
                        mVquVoicePicker.dismiss()
                    }
            }
            .setDividerColor(ResUtils.getColor(android.R.color.transparent))
            .build()


        initLayoutParams(mVquVoicePicker)
    }

    private fun initEvent() {
        mBinding.clAnchorVquRateSettingChatPriceParent.setViewClickListener {
            mVquChatPicker.show()
        }

        mBinding.clAnchorVquRateSettingVideoPriceParent.setViewClickListener {
            mVquVideoPicker.show()
        }

        mBinding.clAnchorVquRateSettingVoicePriceParent.setViewClickListener {
            mVquVoicePicker.show()
        }

        mBinding.cbAnchorVquRateSettingVideoSwitch.setViewClickListener {
            mViewModel.vquSetVideoStatus(if (mBinding.cbAnchorVquRateSettingVideoSwitch.isChecked) 1 else 0)
        }
    }

    private fun initVideoPicker() {
        mVquVideoPicker = OptionsPickerBuilder(this) { options1, _, _, _ ->
            mVquVideoPicker.dismiss()
            if (UserManager.userInfo?.gender == 1) {
                mViewModel.parsingAndSetPrice(mSkillId, mVideoPriceList, options1, "2")
            } else {
                mViewModel.parsingAndSetPrice(mSkillId, mVideoPriceList, options1, "video_price")
            }
        }.isDialog(true)
            .setLayoutRes(R.layout.anchor_vqu_picker_rates) {
                it.findViewById<Button>(R.id.btnSubmit)
                    .setViewClickListener {
                        mVquVideoPicker.returnData()

                    }

                it.findViewById<Button>(R.id.btnCancel)
                    .setViewClickListener {
                        mVquVideoPicker.dismiss()
                    }
            }
            .setDividerColor(ResUtils.getColor(android.R.color.transparent))
            .build()


        initLayoutParams(mVquVideoPicker)
    }

    private fun initChatPicker() {
        mVquChatPicker = OptionsPickerBuilder(this) { options1, _, _, _ ->
            mVquChatPicker.dismiss()
            if (UserManager.userInfo?.gender == 1) {
                mViewModel.parsingAndSetPrice(mSkillId, mChatPriceList, options1, "5")
            } else {
                mViewModel.parsingAndSetPrice(mSkillId, mChatPriceList, options1, "msg_price")
            }
        }.isDialog(true)
            .setLayoutRes(R.layout.anchor_vqu_picker_rates) {
                it.findViewById<Button>(R.id.btnSubmit)
                    .setViewClickListener {
                        mVquChatPicker.returnData()
                    }

                it.findViewById<Button>(R.id.btnCancel)
                    .setViewClickListener {
                        mVquChatPicker.dismiss()
                    }
            }
            .setDividerColor(ResUtils.getColor(android.R.color.transparent))
            .build()

        initLayoutParams(mVquChatPicker)
    }

    private fun initLayoutParams(pickerView: OptionsPickerView<AnchorTantaSettingBean.Selection>) {
        val layoutParams: FrameLayout.LayoutParams =
            pickerView.dialogContainerLayout.layoutParams as FrameLayout.LayoutParams
        layoutParams.leftMargin = 0
        layoutParams.rightMargin = 0
        pickerView.dialogContainerLayout.layoutParams = layoutParams

        val parentLayout: FrameLayout =
            pickerView.dialogContainerLayout.parent as FrameLayout
        val parentParams: FrameLayout.LayoutParams =
            parentLayout.layoutParams as FrameLayout.LayoutParams
        parentLayout.setBackgroundResource(R.drawable.shape_white_top_8_bg)
        parentParams.leftMargin = 0
        parentParams.rightMargin = 0
        parentParams.bottomMargin = 0
        parentParams.width = WindowManager.LayoutParams.MATCH_PARENT

        val window: Window? = pickerView.dialog.window
        if (window != null) {
            val attributes: WindowManager.LayoutParams = window.attributes
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = attributes
            window.setWindowAnimations(R.style.picker_view_slide_anim)
            window.attributes.gravity = Gravity.BOTTOM
        }
    }

    private fun initExplain() {
        val explain = getString(
            R.string.goddess_vqu_rates_setting_explain,
            UserManager.webUrl?.anchorStarlevel ?: ""
        )

        mBinding.tvSkillSettingRatesExplain.html(explain)

    }

    override fun initObserve() {

        mViewModel.skillIdData.observe(this) {
            mSkillId = it
        }

        mViewModel.settingSelectionData.observe(this) {
            mVquStarGrade = it.starGrade

            if (!it.chat.isNullOrEmpty()) {
                mChatPriceList.addAll(it.chat)
            }
            mVquChatPicker.setPicker(mChatPriceList)

            if (!it.video.isNullOrEmpty()) {
                mVideoPriceList.addAll(it.video)
            }
            mVquVideoPicker.setPicker(mVideoPriceList)

            if (!it.voice.isNullOrEmpty()) {
                mVoicePriceList.addAll(it.voice)
            }
            mVquVoicePicker.setPicker(mVoicePriceList)

        }

        mViewModel.priceData.observe(this) {
            mBinding.tvAnchorVquRateSettingChatPrice.text = it.msgPrice
            mBinding.tvAnchorVquRateSettingVideoPrice.text = it.videoPrice
            mBinding.tvAnchorVquRateSettingVoicePrice.text = it.voicePrice
//            mBinding.cbAnchorVquRateSettingVideoSwitch.isChecked = it.openVideoStatus == 1
        }
    }

    override fun initRequestData() {
        mViewModel.vquSkillSetting()
        mViewModel.tantaGetAnchorSetting("")
        mViewModel.tantaGetPrice()
    }
}