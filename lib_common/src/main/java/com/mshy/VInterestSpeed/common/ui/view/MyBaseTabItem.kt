package com.mshy.VInterestSpeed.common.ui.view

import android.content.Context
import com.mshy.VInterestSpeed.common.R
import me.majiajie.pagerbottomtabstrip.item.NormalItemView
import org.greenrobot.eventbus.EventBus

/**
 * author: Tany
 * date: 2022/5/26
 * description:
 */
class MyBaseTabItem(context: Context?) : NormalItemView(context) {

    override fun onRepeat() {
        if (this.title == context.getString(R.string.common_vqu_main_dynamic)) {
            EventBus.getDefault().post("onRepeat")
        } else if (this.title == context.getString(R.string.common_vqu_main_fate)) {
            EventBus.getDefault().post("onHomeRepeat")
        }
        super.onRepeat()

    }
}