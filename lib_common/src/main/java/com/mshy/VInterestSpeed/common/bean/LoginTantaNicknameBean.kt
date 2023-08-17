package com.live.module.login.bean

/**
 * author: Lau
 * date: 2022/4/8
 * description:
 */
data class LoginTantaNicknameBean(
    val female: Gender,
    val male: Gender,
    val male_default: Gender,
    val female_default: Gender
)

data class Gender(
    val avatar: List<String>,
    val nickname: List<Nickname>
)

data class Nickname(
    val key: String,
    val name: String
)
