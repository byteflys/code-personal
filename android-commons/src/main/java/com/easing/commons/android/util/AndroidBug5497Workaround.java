package com.easing.commons.android.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * 软键盘高度监听
 */
public class AndroidBug5497Workaround {
    private OnKeyboardToggleListener onKeyboardToggleListener;

    public static void assistActivity(Activity activity, OnKeyboardToggleListener onKeyboardToggleListener) {
        new AndroidBug5497Workaround(activity, onKeyboardToggleListener);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    //    private FrameLayout.LayoutParams frameLayoutParams;
    private boolean mIsKeyboardShowed;


    private AndroidBug5497Workaround(Activity activity, OnKeyboardToggleListener onKeyboardToggleListener) {
        this.onKeyboardToggleListener = onKeyboardToggleListener;
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
//        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;

//            LogUtils.w("AndroidBug5497Workaround{} ... possiblyResizeChildOfContent() --> heightDifference = " + heightDifference);

            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
//                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;

                if (onKeyboardToggleListener != null) {
                    mIsKeyboardShowed = true;
                    onKeyboardToggleListener.onKeyboardShown(heightDifference);
                }
            } else {
                // keyboard probably just became hidden
//                frameLayoutParams.height = usableHeightSansKeyboard;
                if (onKeyboardToggleListener != null && mIsKeyboardShowed) {
                    mIsKeyboardShowed = false;
                    onKeyboardToggleListener.onKeyboardClosed(heightDifference);
                }
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    public interface OnKeyboardToggleListener {
        void onKeyboardShown(int keyboardSize);

        void onKeyboardClosed(int keyboardSize);
    }

}
