package com.easing.commons.android.struct;

import java.util.ArrayList;
import java.util.List;

public class StringList extends ArrayList<String> {

    public static StringList getDefault() {
        return new StringList();
    }

    public static StringList copyFrom(List<String> source) {
        StringList list = StringList.getDefault();
        list.addAll(source);
        return list;
    }

    public static StringList from(List<String> list) {
        StringList stringList = StringList.getDefault();
        stringList.addAll(list);
        return stringList;
    }
}


