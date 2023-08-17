package com.mshy.VInterestSpeed.common.bean;



import java.io.Serializable;
import java.util.List;

public class CommonVquGlobalConfigBean implements Serializable {
    private CommonVquConfigBean config;
    private CommonVquVersionBean version;

    private CommonVquWebUrlBean webUrl;

    private String rank_url;
    private String rank_url_full;
    private DressBean dress;

    public CommonVquWebUrlBean getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(CommonVquWebUrlBean webUrl) {
        this.webUrl = webUrl;
    }

    public DressBean getDress() {
        return dress;
    }

    public void setDress(DressBean dress) {
        this.dress = dress;
    }

    public CommonVquConfigBean getConfig() {
        return config;
    }

    public void setConfig(CommonVquConfigBean config) {
        this.config = config;
    }

    public CommonVquVersionBean getVersion() {
        return version;
    }

    public void setVersion(CommonVquVersionBean version) {
        this.version = version;
    }

    public String getRank_url() {
        return rank_url;
    }

    public void setRank_url(String rank_url) {
        this.rank_url = rank_url;
    }

    public String getRank_url_full() {
        return rank_url_full;
    }

    public void setRank_url_full(String rank_url_full) {
        this.rank_url_full = rank_url_full;
    }

    public static class DressBean implements Serializable {
        private List<BubbleBean> bubble;

        public List<BubbleBean> getBubble() {
            return bubble;
        }

        public void setBubble(List<BubbleBean> bubble) {
            this.bubble = bubble;
        }

        public static class BubbleBean implements Serializable {
            /**
             * bubble : /v1.3/dress/bubble/lALPD3zUOqg7eJA7bg_110_59.png
             */
            private String bubble;

            public String getBubble() {
                return bubble;
            }

            public void setBubble(String bubble) {
                this.bubble = bubble;
            }
        }
    }


}
