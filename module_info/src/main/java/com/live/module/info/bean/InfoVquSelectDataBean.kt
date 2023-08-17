package com.live.module.info.bean


data class InfoVquSelectDataBean(
    val annual_income: ArrayList<String>,
    val education: ArrayList<String>,
    val height: ArrayList<String>,
    val weight: ArrayList<String>,
    val is_marriage: ArrayList<String>,
    val occupation: ArrayList<Occupation>
)

data class Occupation(
    val key: String,
    val `val`: ArrayList<String>
)