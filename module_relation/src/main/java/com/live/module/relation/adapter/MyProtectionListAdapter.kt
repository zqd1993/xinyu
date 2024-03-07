package com.live.module.relation.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.relation.R
import com.live.module.relation.bean.MyProtectionListBean
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.ui.view.CommonVquAvatarFrameView

class MyProtectionListAdapter :
    BaseQuickAdapter<MyProtectionListBean, BaseViewHolder>(R.layout.my_protection_list_adapter) {
    override fun convert(holder: BaseViewHolder, item: MyProtectionListBean) {
        val gender = if (item.gender == 1) "女" else "男"
        val height = if (item.height.isEmpty()) "" else "/${item.height}cm"
        val occupation = if (item.occupation.isEmpty()) "" else "/${item.occupation}"
        val head = holder.getView<CommonVquAvatarFrameView>(R.id.siv_avatar)
        val protectionLevelIm = holder.getView<ImageView>(R.id.protection_level_im)
        holder.setText(R.id.tv_nick_name, item.nickname)
        holder.setText(R.id.tv_relation_desc, "$gender/${item.age}岁$height$occupation")
        head.loadAvatar(item.avatar)
        if (item.guardianOptionsSnapshot != null) {
            if (item.guardianOptionsSnapshot.icon.isNotEmpty())
                protectionLevelIm.vquLoadImage(item.guardianOptionsSnapshot.icon)
        }

    }
}