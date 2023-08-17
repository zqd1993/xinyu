package com.mshy.VInterestSpeed.alipay

/**
 * author: Lau
 * date: 2022/4/18
 * description:
 */
class PayResult(private val rawResult: Map<String, String>?) {
    var resultStatus: String? = null
    var result: String? = null
    var memo: String? = null

    init {
        init()
    }

    private fun init() {
        if (rawResult.isNullOrEmpty()) {
            return
        }

        rawResult.forEach {
            when (it.key) {
                "resultStatus" -> resultStatus = it.value
                "result" -> result = it.value
                "memo" -> memo = it.value
            }
        }
    }

    override fun toString(): String {
        return ("resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}")
    }
}