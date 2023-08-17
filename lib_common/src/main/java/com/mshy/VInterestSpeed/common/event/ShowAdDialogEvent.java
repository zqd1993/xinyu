package com.mshy.VInterestSpeed.common.event;


import com.mshy.VInterestSpeed.common.bean.CommonVquAdBean;

/**
 * FileName: com.live.vquonline.controller.eventBus
 * Author: Reisen
 * Date: 2021/9/16 16:04
 * Description: 显示广告弹窗
 * History:
 */
public class ShowAdDialogEvent {

    private CommonVquAdBean.ListBean result;

    public ShowAdDialogEvent(CommonVquAdBean.ListBean result) {
        this.result = result;
    }

    public CommonVquAdBean.ListBean getResult() {
        return result;
    }

    public void setResult(CommonVquAdBean.ListBean result) {
        this.result = result;
    }
}
