package com.mshy.VInterestSpeed.common.ui.dialog

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.aliyun.qupaiokhttp.FileDownloadCallback
import com.aliyun.qupaiokhttp.HttpRequest
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.SettingVquVersionBean
import com.mshy.VInterestSpeed.common.databinding.CommonVquDialogUpdateBinding
import com.mshy.VInterestSpeed.common.ext.toast
import java.io.File
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import com.mshy.VInterestSpeed.common.utils.PermissionUtils

/**
 * author: Tany
 * date: 2022/5/2
 * description:
 */
class CommonUpdateDialog : BaseDialogFragment<CommonVquDialogUpdateBinding>(),
    View.OnClickListener {
    val DOWNLOAD_PATH: String = "/aitang/cache/"
    var vquVersionBean: SettingVquVersionBean? = null
    var myActivity: AppCompatActivity? = null
    var isDownload: Boolean = false
    var downLoadFile: File? = null
    override fun CommonVquDialogUpdateBinding.initView() {
        tvVersion.text = "V" + vquVersionBean?.newversion
        tvContent.text = vquVersionBean?.upgradetext
        myActivity = activity as AppCompatActivity
        isCancelable = false
        mCancelAble=false
        if (vquVersionBean?.enforce == 1) {//强制更新
            tvAfter.gone()
        }
        tvAfter.setOnClickListener(this@CommonUpdateDialog)
        tvUpdate.setOnClickListener(this@CommonUpdateDialog)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_after -> {
                dismiss()
            }
            R.id.tv_update -> {
                if (isDownload) {
                    install(downLoadFile!!)
                } else {
                    PermissionUtils.storageUpdatePermission(
                        myActivity!!,
                        requestCallback = { allGranted, grantedList, deniedList ->
                            if (allGranted) {
                                if(vquVersionBean!=null){
                                    update(vquVersionBean!!.downloadurl)
                                }
                            } else {
                                toast("无法获取存权限,无法更新")
                            }
                        })

                }
            }

        }
    }

    private fun update(downLoadUrl: String) {
        val file = File(myActivity?.externalCacheDir.toString()+DOWNLOAD_PATH)
        if (file.isFile) {
            file.delete()
        }

        if (!file.exists()) {
            file.mkdirs()
        }
        downLoadFile = File(myActivity?.externalCacheDir.toString()+DOWNLOAD_PATH + System.currentTimeMillis() + "temp.apk")
        HttpRequest.download(downLoadUrl,
            downLoadFile,
            object : FileDownloadCallback() {
                override fun onFailure() {
                    super.onFailure()
                    "下载失败,请重新下载".toast()
                }

                override fun onProgress(progress: Int, networkSpeed: Long) {
                    super.onProgress(progress, networkSpeed)
                    mBinding.tvLoading.visible()
                    mBinding.progressbar.progress = progress
                }

                override fun onStart() {
                    super.onStart()
                    mBinding.tvUpdate.gone()
                    mBinding.rlProgressbar.visible()
                    mBinding.tvAfter.text="关闭弹窗，后台下载"
                }

                override fun onDone() {
                    super.onDone()
                    "下载成功".toast()
                    isDownload=true
                    mBinding.tvUpdate.visible()
                    mBinding.tvUpdate.text="立即安裝"
                    mBinding.rlProgressbar.gone()
                    install(downLoadFile!!)
                }
            })
    }

    private fun install(downLoadFile: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val contentUri: Uri = FileProvider.getUriForFile(
                BaseApplication.context,
                BaseApplication.context.applicationInfo.packageName.toString() + ".fileProvider",
                downLoadFile)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(downLoadFile),
                "application/vnd.android.package-archive")
        }
        myActivity?.startActivity(intent)
    }

    fun setData(data: SettingVquVersionBean) {
        vquVersionBean = data
    }
}