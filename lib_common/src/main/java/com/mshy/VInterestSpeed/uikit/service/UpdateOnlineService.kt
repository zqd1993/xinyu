package com.mshy.VInterestSpeed.uikit.service

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import java.lang.ref.WeakReference

/**
 * author: Tany
 * date: 2022/5/11
 * description:
 */
class UpdateOnlineService : Service() {
    private val CHANNEL_ID = "OnlineService"
    private val CHANNEL_NAME = "UpdateOnlineService"

    private var myHandler: MyHandler? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread { //准备并创建当前线程的Looper对象
            Looper.prepare()
            myHandler = MyHandler(this@UpdateOnlineService, Looper.myLooper())
            //发送消息开启任务
            myHandler!!.sendEmptyMessage(1)
            //开启消息循环
            Looper.loop()
        }.start()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateOnlineStatus() {
//        //先判断是否有网络  没有网络返回并且10S后重新访问
//        if (!NetworkUtil.isNetAvailable(this)) {
//            myHandler?.sendEmptyMessageDelayed(1, 10000)
//            return
//        }
//        try {
//            var globalApiService =
//                GlobalServiceManage.getRetrofit().create(GlobalApiService::class.java)
//            globalApiService?.vquOnlineUpdate("")
//                ?.enqueue(object : Callback<BaseResponse<UpdateOnlineBean>> {
//                    override fun onResponse(
//                        call: Call<BaseResponse<UpdateOnlineBean>>,
//                        response: Response<BaseResponse<UpdateOnlineBean>>,
//                    ) {
//                        if(response.body()?.code==0){
//                            val onlineOpen: Int = response.body()?.data!!.user_online_open
//                            //	轮训间隔时间（秒）
//                            //	轮训间隔时间（秒）
//                            val onlineTime: Int = response.body()?.data!!.user_online_time
//                            if(onlineOpen==null||onlineTime==null){
//                                return
//                            }
//                            if (onlineOpen == 1) {
//                                myHandler?.sendEmptyMessageDelayed(1, (onlineTime * 1000).toLong())
//                            } else {
//                                //关闭当前服务
//                                stopSelf()
//                            }
//                        }
//                    }
//
//                    override fun onFailure(
//                        call: Call<BaseResponse<UpdateOnlineBean>>,
//                        t: Throwable,
//                    ) {
//                    }
//                })
//
//
//        } catch (e: Exception) {
//            Log.e("UpdateOnlineService", "updateOnlineStatus: " + e.message)
//        }
    }

    override fun onDestroy() {
        if (myHandler != null) {
            //销毁时清空消息队列  防止内存泄漏
            myHandler!!.removeCallbacksAndMessages(null)
            myHandler = null
        }
        super.onDestroy()
        Log.e("result", "onDestroy: ")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
//        var channel: NotificationChannel? = null
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            channel =
//                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
//            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            manager.createNotificationChannel(channel)
//            val notification = Notification.Builder(getApplicationContext(), CHANNEL_ID).build()
//            startForeground(1, notification)
//        }
    }

    /**
     * 创建子线程的handler对象  这里构造方法添加一个looper对象
     * 使其handleMessage方法回调在子线程中
     */
    private class MyHandler(updateOnlineService: UpdateOnlineService, looper: Looper?) :
        Handler(looper!!) {
        private val weak: WeakReference<UpdateOnlineService>
        override fun handleMessage(msg: Message) {
            val updateOnlineService = weak.get()
            if (msg.what == 1) {
                updateOnlineService?.updateOnlineStatus()
            }
        }

        init {
            weak = WeakReference(updateOnlineService)
        }
    }
}