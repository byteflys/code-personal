package com.easing.commons.android.manager;

import android.content.Context;
import android.util.TypedValue;

import com.easing.commons.android.app.CommonApplication;
/**
 * 尺寸单位转换
 * */
public class Dimens {

    public static final int PX = TypedValue.COMPLEX_UNIT_PX;
    public static final int DP = TypedValue.COMPLEX_UNIT_DIP;
    public static final int SP = TypedValue.COMPLEX_UNIT_SP;

    //dp转px的比例系数
    public static float dpToPxScale() {
        return CommonApplication.ctx.getResources().getDisplayMetrics().density;
    }

    //px转dp的比例系数
    public static float pxToDpScale() {
        return 1 / CommonApplication.ctx.getResources().getDisplayMetrics().density;
    }

    //dp转px
    public static int toPx(Context ctx, float dp) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    //px转dp
    public static int toDp(Context ctx, float px) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    //dp转px
    public static int toPx(float dp) {
        return Dimens.toPx(CommonApplication.ctx, dp);
    }

    //px转dp
    public static int toDp(float px) {
        return Dimens.toDp(CommonApplication.ctx, px);
    }
}
