package com.easing.commons.android.value.measure;

public class Rect {

    public int left;
    public int right;
    public int top;
    public int bottom;

    public int width;
    public int height;

    public Rect() {}

    public Rect(int left, int top, int right, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.width = right - left;
        this.height = bottom - top;
    }

    @Override
    public String toString() {
        return "x1-x2: " + left + "-" + right + "  " + "y1-y2: " + top + "-" + bottom;
    }

    //判断是否和其它Rect相交
    public boolean isCrossedWith(Rect other) {
        if (other.left >= this.right) return false;
        if (other.right <= this.left) return false;
        if (other.top >= this.bottom) return false;
        if (other.bottom <= this.top) return false;
        return true;
    }

    //判断两个Rect是否相交
    public static boolean isCrossed(Rect r1, Rect r2) {
        if (r2.left >= r1.right) return false;
        if (r2.right <= r1.left) return false;
        if (r2.top >= r1.bottom) return false;
        if (r2.bottom <= r1.top) return false;
        return true;
    }
}
