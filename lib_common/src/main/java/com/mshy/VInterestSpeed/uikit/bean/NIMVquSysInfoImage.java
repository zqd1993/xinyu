package com.mshy.VInterestSpeed.uikit.bean;

public class NIMVquSysInfoImage {
    private String image;
    private String title;
    private int link_type;
    private String link_url;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public String toString() {
        return "SysInfoImage{" +
                "image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", link_type=" + link_type +
                ", link_url='" + link_url + '\'' +
                '}';
    }
}
