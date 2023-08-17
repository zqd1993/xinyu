package com.mshy.VInterestSpeed.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * FileName: com.live.vquonline.model.main
 * Author: Reisen
 * Date: 2021/9/10 14:21
 * Description:
 * History:
 */
public class CommonVquAdBean implements Serializable {

    /**
     * list : [{"title":"首充活动","image":"images/advert/bfaea29cb8a092d63cd99bb466042e62.png","link_type":1,"link_url":"www.baidu.com"}]
     * is_open : 1
     */

    private int is_open;
    private List<ListBean> list;

    public static class ListBean implements Serializable {
        /**
         * title : 首充活动
         * image : images/advert/bfaea29cb8a092d63cd99bb466042e62.png
         * link_type : 1
         * link_url : www.baidu.com
         */

        private String title;
        private String image;
        private int link_type;
        private String link_url;
        /**
         * width : 0
         * height : 0
         * window_type : 1
         * window_link :
         */

        private int width;
        private int height;
        private int window_type;
        private String gravity;
        private String window_link;
        private int delayed_time;


        public int getDelayed_time() {
            return delayed_time;
        }

        public void setDelayed_time(int delayed_time) {
            this.delayed_time = delayed_time;
        }

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

        public int getWindow_type() {
            return window_type;
        }

        public void setWindow_type(int window_type) {
            this.window_type = window_type;
        }

        public String getWindow_link() {
            return window_link;
        }

        public void setWindow_link(String window_link) {
            this.window_link = window_link;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getLink_type() {
            return link_type;
        }

        public void setLink_type(int link_type) {
            this.link_type = link_type;
        }

        public String getLink_url() {
            return link_url;
        }

        public void setLink_url(String link_url) {
            this.link_url = link_url;
        }
    }

    public int getIs_open() {
        return is_open;
    }

    public void setIs_open(int is_open) {
        this.is_open = is_open;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }
}
