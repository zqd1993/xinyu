package com.mshy.VInterestSpeed.common.ext

import android.view.View

/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/3/31 9:43
 *
 */

/**
 * 给所有view拓展的，防止重复点击
 */
var ViewClickTime = 0L
fun View.setViewClickListener(interval: Long = 200, click: () -> Unit) {
     setOnClickListener {
          val currentTime = System.currentTimeMillis()
          if (ViewClickTime != 0L && (currentTime - ViewClickTime < interval)) {
               return@setOnClickListener
          }
          ViewClickTime = currentTime
          click.invoke()
     }

}

/***
 * 防止快速点击
 */
var lastTime = 0L
fun View.click(listener: (view: View) -> Unit) {
     val minTime = 1000L
     this.setOnClickListener {
          val tmpTime = System.currentTimeMillis()
          if (tmpTime - lastTime > minTime) {
               lastTime = tmpTime
               listener.invoke(this)
          } else {
               "点击过快".toast()
          }
     }
}