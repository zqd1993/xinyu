package com.mshy.VInterestSpeed.common.utils;

import android.text.SpannableStringBuilder;
import android.text.Spanned;

/**
 * author: Tany
 * date: 2022/8/1
 * description:
 */
public class MyTextSpan {

    public SpannableStringBuilder ssb;

    public MyTextSpan(){
        this(new SpannableStringBuilder());
    }

    public MyTextSpan(SpannableStringBuilder ssb){
        this.ssb = ssb;
    }

    public MyTextSpan append(String str,Object... objects){
        if (str==null || str.length()==0){
            return this;
        }
        if (objects == null || objects.length ==0){
            ssb.append(str);
            return this;
        }
        int begin = ssb.length();//添加str前的位置
        ssb.append(str);
        int end = ssb.length();//添加str后的位置
        //对添加各个功能（字体大小、颜色等等）在跨度范围生成效果
        for (int i =0;i<objects.length;i++){
            ssb.setSpan(objects[i],begin,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }
}

