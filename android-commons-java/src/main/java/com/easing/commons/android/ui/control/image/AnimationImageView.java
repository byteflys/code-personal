package com.easing.commons.android.ui.control.image;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.easing.commons.android.R;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.thread.MainThread;

/**
 * 仿抖音直播头像动画
 */
public class AnimationImageView extends FrameLayout {
    private int bounderW = 0;
    private int bounderH = 0;
    private boolean running = false;
    private int h;
    private int w;
    private int anim_circle_src;
    private int circle_src;
    private int image_src;
    private float scaleOffset = 0.1f;

    public AnimationImageView(Context context) {
        super(context);
        init(context, null);
    }

    public AnimationImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public AnimationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimationImageView);
            anim_circle_src = typedArray.getResourceId(R.styleable.AnimationImageView_aiv_anim_circle_src, R.drawable.shape_bg_bdb3f8_circle);
            circle_src = typedArray.getResourceId(R.styleable.AnimationImageView_aiv_circle_src, R.drawable.shape_bg_bdb3f8_circle);
            image_src = typedArray.getResourceId(R.styleable.AnimationImageView_aiv_image_src, R.drawable.ic_menu_default);
            scaleOffset = typedArray.getFloat(R.styleable.AnimationImageView_aiv_scale_offset, scaleOffset);
            typedArray.recycle();
        }
        initView();
    }

    private ImageView imageView;
    private ImageView circle;
    private ImageView animatorCircle;

    public ImageView getImageView() {
        return imageView;
    }

    public ImageView getCircleView() {
        return circle;
    }

    public ImageView getAnimatorCircleView() {
        return animatorCircle;
    }

    private void initView() {
        try {
            if (imageView == null) {
                addView(imageView = new ImageView(getContext()));
                imageView.setImageResource(image_src);
            }
            checkCircle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkCircle() {
        if (enablePlay) {
          /*  if (circle == null) {
                addView(circle = new ImageView(getContext()));
                circle.setImageResource(circle_src);
            }*/
            if (animatorCircle == null) {
                addView(animatorCircle = new ImageView(getContext()));
                animatorCircle.setImageResource(circle_src);
            }
        } else {
            removeCircle();
        }
    }

    private void removeCircle() {
        try {
            if (circle != null) {
                removeView(circle);
                circle = null;
            }
            if (animatorCircle != null) {
                removeView(animatorCircle);
                animatorCircle = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LayoutParams imageViewParams;

    private void measureView() {
        try {
            bounderW = enablePlay ? (int) (w * scaleOffset) : 0;
            bounderH = enablePlay ? (int) (h * scaleOffset) : 0;
            checkCircle();
            if (imageView != null) {
                if (imageViewParams == null) {
                    imageViewParams = new LayoutParams(w - bounderW, h - bounderH);
                    imageViewParams.gravity = Gravity.CENTER;
                } else {
                    imageViewParams.width = w - bounderW;
                    imageViewParams.height = h - bounderH;
                }
                imageView.setLayoutParams(imageViewParams);
            }
            if (enablePlay && circle != null) {
                LayoutParams params = new LayoutParams(w - bounderW, h - bounderH);
                params.gravity = Gravity.CENTER;
                circle.setLayoutParams(params);
            }
            if (enablePlay && animatorCircle != null) {
                LayoutParams params = new LayoutParams(w - bounderW, h - bounderH);
                params.gravity = Gravity.CENTER;
                animatorCircle.setLayoutParams(params);
            }
            try {
                ViewParent parent = getParent();
                if (parent != null && parent instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) parent;
                    viewGroup.setClipChildren(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        try {
            h = getMeasuredHeight();
            w = getMeasuredWidth();
            measureView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPlay() {
        try {
            if (imageView != null) {
                imageView.clearAnimation();
            }
            if (circle != null) {
                circle.clearAnimation();
            }
            if (animatorCircle != null) {
                animatorCircle.clearAnimation();
            }
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            if (animatorSet2 != null) {
                animatorSet2.cancel();
            }
            running = false;
            animatorSet = null;
            animatorSet2 = null;
            measureView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AnimatorSet animatorSet;
    AnimatorSet animatorSet2;
    private boolean enablePlay = true;

    public void setEnablePlay(boolean enablePlay) {
        this.enablePlay = enablePlay;
        if (enablePlay) {
            startPlay();
        } else {
            stopPlay();
        }
    }

    public void startPlay() {
        try {
            if (!enablePlay) {
                stopPlay();
                return;
            }
            if (running) {
                return;
            }
            measureView();
            running = true;
           /* if (animatorSet == null) {
                animatorSet = new AnimatorSet();
                ObjectAnimator imageViewAnimatorX = ImageViewScaleAnimatorX(imageView);
                ObjectAnimator imageViewAnimatorY = ImageViewScaleAnimatorY(imageView);
                animatorSet.playTogether(setRepeat(imageViewAnimatorX),
                        setRepeat(imageViewAnimatorY));

            }*/
            if (animatorSet2 == null) {
                animatorSet2 = new AnimatorSet();
                ObjectAnimator circleAnimatorX = circleScaleAnimatorX(animatorCircle);
                ObjectAnimator circleAnimatorY = circleScaleAnimatorY(animatorCircle);
                ObjectAnimator circleAlphaAnimator = circleAlphaAnimator(animatorCircle);
                animatorSet2.playTogether(setRepeat2(circleAnimatorX),
                        setRepeat2(circleAnimatorY),
                        setRepeat2(circleAlphaAnimator));
            }
            //  animatorSet.start();

            MainThread.postLater(new Action() {
                @Override
                public void run() throws Exception {
                    if (animatorSet2 != null)
                        animatorSet2.start();
                }
            }, 1000);

        } catch (Exception e) {
            e.printStackTrace();
            running = false;
        }
    }

    private ObjectAnimator setRepeat(ObjectAnimator objectAnimator) {
        if (objectAnimator != null) {
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimator.setDuration(1000);
        }
        return objectAnimator;
    }

    private ObjectAnimator setRepeat2(ObjectAnimator objectAnimator) {
        if (objectAnimator != null) {
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimator.setDuration(1500);
        }
        return objectAnimator;
    }

    private ObjectAnimator ImageViewScaleAnimatorX(View view) {
        return ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.9f, 1.0f);
    }

    private ObjectAnimator ImageViewScaleAnimatorY(View view) {
        return ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.9f, 1.0f);
    }

    private ObjectAnimator ImageViewLiveScaleAnimatorX(View view) {
        return ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.9f, 1.0f);
    }

    private ObjectAnimator ImageViewLiveScaleAnimatorY(View view) {
        return ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.9f, 1.0f);
    }

    private ObjectAnimator circleScaleAnimatorX(View view) {
        return ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f);
    }

    private ObjectAnimator circleScaleAnimatorY(View view) {
        return ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.5f);
    }

    private ObjectAnimator circleAlphaAnimator(View view) {
//        return  ObjectAnimator.ofFloat(view, "alpha", 0.5f, 0.0f, 1f, 0.0f, 0.1f, 0.0f, 0.5f, 0.0f, 0.1f, 0.0f, 1f, 0.0f, 0.5f);
        return ObjectAnimator.ofFloat(view, "alpha", 0.8f, 0f);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            stopPlay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE && !running) {
            startPlay();
        } else if ((visibility == INVISIBLE || visibility == GONE) && running) {
            stopPlay();
        }
    }

    public boolean isEnablePlay() {
        return enablePlay;
    }
}
