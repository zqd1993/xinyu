package com.live.module.info.activity

import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import com.live.module.info.R
import com.live.module.info.databinding.InfoTantaActivityNicknameBinding
import com.live.module.info.vm.InfoEditViewModel
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseActivity

/**
 * author: Tany
 * date: 2022/4/20
 * description:修改昵称
 */
class InfoVquNicknameActivity : BaseActivity<InfoTantaActivityNicknameBinding, InfoEditViewModel>() {
    override val mViewModel: InfoEditViewModel by viewModels()
    var vquNickName: String? = ""
    override fun InfoTantaActivityNicknameBinding.initView() {
        mBinding.includeTitle.toolbar.initClose(
            "昵称",
            "保存",
            R.color.color_A3AABE,
            R.mipmap.ic_back,
            {
                finish()
            },
            {
                save()
            })
        vquNickName = intent.getStringExtra("nickName")
        mBinding.etNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                vquNickName = s.toString().trim()
                if (vquNickName.isNullOrEmpty()) {
                    mBinding.ivDel.gone()
//                    mBinding.includeTitle.toolbar.findViewById<TextView>(R.id.tv_right).setTextColor(
//                        ContextCompat.getColor(this@InfoVquNicknameActivity, R.color.color_A3AABE))
                } else {
                    mBinding.ivDel.visible()
//                    mBinding.includeTitle.toolbar.findViewById<TextView>(R.id.tv_right).setTextColor(
//                        ContextCompat.getColor(this@InfoVquNicknameActivity, R.color.color_7C69FE))
                }
            }

        })
        mBinding.etNickname.setText(vquNickName)
        mBinding.ivDel.setViewClickListener { mBinding.etNickname.setText("") }
    }

    private fun save() {
        if (vquNickName.isNullOrEmpty()) {
            resources.getString(R.string.info_nickname_empty).toast()
            return
        }
        if (vquNickName!!.length > 10) {
            resources.getString(R.string.info_nickname_length).toast()
            return
        }
        intent.putExtra("nickName", vquNickName)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun initObserve() {
    }

    override fun initRequestData() {
    }
}