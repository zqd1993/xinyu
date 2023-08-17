package com.mshy.VInterestSpeed.common.converter

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonToken
import com.live.vquonline.base.utils.AESUtils
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.StringReader

/**
 *
 * @Description: 自定义响应ResponseBody
 * @Author: theobald wen
 * @CreateDate: 2021/12/14 11:05
 *
 */
class JsonResponseBodyConverter<T> constructor(gson: Gson, adapter: TypeAdapter<T>) :
    Converter<ResponseBody, T> {

    var gson: Gson = gson
    var adapter: TypeAdapter<T> = adapter

    override fun convert(value: ResponseBody): T? {


        //var strResult = response.substring(1, response.length - 1)
        var result = AESUtils.decrypt("!caicai20180315!", value.string())
        val jsonReader = gson.newJsonReader(StringReader(result))
        // val jsonReader = gson.newJsonReader(value.charStream())
        return try {
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed.")
            }
            result
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            value.close()
        }
    }
}