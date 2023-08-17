package com.mshy.VInterestSpeed.common.bean.gift;


public class GiftTypeBean {
    private int type;

    private DialogGiftBean dialogGiftBean;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DialogGiftBean getDialogGiftBean() {
        return dialogGiftBean;
    }

    public void setDialogGiftBean(DialogGiftBean dialogGiftBean) {
        this.dialogGiftBean = dialogGiftBean;
    }

    public GiftTypeBean(int type, DialogGiftBean dialogGiftBean) {
        this.type = type;
        this.dialogGiftBean = dialogGiftBean;
    }
}
