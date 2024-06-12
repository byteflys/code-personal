package com.easing.commons.android.data;

import java.io.Serializable;

public interface Jsonable extends Serializable {

    long SerializableID = 0L;

    default String stringify() {
        return JSON.stringify(this);
    }
}
