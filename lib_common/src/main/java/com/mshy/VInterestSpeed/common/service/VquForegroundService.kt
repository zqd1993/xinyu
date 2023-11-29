package com.mshy.VInterestSpeed.common.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.utils.AgoraUtils
import com.mshy.VInterestSpeed.common.utils.UserManager


/**
 * author: Lau
 * date: 2022/7/18
 * description:
 */
class VquForegroundService : Service() {

    companion object {
        //通知ID
        private const val NOTIFICATION_ID = 1745

        //唯一的通知通道的ID
        private const val notificationChannelId = "notification_channel_id_01"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        UserManager.isStartForegroundService = true
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            startForegroundWithNotification()
//        }

    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundWithNotification()
        }
    }


    /**
     * 开启前台服务并发送通知
     */
    private fun startForegroundWithNotification() {
        //8.0及以上注册通知渠道
        createNotificationChannel()
        val notification: Notification = createForegroundNotification()
        //将服务置于启动状态 ,NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIFICATION_ID, notification)
        //发送通知到状态栏
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    /**
     * 创建通知渠道
     */
    private fun createNotificationChannel() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //Android8.0以上的系统，新建消息通道
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //用户可见的通道名称
            val channelName = "Foreground Service Notification"
            //通道的重要程度
            val importance: Int = NotificationManager.IMPORTANCE_LOW
            //构建通知渠道
            val notificationChannel = NotificationChannel(
                notificationChannelId,
                channelName, importance
            )
            notificationChannel.description = "Channel description"
            //LED灯
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            //震动
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    /**
     * 创建服务通知
     */
    private fun createForegroundNotification(): Notification {
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, notificationChannelId)
        //通知小图标
        builder.setSmallIcon(R.mipmap.ic_launcher)
        //通知标题
        builder.setContentTitle("甜缘APP")
        //通知内容
        builder.setContentText("正在视频/音频通话中...")
        //设置通知显示的时间
        builder.setWhen(System.currentTimeMillis())
        //设定启动的内容

        if (AgoraUtils.contactType == AgoraUtils.CONTACT_TYPE_VIDEO) {
            val activityIntent = Intent(this, Class.forName("com.live.module.agora.activity.AgoraTantaVideo2Activity"))
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                this,
                1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder.setContentIntent(pendingIntent)
        } else if (AgoraUtils.contactType == AgoraUtils.CONTACT_TYPE_AUDIO) {
            val activityIntent = Intent(this, Class.forName("com.live.module.agora.activity.AgoraTantaAudio2Activity"))
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                this,
                1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder.setContentIntent(pendingIntent)
        }



//        val activityIntent: Intent = Intent(this, MainActivity::class.java)
//        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(
//            this,
//            1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        builder.setContentIntent(pendingIntent)
        //设置通知优先级
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        //设置为进行中的通知
        builder.setOngoing(true)

        //创建通知并返回
        return builder.build()

    }

    //client 可以通过Binder获取Service实例
    inner class MyBinder : Binder() {
        val service: VquForegroundService
            get() = this@VquForegroundService
    }


    override fun onDestroy() {
        stopForeground(true)
        UserManager.isStartForegroundService = false
        super.onDestroy()
    }

}