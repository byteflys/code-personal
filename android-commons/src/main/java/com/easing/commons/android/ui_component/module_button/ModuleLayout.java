package com.easing.commons.android.ui_component.module_button;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import com.easing.commons.android.format.Maths;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.module.Module;
import com.easing.commons.android.ui.control.scroll.BounceScrollView;
import com.easing.commons.android.ui.dialog.TipBox;
import com.easing.commons.android.view.Views;

import java.util.List;

//模块布局，只支持普通模块，不支持模块分组功能
@SuppressWarnings("all")
public class ModuleLayout extends BounceScrollView {

    int columnCount = 3;
    int rowCount = 1;
    int padding = Dimens.toPx(5);

    FrameLayout frameLayout;
    GridLayout gridLayout;

    Context context;

    public ModuleLayout(Context context) {
        this(context, null);
    }

    public ModuleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    //设置模块列表
    public void modules(List<ModuleInfo> infos) {
        post(() -> {
            //移除旧控件
            removeAllViews();

            //TODO
            //无数据时设置背景提示
            //进入编辑模式，通过拖拽来添加模块
            if (infos.size() == 0)
                setBackground(null);
            else
                setBackground(null);

            //计算行列数及大小
            rowCount = Maths.upperInt(infos.size() / (double) columnCount);
            int parentWidth = getMeasuredWidth();
            int parentHeight = getMeasuredHeight();
            int gridWidth = parentWidth;
            int cellWidth = (gridWidth - padding * (columnCount - 1)) / columnCount;
            int gridHeight = cellWidth * rowCount + padding * (columnCount - 1);
            if (gridHeight < parentHeight) gridHeight = parentHeight;
            if (gridHeight < cellWidth) gridHeight = cellWidth;

            //添加FrameLayout
            frameLayout = new FrameLayout(context);
            addView(frameLayout, new BounceScrollView.LayoutParams(Views.MATCH_PARENT, Views.WRAP_CONTENT));

            //添加GirdLayout
            gridLayout = new GridLayout(getContext());
            gridLayout.setColumnCount(columnCount);
            frameLayout.addView(gridLayout, new FrameLayout.LayoutParams(gridWidth, gridHeight));

            //添加GirdLayout中的单元格
            for (int i = 0; i < infos.size(); i++) {
                ModuleInfo info = infos.get(i);
                ModuleButton button = new ModuleButton(context);
                button.setImageDrawable(getResources().getDrawable(info.icon, null));
                button.setBackground(generateBackgroundDrawable(i));
                button.info = info;

                int row = Maths.floorInt(i / (double) columnCount);
                int col = i % columnCount;
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(row, 1), GridLayout.spec(col, 1));
                layoutParams.width = cellWidth;
                layoutParams.height = cellWidth;
                if (row != rowCount - 1) layoutParams.bottomMargin = padding;
                if (col != columnCount - 1) layoutParams.rightMargin = padding;
                gridLayout.addView(button, layoutParams);

                //设置点击事件
                Views.onClick(button, () -> {
                    Module module = info.module();
                    if (module == null)
                        TipBox.tipInCenter("敬请期待");
                    else {
                        module.register();
                        module.launch();
                    }
                });
            }
        });
    }

    //生成渐变色Drawable
    public static Drawable generateBackgroundDrawable(int index) {
        int[] gradientColors = generateFixedGradientColors();
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TR_BL, gradientColors);
        drawable.setCornerRadius(Dimens.toPx(5));
        return drawable;
    }

    //生成固定渐变色
    public static int[] generateFixedGradientColors() {
        int[] gradientColors = new int[3];
        gradientColors[0] = 0xFF0D80FC;
        gradientColors[1] = 0xFF08A2F9;
        gradientColors[2] = 0xFF01CCF9;
        return gradientColors;
    }

    //生成随机渐变色
    public static int[] generateGradientColors(int index) {
        int type = index % 6;
        int[] gradientColors = new int[3];
        switch (type) {
            case 0:
                gradientColors[0] = 0xFF0D80FC;
                gradientColors[1] = 0xFF08A2F9;
                gradientColors[2] = 0xFF01CCF9;
                break;
            case 1:
                gradientColors[0] = 0xFFFF6600;
                gradientColors[1] = 0xFFFF8800;
                gradientColors[2] = 0xFFFFAA00;
                break;
            case 2:
                gradientColors[0] = 0xFFFB2440;
                gradientColors[1] = 0xFFFD3763;
                gradientColors[2] = 0xFFFF4A9A;
                break;
            case 3:
                gradientColors[0] = 0xFF7530DF;
                gradientColors[1] = 0xFF8F3DEE;
                gradientColors[2] = 0xFFA950FE;
                break;
            case 4:
                gradientColors[0] = 0xFF880000;
                gradientColors[1] = 0xFFAA0000;
                gradientColors[2] = 0xFFCC0000;
                break;
            case 5:
                gradientColors[0] = 0xFF004400;
                gradientColors[1] = 0xFF006600;
                gradientColors[2] = 0xFF038203;
                break;
            default:
                gradientColors[0] = 0xFF000000;
                gradientColors[1] = 0xFF000000;
                gradientColors[2] = 0xFF000000;
                break;
        }
        return gradientColors;
    }
}
