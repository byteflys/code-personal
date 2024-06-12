package com.easing.commons.android.functional;

public interface Filter<T> {

    boolean keep(T v);
}
