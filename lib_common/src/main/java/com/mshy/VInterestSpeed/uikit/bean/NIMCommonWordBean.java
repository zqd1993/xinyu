package com.mshy.VInterestSpeed.uikit.bean;

import java.io.Serializable;

/**
 * FileName: com.live.vquonline.model.news
 * Author: Reisen
 * Date: 2022/3/8 15:29
 * Description:
 * History:
 */
public class NIMCommonWordBean implements Serializable {

    /**
     * id : 1
     * word : 大公司是
     * gender : 1
     * status : 1
     * create_time_text :
     * update_time_text :
     */

    private int id;
    private String word;
    private int gender;
    private int status;
    private String create_time_text;
    private String update_time_text;

    private int is_system;
    private String user_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreate_time_text() {
        return create_time_text;
    }

    public void setCreate_time_text(String create_time_text) {
        this.create_time_text = create_time_text;
    }

    public String getUpdate_time_text() {
        return update_time_text;
    }

    public void setUpdate_time_text(String update_time_text) {
        this.update_time_text = update_time_text;
    }

    public int getIs_system() {
        return is_system;
    }

    public void setIs_system(int is_system) {
        this.is_system = is_system;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
