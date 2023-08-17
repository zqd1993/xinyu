package com.mshy.VInterestSpeed.common.helper

/**
 * 请求响应code枚举抽象
 *
 * ""
 * @since 2021/7/9 2:56 下午
 */
interface IResponseCode {

    /**
     * 获取该枚举的code码
     * @return Int
     */
    fun getCode(): Int

    /**
     * 获取该枚举的描述
     * @return String
     */
    fun getMessage(): String
}

/**
 * 请求响应code的枚举
 *
 * ""
 * @since 2021/7/9 2:55 下午
 */
enum class ResponseCodeEnum : IResponseCode {

    // 通用异常
    ERROR {
        override fun getCode() = 1
        override fun getMessage() = "处理失败"
    },

    // 成功
    SUCCESS {
        override fun getCode() = 0
        override fun getMessage() = "成功"
    },


    NOT_LOGIN {
        override fun getCode(): Int = 1001
        override fun getMessage(): String = "未登录"
    },
    NOT_ENOUGH_MONEY {
        override fun getCode(): Int = 1002
        override fun getMessage(): String = "余额不足"
    },
    NOT_ENOUGH_COIN {
        override fun getCode(): Int = 1003
        override fun getMessage(): String = "余额不足"
    },
    NOT_SUPER_MEMBER {
        override fun getCode(): Int = 1005
        override fun getMessage(): String = "不是会员"
    },
    COMPARE_AVATAR_FAILURE {
        override fun getCode(): Int = 1006
        override fun getMessage(): String = "压缩头像错误"
    },
    NOT_AUTH {
        override fun getCode(): Int = 1007
        override fun getMessage(): String = "未认证"
    },

    NO_NETWORK {
        override fun getCode(): Int = -1
        override fun getMessage(): String = "网络不可用"
    }

}