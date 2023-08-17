package com.mshy.VInterestSpeed.common.bean;

import java.io.Serializable;

/**
 * FileName: com.live.vquonline.model.common
 * Author: Reisen
 * Date: 2021/12/22 14:27
 * Description: h5弹窗的实体类
 * History:
 */
public class H5UrlBean implements Serializable {

    /**
     * blindDateChange : {"url":"https://asset.vqu.show/h5/#/playflow?token=EIcTuSJ8YdTr4zgTW5WAkafMa8Jb1czcu_PcMDq2ZQo","width":"280","height":"411"}
     * palyIntroduce : {"url":"","width":"280","height":"411"}
     */

    private BlindDateChangeBean blindDateChange;
    private PalyIntroduceBean palyIntroduce;
    //实际使用时的宽高 需手动根据对象不同传参
    private int width;
    private int height;
    //显示位置 center 居中 left 靠左 right 靠右 top置顶  bottom 置底  full全屏
    private String gravity = "center";

    public String getGravity() {
        return gravity;
    }

    public void setGravity(String gravity) {
        this.gravity = gravity;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BlindDateChangeBean getBlindDateChange() {
        return blindDateChange;
    }

    public void setBlindDateChange(BlindDateChangeBean blindDateChange) {
        this.blindDateChange = blindDateChange;
    }

    public PalyIntroduceBean getPalyIntroduce() {
        return palyIntroduce;
    }

    public void setPalyIntroduce(PalyIntroduceBean palyIntroduce) {
        this.palyIntroduce = palyIntroduce;
    }

    public static class BlindDateChangeBean implements Serializable {
        /**
         * url : https://asset.vqu.show/h5/#/playflow?token=EIcTuSJ8YdTr4zgTW5WAkafMa8Jb1czcu_PcMDq2ZQo
         * width : 280
         * height : 411
         */

        private String url;
        private int width;
        private int height;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    public static class PalyIntroduceBean implements Serializable {
        /**
         * url :
         * width : 280
         * height : 411
         */

        private String url;
        private int width;
        private int height;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
