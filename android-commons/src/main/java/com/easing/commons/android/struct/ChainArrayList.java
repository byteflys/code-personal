package com.easing.commons.android.struct;

import com.easing.commons.android.helper.exception.BizException;

import java.util.ArrayList;
import java.util.LinkedList;

//使用链式调用风格包装一个LinkedList，添加一些实用方法
public class ChainArrayList<T> {

    public final ArrayList<T> baseList = new ArrayList();

    public static <T> ChainArrayList<T> create(Class<T> clazz) {
        return new ChainArrayList();
    }

    public ChainArrayList<T> add(T item) {
        baseList.add(item);
        return this;
    }

    public ChainArrayList<T> add(T... itemArray) {
        for (T item : itemArray)
            baseList.add(item);
        return this;
    }

    public T first() {
        if (baseList.size() == 0) return null;
        return baseList.get(0);
    }

    public T last() {
        if (baseList.size() == 0) return null;
        return baseList.get(baseList.size() - 1);
    }

    public T next(T item) {
        int index = baseList.indexOf(item);
        if (index == -1) throw BizException.of("item do not exist");
        if (index < baseList.size() - 1)
            return baseList.get(index + 1);
        return null;
    }

    public T previous(T item) {
        int index = baseList.indexOf(item);
        if (index == -1) throw BizException.of("item do not exist");
        if (index > 0)
            return baseList.get(index - 1);
        return null;
    }

    public int size() {
        return baseList.size();
    }

    public boolean empty() {
        return baseList.size() == 0;
    }
}
