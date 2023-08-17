package com.mshy.VInterestSpeed.common.helper;

import com.mshy.VInterestSpeed.common.bean.CommonVquWebUrlBean;


/**
 * FileName: com.live.vquonline.utils
 * Author: Reisen
 * Date: 2022/3/24 15:50
 * Description:
 * History:
 */
public class CommonVquWebUrlHelper {

    private CommonVquWebUrlBean webUrl;


    private CommonVquWebUrlHelper() {

    }

    public static CommonVquWebUrlHelper getInstance() {
        return InstanceHolder.instance;
    }

    static class InstanceHolder {
        final static CommonVquWebUrlHelper instance = new CommonVquWebUrlHelper();
    }

    /**
     * 保存URL地址
     *
     * @param webUrl
     */
    public CommonVquWebUrlHelper saveUrl(CommonVquWebUrlBean webUrl) {
        this.webUrl = webUrl;
        return this;
    }

    public CommonVquWebUrlBean getWebUrl() {
        return webUrl == null ? new CommonVquWebUrlBean() : webUrl;
    }


}
