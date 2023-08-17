package com.mshy.VInterestSpeed.common.converter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.charset.Charset

/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2021/12/14 11:03
 *
 */
class JsonRequestBodyConverter<T> constructor(gson : Gson, adapter : TypeAdapter<T>): Converter<T, RequestBody> {


        var gson: Gson = gson
        var adapter: TypeAdapter<T> = adapter

        val MEDIA_TYPE: MediaType = "application/json; charset=UTF-8".toMediaType()
        val UTF_8 = Charset.forName("UTF-8")


    override fun convert(value: T): RequestBody? {
        val buffer = Buffer()
        val writer: Writer =
            OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return RequestBody.create (MEDIA_TYPE, buffer.readByteString())
    }
}