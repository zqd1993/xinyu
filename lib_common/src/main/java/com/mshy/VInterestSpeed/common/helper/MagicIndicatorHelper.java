package com.mshy.VInterestSpeed.common.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;

import com.mshy.VInterestSpeed.common.R;
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
        simplePagerTitleView.setSelectedColor(context.getResources().getColor(R.color.color_B4A3FD));
        return simplePagerTitleView;
    }

    public static IPagerIndicator getDefaultIndicator(Context context) {
        return getDefaultIndicator(context, R.color.color_B4A3FD);
    }

    public static IPagerIndicator getDefaultIndicator(Context context, int color) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setLineHeight(UIUtil.dip2px(context, 4));
        indicator.setLineWidth(UIUtil.dip2px(context, 16));
        indicator.setRoundRadius(UIUtil.dip2px(context, 2f));
        indicator.setColors(context.getResources().getColor(color));
        indicator.setYOffset(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                8, context.getResources().getDisplayMetrics()));
        return indicator;
    }
}
