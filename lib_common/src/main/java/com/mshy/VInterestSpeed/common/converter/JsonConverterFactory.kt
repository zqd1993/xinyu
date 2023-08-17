package com.mshy.VInterestSpeed.common.converter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/**
 *
 * @Description: 自定义json转换器
 * @Author: theobald wen
 * @CreateDate: 2021/12/14 10:59
 *
 */
class JsonConverterFactory constructor(var gson: Gson) : Converter.Factory() {



    fun create(gson: Gson?): JsonConverterFactory? {
        if (gson == null) throw NullPointerException("gson == null")
        return JsonConverterFactory(gson)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return JsonRequestBodyConverter(gson, adapter)
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return JsonResponseBodyConverter(gson, adapter) //响应
    }
}