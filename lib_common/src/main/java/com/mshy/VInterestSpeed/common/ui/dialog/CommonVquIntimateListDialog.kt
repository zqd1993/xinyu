package com.mshy.VInterestSpeed.common.ui.dialog

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonIntimateListBean
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage
import com.mshy.VInterestSpeed.common.ui.adapter.CommonVquIntimateAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * author: Tany
 * date: 2022/6/28
 * description:
 */
class CommonVquIntimateListDialog : BaseDialogFragment(),
    View.OnClickListener {
    private var mIvClose: ImageView? = null
    private var ivRight: ImageView? = null
    private var ivLeft: ImageView? = null
    private var ivLevel: ImageView? = null
    private var tvIntimate: TextView? = null
    private var tvNext: TextView? = null
    private var rvIntimate: RecyclerView? = null
    var IMAGE_URL = "https://asset.whzhenban.top/"
    var globalApiService: GlobalApiService? = null
    var toId: String = ""

    override fun initView(view: View) {
        mIvClose = view.findViewById(R.id.iv_close)
        tvIntimate = view.findViewById(R.id.tv_intimate)
        tvNext = view.findViewById(R.id.tv_next)
        ivLeft = view.findViewById(R.id.iv_left)
        ivRight = view.findViewById(R.id.iv_right)
        ivLevel = view.findViewById(R.id.iv_level)
        rvIntimate = view.findViewById(R.id.rv_intimate)
        this.isCancelable = true
        mIvClose?.setOnClickListener(this)
        getData()
    }

    fun setId(id: String): CommonVquIntimateListDialog? {
        this.toId = id
        return this
    }

    private fun getData() {
        globalApiService =
            GlobalServiceManage.getRetrofit().create(GlobalApiService::class.java)
        globalApiService?.vquGetIntimateList(toId)
            ?.enqueue(object : Callback<BaseResponse<CommonIntimateListBean>> {
                override fun onResponse(
                    call: Call<BaseResponse<CommonIntimateListBean>>,
                    response: Response<BaseResponse<CommonIntimateListBean>>,
                ) {
                    if (response == null) {
                        return
                    }
                    if (response.isSuccessful) {
                        var currentData = response.body()?.data?.current
                        tvIntimate?.text = "亲密度：" + currentData?.score + "°C"
                        tvNext?.text=currentData?.des
                        ivLeft?.vquLoadCircleImage(currentData?.manUrl)
                        ivRight?.vquLoadCircleImage(currentData?.womanUrl)
                        ivLevel?.vquLoadImage(currentData?.iconUrl)
                        if(!response.body()?.data?.list.isNullOrEmpty()){
                            var myAdapter= CommonVquIntimateAdapter(response.body()?.data?.list!!)
                            rvIntimate?.adapter=myAdapter
                        }
                    }

                }

                override fun onFailure(
                    call: Call<BaseResponse<CommonIntimateListBean>>,
                    t: Throwable,
                ) {

                }
            })
    }


    override fun getLayoutId(): Int {
        return R.layout.common_vqu_dialog_intimate_list
    }

    override fun getWidth(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    override fun getHeight(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    override fun onClick(v: View) {
        if (v === mIvClose) {
            dismissAllowingStateLoss()
        }
    }
}