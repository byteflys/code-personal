package com.easing.commons.android.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.ui.control.other.LyricView;

@SuppressWarnings("all")
public class Fonts {

    public enum Font {

        LIBIAN("font/libian.ttf"),
        ALTERNATE_BOLD("font/alternate_bold.ttf");

        String path;

        Font(String path) {
            this.path = path;
        }

    }

    public static void bindFont(View root, Font font) {
        bindFont(root, font, 400);
    }

    public static void bindFonts(Font font, View... root) {
        for (View view : root) {
            bindFont(view, font, 400);
        }
    }

    //400普通，700加粗
    public static void bindFont(View root, Font font, int weight) {
        Context context = root.getContext();
        Typeface.Builder builder = new Typeface.Builder(context.getAssets(), font.path);
        builder.setWeight(weight);
        Typeface typeface = builder.build();

        if (root instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) root;
            for (int i = 0; i < viewGroup.getChildCount(); i++)
                bindFont(viewGroup.getChildAt(i), font);
        } else if (root instanceof TextView) {
            TextView tv = (TextView) root;
            tv.setTypeface(typeface);
        } else if (root instanceof LyricView) {
            LyricView tv = (LyricView) root;
            tv.setTypeface(typeface);
        }
    }

    public static Typeface getTypeface(View v, Font font) {
        return Typeface.createFromAsset(v.getContext().getAssets(), font.path);
    }

    public static Typeface create(String path) {
        return Typeface.createFromAsset(CommonApplication.ctx.getAssets(), path);
    }

}
