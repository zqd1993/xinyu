package com.live.module.mine.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.view.Gravity
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.live.module.mine.databinding.MineDialogSignDaySuccessBinding
import com.live.module.mine.vm.MineSignDayViewModel
import com.mshy.VInterestSpeed.common.bean.SignDaySuccessData
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseVMDialogFragment
import com.mshy.VInterestSpeed.common.utils.FontsFamily
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/6/23
 * description:
 */
@AndroidEntryPoint
class SignDaySuccessDialog :
    BaseVMDialogFragment<MineDialogSignDaySuccessBinding, MineSignDayViewModel>() {

    override val mViewModel: MineSignDayViewModel by viewModels()


    private var mData: SignDaySuccessData? = null


    override fun MineDialogSignDaySuccessBinding.initView() {

//        AssetManager mgr=getAssets();//得到AssetManager
//        Typeface tf=Typeface.createFromAsset(mgr, "fonts/ttf.ttf");//根据路径得到Typeface
//        tv=findViewById(R.id.textview);
//        tv.setTypeface(tf);//设置字体



        mBinding.tvMineDialogSignDayAward.typeface = FontsFamily.tfDDinExpBold

        mBinding.tvMineDialogSignDayAdd.typeface = FontsFamily.tfDDinExpBold

        val data = mData

        if (data != null) {
//            mBinding.ivMineDialogSignDaySuccessIcon.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + data.img)
            mBinding.tvMineDialogSignDayAward.text = data.title


            val count = "已连续签到 <font color=\'#FF8859\'>${data.number}</font> 天奖励"

            val fromHtml = Html.fromHtml(count)

            mBinding.stvMineDialogSignDaySignCount.text = fromHtml
        }


        mBinding.ivMineDialogSignDayClose.setViewClickListener { dismiss() }

        mBinding.stvMineDialogSignDaySubmit.setViewClickListener {
            dismiss()
        }
    }

    override fun initObserve() {

    }

    override fun initRequestData() {
    }

    fun setData(data: SignDaySuccessData?) {
        mData = data
    }

    override fun initWindow() {
//        super.initWindow()

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