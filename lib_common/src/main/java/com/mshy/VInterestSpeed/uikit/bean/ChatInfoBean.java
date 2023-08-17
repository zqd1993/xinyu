package com.mshy.VInterestSpeed.uikit.bean;

import java.io.Serializable;
import java.util.List;

/**
 * FileName: com.live.vquonline.model.news
 * Author: Reisen
 * Date: 2022/3/23 10:59
 * Description:
 * History:
 */
public class ChatInfoBean implements Serializable {

    /**
     * score : 0
     * auth : {"real_auth_status":1,"real_auth_msg":"已完成真人认证","auth_status":1,"auth_msg":"已完成实名认证"}
     * city : 深圳
     * base_info : 27·programmer·176cm·天秤座
     * albumList : [{"id":74,"url":"/v1.3/test/9fadbf10f0b34ae4688bf5e32831dffd7971134c.png"},{"id":75,"url":"/v1.3/test/part-00174-1860%20-%20%E5%89%AF%E6%9C%AC.jpg"}]
     */
    private int user_id;
    private String score;
    private AuthBean auth;
    private String city;
    private String login_ip_addr;//地址
    private String base_info;
    private List<AlbumListBean> albumList;

    public String getLogin_ip_addr() {
        return login_ip_addr;
    }

    public void setLogin_ip_addr(String login_ip_addr) {
        this.login_ip_addr = login_ip_addr;
    }

    public static class AuthBean implements Serializable {
        /**
         * real_auth_status : 1
         * real_auth_msg : 已完成真人认证
         * auth_status : 1
         * auth_msg : 已完成实名认证
         */

        private int real_auth_status;
        private String real_auth_msg;
        private int auth_status;
        private String auth_msg;

        public int getReal_auth_status() {
            return real_auth_status;
        }

        public void setReal_auth_status(int real_auth_status) {
            this.real_auth_status = real_auth_status;
        }

        public String getReal_auth_msg() {
            return real_auth_msg;
        }

        public void setReal_auth_msg(String real_auth_msg) {
            this.real_auth_msg = real_auth_msg;
        }

        public int getAuth_status() {
            return auth_status;
        }

        public void setAuth_status(int auth_status) {
            this.auth_status = auth_status;
        }

        public String getAuth_msg() {
            return auth_msg;
        }

        public void setAuth_msg(String auth_msg) {
            this.auth_msg = auth_msg;
        }
    }

    public static class AlbumListBean implements Serializable {
        /**
         * id : 74
         * url : /v1.3/test/9fadbf10f0b34ae4688bf5e32831dffd7971134c.png
         */

        private int id;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public AuthBean getAuth() {
        return auth;
    }

    public void setAuth(AuthBean auth) {
        this.auth = auth;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBase_info() {
        return base_info;
    }

    public void setBase_info(String base_info) {
        this.base_info = base_info;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<AlbumListBean> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<AlbumListBean> albumList) {
        this.albumList = albumList;
    }
}
