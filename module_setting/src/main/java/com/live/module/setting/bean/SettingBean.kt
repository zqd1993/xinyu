package com.live.module.setting.bean

/**
 * author: Tany
 * date: 2022/4/2
 * description:设置
 */
data class SettingBean(
    var title:String="",//左边文字
    var tip:String="",//括号内的提示文字
    var right:String="",//右边文字
    var isShowCheckBox:Boolean=false,//是否显示checkbox
    var isShowArrow:Boolean=true,//是否显示右边箭头
    var isSelect:Boolean=false

)