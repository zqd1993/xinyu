package com.live.module.info.activity

import android.app.Activity
import android.graphics.Color
import android.view.View
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.live.module.info.R
import com.live.module.info.bean.InfoUserTagBean
import com.live.module.info.bean.Tag
import com.live.module.info.databinding.InfoTantaActivityLabelBinding
import com.live.module.info.vm.InfoEditViewModel
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.utils.UiUtils
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoVquLabelEditActivity : BaseActivity<InfoTantaActivityLabelBinding, InfoEditViewModel>() {
    var userTag = ArrayList<Tag>()
    var bottomAdapter: TagAdapter<Tag>? = null

    fun notifyHint() {
//        if (userTag.size == 0) {
//            mBinding.tvTip.visible()
//        } else {
//            mBinding.tvTip.gone()
//        }
    }


    private fun initTagFlowLayout(result: InfoUserTagBean?) {
        if (userTag.isNullOrEmpty()) {
            userTag = result?.user_tag!!
        }
        notifyHint()
        var bottomTag = result?.tag_list
        var topAdapter = object : TagAdapter<Tag>(userTag) {
            override fun getView(parent: FlowLayout, position: Int, bean: Tag): View {
                val view = layoutInflater.inflate(
                    R.layout.info_tanta_item_label_del,
                    mBinding.tagBottom, false
                ) as ConstraintLayout
                var tv =
                    view.findViewById<com.mshy.VInterestSpeed.common.ui.view.ShapeTextView>(R.id.tv_tag)
                tv.text = bean.name
                var colorList = bean.color.split(",")
                tv.solidColor = Color.parseColor("#" + colorList[0])
                tv.setTextColor(Color.parseColor("#" + colorList[1]))
                return view
            }

            override fun onSelected(position: Int, view: View) {
                super.onSelected(position, view)
                for (x in 0 until bottomTag!!.size) {
                    if (userTag[position].id == bottomTag[x].id) {
                        bottomTag[x].isSelect = false
                    }
                }
                bottomAdapter?.notifyDataChanged()
                userTag.remove(userTag[position])
                notifyDataChanged()
                notifyHint()
            }

            override fun unSelected(position: Int, view: View) {
                super.unSelected(position, view)
            }
        }
        mBinding.tagTop.adapter = topAdapter

        for (x in 0 until bottomTag!!.size) {
            for (y in 0 until userTag.size) {
                if (bottomTag[x].id == userTag[y].id) {
                    bottomTag[x].isSelect = true
                    break
                }
            }
        }

        bottomAdapter = object : TagAdapter<Tag>(bottomTag) {
            override fun getView(parent: FlowLayout, position: Int, bean: Tag): View {
                val view = layoutInflater.inflate(
                    R.layout.info_tanta_item_label,
                    mBinding.tagBottom, false
                ) as ConstraintLayout
                var tv =
                    view.findViewById<com.mshy.VInterestSpeed.common.ui.view.ShapeTextView>(R.id.tv_tag)
                tv.text = bean.name
                var colorList = bean.color.split(",")
                if (bean.isSelect) {
                    tv.solidColor = Color.parseColor("#ffffff")
                    tv.setTextColor(Color.parseColor("#000000"))
                    tv.setStrokeWidth(UiUtils.dip2px(applicationContext, 1f))
                    tv.setStrokeColor(Color.parseColor("#B4A3FD"))
                } else {
                    tv.solidColor = Color.parseColor("#F5F5F5")
                    tv.setTextColor(Color.parseColor("#000000"))
                    tv.setStrokeColor(Color.parseColor("#00000000"))
                    tv.setStrokeWidth(0)

                }

                return view
            }

            override fun onSelected(position: Int, view: View) {
                super.onSelected(position, view)

                var pos = -1
                for (index in 0 until userTag.size) {
                    if (bottomTag!![position].id == userTag[index].id) {
                        pos = index
                        break
                    }
                }
                if (pos == -1) {
                    if (userTag.size == 8) {
                        "最多选择8个标签".toast()
                    } else {
                        userTag.add(bottomTag!![position])
                        bottomTag!![position].isSelect = true
                    }

                } else {
                    userTag.remove(userTag[pos])
                    bottomTag!![position].isSelect = false
                }
                topAdapter.notifyDataChanged()
                bottomAdapter?.notifyDataChanged()
                notifyHint()

            }

            override fun unSelected(position: Int, view: View) {
                super.unSelected(position, view)
                var pos = -1
                for (index in 0 until userTag.size) {
                    if (bottomTag!![position].id == userTag[index].id) {
                        pos = index
                        break
                    }
                }
                if (pos == -1) {
                    if (userTag.size == 8) {
                        "最多选择8个标签".toast()
                    } else {
                        userTag.add(bottomTag!![position])
                        bottomTag[position].isSelect = true
                    }
                } else {
                    userTag.remove(userTag[pos])
                    bottomTag[position].isSelect = false
                }
                bottomAdapter?.notifyDataChanged()
                topAdapter.notifyDataChanged()
                notifyHint()
            }
        }
        mBinding.tagBottom.adapter = bottomAdapter

    }

    override val mViewModel: InfoEditViewModel by viewModels()

    override fun InfoTantaActivityLabelBinding.initView() {
        mBinding.includeTitle.toolbar.initClose(
            "我的标签",
            "保存",
            R.color.color_A3AABE,
            R.mipmap.ic_back,
            {
                finish()
            },
            {
                intent.putExtra("userTag", userTag)
                setResult(Activity.RESULT_OK, intent)
                finish()
            })
        var tag = intent.getSerializableExtra("userTag")
        if (tag != null) {
            userTag = tag as ArrayList<Tag>
        }

    }

    override fun initObserve() {
        mViewModel.vquGetLabelData.observe(this, Observer {
            initTagFlowLayout(it.data)
        })
    }

    override fun initRequestData() {
        mViewModel.vquGetUserTag()

    }

}