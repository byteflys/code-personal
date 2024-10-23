package com.easing.commons.android.struct;

import com.easing.commons.android.data.JSON;

import java.util.LinkedList;

public class SerializableList<T> extends LinkedList<T> {

    @Override
    public String toString() {
        return JSON.stringify(this);
    }
}

