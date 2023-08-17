package com.mshy.VInterestSpeed.common.ui.dialog

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.live.vquonline.base.ktx.clickDelay
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.databinding.CommonDialogPosterBinding
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.umeng.socialize.bean.SHARE_MEDIA
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import com.mshy.VInterestSpeed.common.utils.PermissionUtils


/**
 * author: Lau
 * date: 2022/5/6
 * description:
 */
class CommonVquPosterDialog : BaseDialogFragment<CommonDialogPosterBinding>() {

    private var mVquPosterBean: com.mshy.VInterestSpeed.common.bean.PosterBean? = null

    private var mVquQrCodeBitmap: Bitmap? = null

    override fun CommonDialogPosterBinding.initView() {
        mBinding.raivCommonPosterImg.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + mVquPosterBean?.posterPicUrl)
        createQrCode()

        mBinding.llCommonPosterWx.clickDelay {
            if (com.mshy.VInterestSpeed.common.utils.AppInstallUtil.isWxInstalled()) {
                share(SHARE_MEDIA.WEIXIN_CIRCLE)
            } else {
                "请安装微信".toast()
            }
        }

        mBinding.llCommonPosterWechat.clickDelay {
            if (com.mshy.VInterestSpeed.common.utils.AppInstallUtil.isWxInstalled()) {
                share(SHARE_MEDIA.WEIXIN)
            } else {
                "请安装微信".toast()
            }
        }
        mBinding.llCommonPosterQq.clickDelay {
            if (com.mshy.VInterestSpeed.common.utils.AppInstallUtil.isQqInstalled()) {
                share(SHARE_MEDIA.QQ)
            } else {
                "请安装QQ".toast()
            }
        }
        mBinding.llCommonPosterSave.clickDelay {

            saveImage()
        }
        mBinding.ivClose.clickDelay {
            dismiss()
        }

        mBinding.rlCommonPosterRoot.clickDelay {
            dismiss()
        }
    }

    private fun saveImage() {
        PermissionUtils.storagePermission(
            this,
            requestCallback = { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    SaveImage().execute()
                } else {
                    toast("无法获取存储图片相关权限")
                }
            })

    }

    override fun initWindow() {
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.CENTER
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.setCanceledOnTouchOutside(mCancelAble)
        dialog?.setCancelable(mCancelAble)
    }

    private fun share(shareMedia: SHARE_MEDIA) {
        val bitmap: Bitmap? =
            com.mshy.VInterestSpeed.common.utils.BitMapUtil.ImageCompress(getViewToBitmap(mBinding.srlCommonPosterImgParent))
        if (bitmap != null) {
            com.mshy.VInterestSpeed.common.utils.ShareManager.getInstance().sharePicToPlatform(shareMedia, bitmap, requireActivity())
        } else {
            toast("图片生成失败")
        }
    }

    /**
     * 获取一个 View 的缓存视图 * (前提是这个View已经渲染完成显示在页面上) * @param view * @return
     */
    private fun getViewToBitmap(view: View): Bitmap? {
        //创建Bitmap,最后一个参数代表图片的质量.
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //创建Canvas，并传入Bitmap.
        val canvas = Canvas(bitmap)
        //View把内容绘制到canvas上，同时保存在bitmap.
        view.draw(canvas)
        return bitmap
    }



    private fun createQrCode() {
        mVquQrCodeBitmap = com.mshy.VInterestSpeed.common.utils.QRCodeUtil.createQRCodeBitmap(
            mVquPosterBean?.getqCodeContent() ?: "二维码内容",
            dp2px(65f),
            dp2px(65f),
            "UTF-8",
            "Q", "1", Color.BLACK, Color.WHITE, null,
            0f, null
        )

        mBinding.ivCommonPosterQrCode.setImageBitmap(mVquQrCodeBitmap)

    }

    fun setData(data: com.mshy.VInterestSpeed.common.bean.PosterBean) {
        mVquPosterBean = data
    }

    override fun onDestroy() {
        mVquQrCodeBitmap?.recycle()
        mVquQrCodeBitmap = null
        super.onDestroy()
    }


    /***
     * 功能：用线程保存图片
     *
     * @author wangyp
     */
    private open inner class SaveImage : AsyncTask<Boolean, Void?, Boolean>() {
        override fun doInBackground(vararg params: Boolean?): Boolean {
            var result = false
            val bitmap: Bitmap? = getViewToBitmap(mBinding.srlCommonPosterImgParent)
            if (bitmap != null) {
                result =
                    saveImageToGallery(getViewToBitmap(mBinding.srlCommonPosterImgParent))
            }
            return result
        }

        override fun onPostExecute(result: Boolean?) {
            if (result == true) {
                toast("二维码保存成功")
//                MaleToast.showMessage(getActivity(), "二维码保存成功")
            } else {
                toast("二维码保存失败")
            }
            dismissAllowingStateLoss()
        }

    }

    fun saveImageToGallery(bmp: Bitmap?): Boolean { // 首先保存图片

        if (bmp == null) {
            return false
        }

        val storePath: String =
            com.mshy.VInterestSpeed.common.utils.FilePathUtils.getFilesPath() + File.separator + System.currentTimeMillis()
                .toString()
        val appDir = File(storePath)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            //通过io流的方式来压缩保存图片
            val isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos)
            fos.flush()
            fos.close()
            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(
                requireActivity().contentResolver,
                file.absolutePath,
                fileName,
                null
            )
            //保存图片后发送广播通知更新数据库
            val uri: Uri = Uri.fromFile(file)
            requireActivity().sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            return isSuccess
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    private fun createBitmapThumbnail(bitMap: Bitmap, newWidth: Float, newHeight: Float): Bitmap {
        var width = bitMap.width
        var height = bitMap.height


        val scaleWidth: Float = newWidth / width
        val scaleHeight: Float = newHeight / height

        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        val newBitmap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true)
        return newBitmap
    }
}