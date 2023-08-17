package com.live.module.info.activity

import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import com.live.module.info.R
import com.live.module.info.databinding.InfoTantaActivityIntroduceBinding
import com.live.module.info.vm.InfoEditViewModel
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ui.BaseActivity

/**
 * author: Tany
 * date: 2022/4/24
 * description:自我介绍
 */
class InfoVquIntroduceActivity :
    BaseActivity<InfoTantaActivityIntroduceBinding, InfoEditViewModel>() {

    var vquSign: String = ""
    override val mViewModel: InfoEditViewModel by viewModels()

    override fun InfoTantaActivityIntroduceBinding.initView() {
        mBinding.includeTitle.toolbar.initClose(
            "自我介绍",
            "保存",
            R.color.color_A3AABE,
            R.mipmap.ic_back,
            {
                finish()
            },
            {
                intent.putExtra("sign", vquSign)
                setResult(RESULT_OK, intent)
                finish()
            })
        initEdit()
    }

    private fun initEdit() {
        vquSign = intent.getStringExtra("sign").toString()
        mBinding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                vquSign = s.toString().trim()
                mBinding.tvHint.text = vquSign?.length.toString() + "/100"
//                if (vquSign.isNullOrEmpty()) {
//                    mBinding.includeTitle.toolbar.findViewById<TextView>(R.id.tv_right).setTextColor(
//                        ContextCompat.getColor(this@InfoVquIntroduceActivity, R.color.color_A3AABE))
//                } else {
//                    mBinding.includeTitle.toolbar.findViewById<TextView>(R.id.tv_right).setTextColor(
//                        ContextCompat.getColor(this@InfoVquIntroduceActivity, R.color.color_7C69FE))
//                }
            }

        })
        mBinding.etContent.setText(vquSign)

    }

    override fun initObserve() {
    }

    override fun initRequestData() {
    }
}