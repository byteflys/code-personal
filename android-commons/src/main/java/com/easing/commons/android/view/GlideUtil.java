package com.easing.commons.android.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.easing.commons.android.R;
import com.easing.commons.android.helper.callback.SucceedCallBackListener;
import com.easing.commons.android.manager.Device;
import com.easing.commons.android.manager.Dimens;

/**
 * 图片加载
 */
public class GlideUtil {
    /**
     * 加载普通图片
     */
    public static void loadNetImage(ImageView imageView, Object url) {
        if (imageView == null || isInValidContextForGlide(imageView.getContext()) || url == null)
            return;

        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.image_login)
                .error(R.drawable.photo_load_failed)
                .centerCrop()
                .into(imageView);

        // Glide.with(context).asGif().load(R.drawable.gif_loading).into(imageView);
    }

    /**
     * 不使用缓存
     */
    public static void loadNotMemoryNetImage(ImageView imageView, Object url) {
        if (imageView == null || isInValidContextForGlide(imageView.getContext()) || url == null)
            return;

        Glide.with(imageView.getContext())
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                .into(imageView);

    }


    /**
     * 带占位
     *
     * @param imageView
     * @param url
     * @param placeholderId 加载中的占位图
     * @param errorId       失败后占位图
     */
    public static void loadNetDefaultImage(ImageView imageView, Object url,
                                           @DrawableRes int placeholderId,
                                           @DrawableRes int errorId) {
        if (imageView == null || isInValidContextForGlide(imageView.getContext()) || url == null)
            return;

        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .placeholder(placeholderId)
                .error(errorId)
                .into(imageView);

    }

    /**
     * 加载自定义圆角
     *
     * @param imageView
     * @param url
     * @param radius      圆角大小 单位 dp
     * @param rightTop    是否圆角又上角
     * @param leftTop     是否圆角左上角
     * @param leftBottom  是否圆角左下角
     * @param rightBottom 是否圆角又下角
     */
    public static void loadNetRadiusImage(ImageView imageView, Object url, int radius, boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
        if (imageView == null || isInValidContextForGlide(imageView.getContext()) || url == null)
            return;
        CornerTransform transformation = new CornerTransform(imageView.getContext(), Dimens.toPx(radius));
        //只是绘制左上角和右上角圆角
        transformation.setExceptCorner(leftTop, rightTop, leftBottom, rightBottom);

        Glide.with(imageView.getContext()).
                load(url).
                centerCrop().
                diskCacheStrategy(DiskCacheStrategy.NONE).
                transform(transformation).
                into(imageView);

    }


    /**
     * 宽度全屏 高度自动适配
     *
     * @param imageHeight 最大高度 0为全部填充
     */
    public static void loadIntoUseFitWidth(ImageView imageView, Object imageUrl, int imageHeight) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageView.setAdjustViewBounds(true);
                            imageView.setMaxHeight(imageHeight);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();

                        int screenWidth = Device.screenWidth();


                        float bi = ((float) screenWidth) / ((float) drawable.getIntrinsicWidth());
                        //float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        // int vh = (int) ((float) vw / (float) 1.78);
                        //  params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        int height = 0;
                        if (imageHeight != 0) {

                            if ((int) (((float) drawable.getIntrinsicHeight()) * bi) > imageHeight) {
                                height = imageHeight;
                            } else {
                                height = (int) (((float) drawable.getIntrinsicHeight()) * bi);
                            }
                        } else {
                            height = (int) (((float) drawable.getIntrinsicHeight()) * bi);
                        }
                        params.height = height;
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .into(imageView);
    }


    private static boolean isInValidContextForGlide(final Context context) {
        if (context == null) {
            return true;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            return activity.isDestroyed() || activity.isFinishing();
        }
        return false;
    }

    /**
     * 带回调的图片加载器
     */
    public static void loadIntoListener(ImageView imageView, Object imageUrl, SucceedCallBackListener<Boolean> listener) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.image_login)
                .error(R.drawable.photo_load_failed).
                diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {

                        if (listener != null)
                            listener.succeedCallBack(false);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        if (listener != null)
                            listener.succeedCallBack(true);
                        return false;
                    }
                })
                .into(imageView);
    }
}
