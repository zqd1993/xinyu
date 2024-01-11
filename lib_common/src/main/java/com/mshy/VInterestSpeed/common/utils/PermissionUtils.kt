package com.mshy.VInterestSpeed.common.utils

import android.Manifest
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.RequestCallback
import com.permissionx.guolindev.request.PermissionBuilder

/**
 * author: Lau
 * date: 2022/4/9
 * description:
 */
object PermissionUtils {

    private val locationPermission = arrayListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

    private val storagePermission = arrayListOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val cameraPermission = arrayListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val videoPermission = arrayListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,

    )

    private val voicePermission = arrayListOf(
        Manifest.permission.RECORD_AUDIO,

    )

    /**
     * 权限请求，杂七杂八的权限
     */
    fun permission(
        fragment: Fragment,
        permissions: MutableList<String>,
        reason: String? = null,
        forward: String? = null,
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {

        if (permissions.isNotEmpty()) {
            for (permission in permissions) {
                if (!PermissionX.isGranted(fragment.requireContext(), permission)) {
                    val dialog = MessageDialog()
                    dialog.setTitle("权限申请通知")
                    dialog.setContent(reason ?: "")
                    dialog.setCancelAble(false)
                    dialog.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
                        override fun onLeftClick(): Boolean {
                            return dismissCallback?.invoke() ?: false
                        }

                        override fun onRightClick(): Boolean {
//                            dialog.dismiss()

//                            val currentTimeMillis = System.currentTimeMillis()
//                            val handler = Handler()
//                            handler.postDelayed({
                            val permissionx =
                                PermissionX.init(fragment).permissions(permissions)

//                            shouldShowReasonOrForwardDialog(permissionx, reason, forward)

                            permissionx.request(requestCallback)
//                            }, 1000)
//
//                            val diff = System.currentTimeMillis() - currentTimeMillis
//                            Log.d("aaaa", "onRightClick: $diff")

                            return false
                        }

                    })
                    dialog.show(fragment.childFragmentManager, "")

                    return
                }
            }

            val permissionx =
                PermissionX.init(fragment).permissions(permissions)

//            shouldShowReasonOrForwardDialog(permissionx, reason, forward)

            permissionx.request(requestCallback)
        }

    }

    /**
     * 权限请求，杂七杂八的权限
     */
    fun permission(
        activity: FragmentActivity,
        permissions: MutableList<String>,
        reason: String? = null,
        forward: String? = null,
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {

        var count = 0
        if (permissions.isNotEmpty()) {
            for (permission in permissions) {
                count++
                if (!PermissionX.isGranted(activity, permission)) {
                    val dialog = MessageDialog()
                    dialog.setTitle("权限申请通知")
                    dialog.setContent(reason ?: "")
                    dialog.setCancelAble(false)
                    dialog.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
                        override fun onLeftClick(): Boolean {
                            return dismissCallback?.invoke() ?: false
                        }

                        override fun onRightClick(): Boolean {

                            val permissionx =
                                PermissionX.init(activity).permissions(permissions)

//                            shouldShowReasonOrForwardDialog(permissionx, reason, forward)

                            permissionx.request(requestCallback)


                            return false
                        }

                    })
                    dialog.show(activity.supportFragmentManager, "")

                    return
                }
            }

//            if (count == permissions.size - 1) {
            val permissionx =
                PermissionX.init(activity).permissions(permissions)

//            shouldShowReasonOrForwardDialog(permissionx, reason, forward)

            permissionx.request(requestCallback)
//            }
        }

    }

    /**
     * 权限请求，相机权限
     */
    fun cameraPermission(
        fragment: Fragment,
        reason: String? = "用于发布图片及视频等内容展示",
        forward: String? = "用于发布图片及视频等内容展示",
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {

        permission(fragment, cameraPermission, reason, forward, requestCallback, dismissCallback)

    }

    /**
     * 权限请求，相机权限
     */
    fun cameraPermission(
        activity: FragmentActivity,
        reason: String? = "用于发布图片及视频等内容展示",
        forward: String? = "用于发布图片及视频等内容展示",
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {

        permission(activity, cameraPermission, reason, forward, requestCallback, dismissCallback)

    }

    /**
     * 权限请求，内存权限
     */
    fun storagePermission(
        fragment: Fragment,
        reason: String? = "允许App读取/写入/存储中的图片、文件等内容，主要用于帮助您发布信息，在本地记录崩溃日志信息（如有）等功能",
        forward: String? = "允许App读取/写入/存储中的图片、文件等内容，主要用于帮助您发布信息，在本地记录崩溃日志信息（如有）等功能",
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {

        permission(fragment, storagePermission, reason, forward, requestCallback, dismissCallback)

    }
    /**
     * 权限请求，内存权限/更新
     */
    fun storageUpdatePermission(
        fragment: FragmentActivity,
        reason: String? = "允许App读取/写入/存储文件等内容，主要用于下载新版本安装包",
        forward: String? = "允许App读取/写入/存储文件等内容，主要用于下载新版本安装包",
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {

        permission(fragment, storagePermission, reason, forward, requestCallback, dismissCallback)

    }

    /**
     * 权限请求，内存权限
     */
    fun storagePermission(
        activity: FragmentActivity,
        reason: String? = "允许App读取/写入/存储中的图片、文件等内容，主要用于帮助您发布信息，在本地记录崩溃日志信息（如有）等功能",
        forward: String? = "允许App读取/写入/存储中的图片、文件等内容，主要用于帮助您发布信息，在本地记录崩溃日志信息（如有）等功能",
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {

        permission(activity, storagePermission, reason, forward, requestCallback, dismissCallback)

    }

    /**
     * 权限请求，定位权限
     */
    fun location(
        fragment: Fragment,
        reason: String? = "用于完善基本城市信息以及基于地理位置的服务（LBS）等相关功能",
        forward: String? = "用于完善基本城市信息以及基于地理位置的服务（LBS）等相关功能",
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {

        permission(fragment, locationPermission, reason, forward, requestCallback, dismissCallback)

    }

    /**
     * 权限请求，定位权限
     */
    fun location(
        activity: FragmentActivity,
        reason: String? = "用于完善基本城市信息以及基于地理位置的服务（LBS）等相关功能",
        forward: String? = "用于完善基本城市信息以及基于地理位置的服务（LBS）等相关功能",
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {
        permission(activity, locationPermission, reason, forward, requestCallback, dismissCallback)

    }

    private fun shouldShowReasonOrForwardDialog(
        permissions: PermissionBuilder,
        reason: String? = null,
        forward: String? = null,
    ) {

        if (!reason.isNullOrEmpty()) {
            permissions.onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, reason, "我明白了", "取消")
            }
        }

        if (!forward.isNullOrEmpty()) {
            permissions.onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, forward, "我已明白", "取消")
            }
        }
    }

    private val recordPermission = arrayListOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    /**
     * 权限请求，录音权限
     */
    fun record(
        fragment: Fragment,
        reason: String? = "用于帮助您完成音视频信息发布等需要使用该权限的相关功能；",
        forward: String? = "用于帮助您完成音视频信息发布等需要使用该权限的相关功能；",
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {

        permission(fragment, recordPermission, reason, forward, requestCallback, dismissCallback)


    }

    /**
     * 权限请求，录音权限
     */
    fun record(
        activity: FragmentActivity,
        reason: String? = "要使用麦克风录音权限，以正常使用上传录音、发送语音、音视频聊天等功能。",
        forward: String? = "要使用麦克风录音权限，以正常使用上传录音、发送语音、音视频聊天等功能。",
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {

        permission(activity, recordPermission, reason, forward, requestCallback, dismissCallback)

    }

    /**
     * 权限请求，视频拨打
     */
    fun videoPermission(
        activity: Fragment,
        reason: String? = null,
        forward: String? = null,
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {

        permission(activity, videoPermission, reason, forward, requestCallback, dismissCallback)

    }

    /**
     * 权限请求，视频拨打
     */
    fun videoPermission(
        fragment: FragmentActivity,
        reason: String? = null,
        forward: String? = null,
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {

        permission(fragment, videoPermission, reason, forward, requestCallback, dismissCallback)
    }

    fun vquVoicePermission(
        fragment: FragmentActivity,
        reason: String? = null,
        forward: String? = null,
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {
        permission(fragment, voicePermission, reason, forward, requestCallback, dismissCallback)
    }

    fun vquVoicePermission(
        fragment: Fragment,
        reason: String? = null,
        forward: String? = null,
        requestCallback: RequestCallback,
        dismissCallback: (() -> Boolean)? = null
    ) {
        permission(fragment, voicePermission, reason, forward, requestCallback, dismissCallback)
    }
}