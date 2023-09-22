package com.live.module.dynamic.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;

import com.live.module.dynamic.R;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.UIUtil;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;


/**
 * FileName: com.live.vquonline.view.base.utils
 * Author: Reisen
 * Date: 2022/2/28 10:58
 * Description:
 * History:
 */
public class MagicIndicatorHelper {

    public static SimplePagerTitleView getDefaultTitleView(Context context, String text) {
        SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
        simplePagerTitleView.setText(text);
        simplePagerTitleView.setTextSize(21);
        simplePagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        simplePagerTitleView.setNormalColor(context.getResources().getColor(R.color.color_999999));
        simplePagerTitleView.setSelectedColor(context.getResources().getColor(R.color.color_222222));
        return simplePagerTitleView;
    }

    public static IPagerIndicator getDefaultIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setLineHeight(UIUtil.dip2px(context, 4));
        indicator.setLineWidth(UIUtil.dip2px(context, 14));
        indicator.setRoundRadius(UIUtil.dip2px(context, 3.5f));
        indicator.setColors(context.getResources().getColor(R.color.black));
        indicator.setYOffset(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                8, context.getResources().getDisplayMetrics()));
        return indicator;
    }
}
