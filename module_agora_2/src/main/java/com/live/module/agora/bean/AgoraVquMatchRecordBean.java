package com.live.module.agora.bean;

import java.io.Serializable;
import java.util.List;


public class AgoraVquMatchRecordBean implements Serializable {
    public List<LogBean> getLog() {
        return log;
    }

    public void setLog(List<LogBean> log) {
        this.log = log;
    }

    private List<LogBean> log;



    public static class LogBean implements Serializable {
        /**
         * from_username : 王者的琴涛
         * to_username : 已认证女号
         */

        private String from_username;
        private String to_username;

        public String getFrom_username() {
            return from_username;
        }

        public void setFrom_username(String from_username) {
            this.from_username = from_username;
        }

        public String getTo_username() {
            return to_username;
        }

        public void setTo_username(String to_username) {
            this.to_username = to_username;
        }
    }
}
