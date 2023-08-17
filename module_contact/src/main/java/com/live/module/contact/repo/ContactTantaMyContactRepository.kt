package com.live.module.contact.repo

import com.live.module.contact.bean.ContactVquInvitedListBean
import com.live.module.contact.bean.ContactVquOverviewData
import com.live.module.contact.net.ContactVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/22
 * description:
 */
class ContactTantaMyContactRepository @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mVquApi: ContactVquApiService

    suspend fun vquOverview(afterKey: Int, beginTime: String, endTime: String, sortType: String) =
        request<BaseResponse<ContactVquOverviewData>> {
            mVquApi.vquIncome(afterKey, beginTime, endTime, sortType).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    fun vquExcel2email(email: String, beginTime: String, endTime: String) =
        request<BaseResponse<Any>> {
            mVquApi.vquExcel2email(email, beginTime, endTime).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquInviteList(page: Int, keyword: String) =
        request<BaseResponse<ContactVquInvitedListBean>> {
            mVquApi.vquInviteList(page, keyword).run {
                responseCodeExceptionHandler(code, message,false) {
                    emit(this)
                }
            }
        }
}