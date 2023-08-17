package com.mshy.VInterestSpeed.common.db

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import com.live.vquonline.base.BaseApplication
import com.mshy.VInterestSpeed.common.bean.City
import java.io.*

/**
 * author: Lau
 * date: 2022/4/11
 * description:
 */
class CityManager() {
    private var DB_PATH: String = ""
    private val DB_NAME_V1 = "china_cities_v2.db"
    private val DB_NAME_V2 = "china_cities_v3.db"
    private val LATEST_DB_NAME = DB_NAME_V2
    private val BUFFER_SIZE = 1024*4

    private val TABLE_NAME = "cities"

    private val COLUMN_C_NAME = "c_name"
    private val COLUMN_C_PROVINCE = "c_province"
    private val COLUMN_C_PINYIN = "c_pinyin"
    private val COLUMN_C_CODE = "c_code"


    init {
        DB_PATH = (File.separator.toString() + "data"
                + Environment.getDataDirectory().absolutePath + File.separator
                + BaseApplication.application.packageName + File.separator + "databases" + File.separator)
        copyDBFile()
    }

    private fun copyDBFile() {
        val dir = File(DB_PATH)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        //如果旧版数据库存在，则删除
        val dbV1 = File(DB_PATH + DB_NAME_V1)
        if (dbV1.exists()) {
            dbV1.delete()
        }

        val dbFile = File(DB_PATH + LATEST_DB_NAME)
        //创建新版本数据库
        if (!dbFile.exists()) {
            val `is`: InputStream
            val os: OutputStream
            try {
                `is` = BaseApplication.application.resources.assets.open(LATEST_DB_NAME)

                os = FileOutputStream(dbFile)
                val buffer = ByteArray(BUFFER_SIZE)
                var length: Int
                while (`is`.read(buffer, 0, buffer.size).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
                os.close()
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    fun getAllCities(): MutableList<City> {
        val result: MutableList<City> = ArrayList()
        try {
            val db: SQLiteDatabase =
                SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null)
            val cursor: Cursor = db.rawQuery("select * from $TABLE_NAME", null)
            var city: City
            while (cursor.moveToNext()) {
                val nameIndex = cursor.getColumnIndex(COLUMN_C_NAME)
                val name: String = cursor.getString(nameIndex)
                val provinceIndex = cursor.getColumnIndex(COLUMN_C_PROVINCE)
                val province: String = cursor.getString(provinceIndex)
                val pinyinIndex = cursor.getColumnIndex(COLUMN_C_PINYIN)
                val pinyin: String = cursor.getString(pinyinIndex)
                val codeIndex = cursor.getColumnIndex(COLUMN_C_CODE)
                val code: String = cursor.getString(codeIndex)
                city = City(name, province, pinyin, code)
                result.add(city)
            }
            cursor.close()
            db.close()
            result.sortBy {
                it.pinyin.substring(0, 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun searchCity(keyword: String): MutableList<City> {
        val result: MutableList<City> = ArrayList()
        try {
            val sql = ("select * from " + TABLE_NAME + " where "
                    + COLUMN_C_NAME + " like ? " + "or "
                    + COLUMN_C_PINYIN + " like ? ")
            val db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null)
            val cursor = db.rawQuery(
                sql, arrayOf(
                    "%$keyword%",
                    "$keyword%"
                )
            )
            while (cursor.moveToNext()) {
                val nameIndex = cursor.getColumnIndex(COLUMN_C_NAME)
                val name: String = cursor.getString(nameIndex)
                val provinceIndex = cursor.getColumnIndex(COLUMN_C_PROVINCE)
                val province: String = cursor.getString(provinceIndex)
                val pinyinIndex = cursor.getColumnIndex(COLUMN_C_PINYIN)
                val pinyin: String = cursor.getString(pinyinIndex)
                val codeIndex = cursor.getColumnIndex(COLUMN_C_CODE)
                val code: String = cursor.getString(codeIndex)
                val city = City(name, province, pinyin, code)
                result.add(city)
            }
            cursor.close()
            db.close()
            result.sortBy {
                it.pinyin.substring(0, 1)
            }
        } catch (e: java.lang.Exception) {
        }
        return result
    }


}