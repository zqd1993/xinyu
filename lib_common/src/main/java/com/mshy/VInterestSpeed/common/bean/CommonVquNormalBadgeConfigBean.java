package com.mshy.VInterestSpeed.common.bean;

import java.io.Serializable;

/**
 * FileName: com.live.vquonline.model.live
 * Author: Reisen
 * Date: 2021/9/2 10:52
 * Description:普通徽章相关配置bean
 * History:
 */
public class CommonVquNormalBadgeConfigBean implements Serializable {

    /**
     * id : 1
     * name : 骑士徽章
     * file : /v1.3/dress/badge/%E8%B4%B5%E6%97%8F%E5%8B%8B%E7%AB%A0_%E9%AA%91%E5%A3%AB.png
     * type : badge
     * width : 46
     */

    private int id;
    private String name;
    private String file;
    private String type;
    private String width;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
