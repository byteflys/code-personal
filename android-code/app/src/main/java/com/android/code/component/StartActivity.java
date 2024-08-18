package com.android.code.component;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import com.android.code.R;
import com.android.code.ui.DrawableView;
import com.android.code.ui.EraserView;
import com.android.code.ui.HeartDiagramView;
import com.android.code.ui.InvertImageView_DstIn;
import com.android.code.ui.InvertImageView_SrcATop;
import com.android.code.ui.InvertImageView_SrcIn;
import com.android.code.ui.RoundImageView_DstIn;
import com.android.code.ui.RoundImageView_SrcATop;
import com.android.code.ui.ScratchCard;
import com.android.code.ui.TwitterView;
import com.android.code.ui.WaveView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("all")
public class StartActivity extends AppCompatActivity {

    ViewGroup container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        container = findViewById(R.id.container);
        init();
    }

    protected void onClick(int id, View.OnClickListener listener) {
        findViewById(id).setOnClickListener(listener);
    }

    protected void init() {
        //SrcIn实现圆角图片
        onClick(R.id.menu1, (v) -> {
            container.removeAllViews();
            DrawableView view = new DrawableView(this);
            container.addView(view, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        });
        //DstIn实现圆角图片
        onClick(R.id.menu2, (v) -> {
            container.removeAllViews();
            RoundImageView_DstIn view = new RoundImageView_DstIn(this);
            container.addView(view, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        });
        //SrcATop实现圆角图片
        onClick(R.id.menu3, (v) -> {
            container.removeAllViews();
            RoundImageView_SrcATop view = new RoundImageView_SrcATop(this);
            container.addView(view, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        });
        //SrcIn实现倒影效果
        onClick(R.id.menu4, (v) -> {
            container.removeAllViews();
            InvertImageView_SrcIn view = new InvertImageView_SrcIn(this);
            container.addView(view, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        });
        //DstIn实现倒影效果
        onClick(R.id.menu5, (v) -> {
            container.removeAllViews();
            InvertImageView_DstIn view = new InvertImageView_DstIn(this);
            container.addView(view, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        });
        //SrcATop实现倒影效果
        onClick(R.id.menu6, (v) -> {
            container.removeAllViews();
            InvertImageView_SrcATop view = new InvertImageView_SrcATop(this);
            container.addView(view, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        });
        //Clear和DstIn实现心电图效果
        onClick(R.id.menu7, (v) -> {
            container.removeAllViews();
            HeartDiagramView view = new HeartDiagramView(this);
            container.addView(view, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        });
        //SrcIn实现波浪效果
        onClick(R.id.menu8, (v) -> {
            container.removeAllViews();
            WaveView view = new WaveView(this);
            container.addView(view, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        });
        //Multiply实现推特图标裁边效果
        onClick(R.id.menu9, (v) -> {
            container.removeAllViews();
            TwitterView view = new TwitterView(this);
            container.addView(view, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        });
        //SrcOut实现橡皮擦效果
        onClick(R.id.menu10, (v) -> {
            container.removeAllViews();
            EraserView view = new EraserView(this);
            container.addView(view, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        });
        //DST_IN实现刮刮卡效果
        onClick(R.id.menu11, (v) -> {
            container.removeAllViews();
            ScratchCard view = new ScratchCard(this);
            container.addView(view, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        });
    }
}


