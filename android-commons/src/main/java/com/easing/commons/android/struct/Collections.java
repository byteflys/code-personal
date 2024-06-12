package com.easing.commons.android.struct;

import com.easing.commons.android.format.TextBuilder;
import com.easing.commons.android.functional.EqualAssertor;
import com.easing.commons.android.functional.Comparator;
import com.easing.commons.android.functional.Predication;
import com.easing.commons.android.functional.StringConvertor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("all")
public class Collections {

    public static <T> T[] toArray(Set<T> set, T[] array) {
        return set.toArray(array);
    }

    public static <T> T[] toArray(List<T> list, ListArrayConvertor<T> convertor) {
        return convertor.toArray(list);
    }

    public static String[] toStringArray(Set<String> set) {
        return set.toArray(new String[set.size()]);
    }

    public static Object[][] toArray2(List<Object[]> list) {
        if (list == null)
            return null;
        if (list.size() == 0)
            return new Object[0][0];
        int len1 = list.size();
        int len2 = list.get(0).length;
        Object[][] array = new Object[len1][len2];
        for (int i = 0; i < len1; i++)
            for (int j = 0; j < len2; j++)
                array[i][j] = list.get(i)[j];
        return array;
    }

    public static <T> T getFirst(List<T> objs) {
        return objs.get(0);
    }

    public static <T> T getLast(List<T> objs) {
        return objs.get(objs.size() - 1);
    }

    public static <T> boolean isLastItem(List<T> list, T item) {
        int index = list.indexOf(item);
        if (index < 0) return false;
        return index == list.size() - 1;
    }

    //过滤数据
    public static <T> T[] filter(T[] datas, ListArrayConvertor<T> listArrayConvertor, Predication<T> predication) {
        List<T> list = new ArrayList();
        for (T data : datas)
            if (predication.predicate(data))
                list.add(data);
        T[] newArray = listArrayConvertor.toArray(list);
        return newArray;
    }

    //过滤数据
    public static <T> List<T> filter(List<T> datas, Predication<T> predication) {
        if (datas == null) return new ArrayList();
        List<T> filtered = new ArrayList();
        synchronized (datas) {
            for (T data : datas)
                if (predication.predicate(data))
                    filtered.add(data);
        }
        return filtered;
    }

    //查找单个数据
    public static <T> T find(List<T> datas, Predication<T> predication) {
        synchronized (datas) {
            for (T data : datas)
                if (predication.predicate(data))
                    return data;
        }
        return null;
    }

    //排序数据
    public static <T> void sort(List<T> datas, Comparator<T> comparator) {
        if (datas == null) return;
        java.util.Collections.sort(datas, comparator::compare);
    }

    //交换List中的数据
    public static <T> void swap(List<T> datas, int aIndex, int bIndex) {
        java.util.Collections.swap(datas, aIndex, bIndex);
    }

    //交换List中的数据
    public static <T> void swap(List<T> datas, T a, T b) {
        int aIndex = datas.indexOf(a);
        int bIndex = datas.indexOf(b);
        java.util.Collections.swap(datas, aIndex, bIndex);
    }

    //移动List中的数据，将a移到b的位置
    public static <T> void moveTo(List<T> datas, T a, T b) {
        int aIndex = datas.indexOf(a);
        int bIndex = datas.indexOf(b);
        datas.remove(a);
        datas.add(bIndex, a);
    }

    //数组转List
    public static <T> LinkedList<T> asList(T... array) {
        if (array == null) return null;
        LinkedList<T> list = new LinkedList();
        for (T item : array)
            list.add(item);
        return list;
    }

    public static <T> LinkedList<T> asList(Set<T> set) {
        if (set == null) return null;
        LinkedList<T> list = new LinkedList();
        for (T item : set)
            list.add(item);
        return list;
    }

    public static <T> List<T> asList(Collection<T> collection) {
        if (collection == null) return null;
        LinkedList<T> list = new LinkedList();
        for (T item : collection)
            list.add(item);
        return list;
    }

    //判断List是否为空
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    //判断数组是否为空
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    //找到收个null所在位置
    public static int findNull(List list) {
        for (int i = 0; i < list.size(); i++)
            if (list.get(i) == null)
                return i;
        return -1;
    }

    //创建一个空的List
    public static <T> LinkedList<T> emptyList() {
        return new LinkedList();
    }

    //创建一个空的Map
    public static <K, V> LinkedHashMap<K, V> emptyMap() {
        return new LinkedHashMap();
    }

    //创建一个空的支持线程并发的List
    public static <T> List<T> sychronizedList() {
        return java.util.Collections.synchronizedList(new LinkedList());
    }

    //创建一个空的支持线程并发的Map
    public static <K, V> Map<K, V> sychronizedMap() {
        return java.util.Collections.synchronizedMap(new LinkedHashMap());
    }

    //数组拷贝
    public static <T> List<T> listCopy(List<T> source) {
        LinkedList<T> dest = new LinkedList();
        for (T item : source)
            dest.add(item);
        return dest;
    }

    //MAP拷贝
    public static <K, T> Map<K, T> mapCopy(Map<K, T> source) {
        Map dest = new LinkedHashMap();
        for (K key : source.keySet())
            dest.put(key, source.get(key));
        return dest;
    }

    public static <T> List<T> differ(List<T> collectA, List<T> collectB, EqualAssertor<T> assertor) {
        List<T> differentItems = new LinkedList();
        for (T itemA : collectA) {
            boolean different = true;
            for (T itemB : collectB)
                if (assertor.isEqual(itemA, itemB)) {
                    different = false;
                    break;
                }
            if (different)
                differentItems.add(itemA);
        }
        for (T itemB : collectB) {
            boolean different = true;
            for (T itemA : collectA)
                if (assertor.isEqual(itemB, itemA)) {
                    different = false;
                    break;
                }
            if (different)
                differentItems.add(itemB);
        }
        return differentItems;
    }

    public static <T, S> List<S> convert(List<T> collectA, ListConvertor<T, S> convertor) {
        LinkedList<S> collectB = Collections.emptyList();
        for (T a : collectA)
            collectB.add(convertor.convert(a));
        return collectB;
    }

    //判断字符串数组中是否包含某字符串
    public static boolean contains(String[] types, String type) {
        for (String item : types)
            if (item.equals(type))
                return true;
        return false;
    }

    //将List转为字符串显示
    public static <T> String toString(List<T> list, StringConvertor<T> convertor, String split) {
        TextBuilder builder = TextBuilder.create();
        for (int i = 0; i < list.size(); i++) {
            T item = list.get(i);
            builder.append(convertor.covert(item));
            if (i != list.size() - 1)
                builder.append(split);
        }
        return builder.build();
    }

    //合并数组
    public static Object[] merge(Object object, Object[] array) {
        Object[] newArray = new Object[array.length + 1];
        newArray[0] = object;
        System.arraycopy(array, 0, newArray, 1, array.length);
        return newArray;
    }

    //合并数组
    public static Object[] merge(Object o1, Object o2, Object[] array) {
        Object[] newArray = new Object[array.length + 2];
        newArray[0] = o1;
        newArray[1] = o2;
        System.arraycopy(array, 0, newArray, 2, array.length);
        return newArray;
    }

    //添加数组到List
    public static <T> void addAll(List<T> list, T[] array) {
        if (array == null) return;
        for (T item : array)
            list.add(item);
    }

    //获取List中的下个item
    //如果是最后一个item，则返回空
    public static <T> T nextItem(List<T> list, T item) {
        if (list == null || list.isEmpty())
            return null;
        if (item == null)
            return list.get(0);
        int index = list.indexOf(item);
        if (index < 0)
            return null;
        if (index == list.size() - 1)
            return null;
        T next = list.get(index + 1);
        return next;
    }

    //循环获取List中的下个item
    //如果是最后一个item，则下一个返回首个元素
    public static <T> T nextItemCircularly(List<T> list, T item) {
        if (list == null || list.isEmpty()) return null;
        if (item == null) return list.get(0);
        int index = list.indexOf(item);
        return index == list.size() - 1 ? list.get(0) : list.get(index + 1);
    }

    //拷贝数组
    public static byte[] arraycopy(byte[] src, Integer srcStartPos, byte[] dst, Integer dstStartPos, Integer length) {
        if (length == null) length = src.length;
        if (dst == null) dst = new byte[length];
        if (srcStartPos == null) srcStartPos = 0;
        if (dstStartPos == null) dstStartPos = 0;
        System.arraycopy(src, srcStartPos, dst, dstStartPos, length);
        return dst;
    }

    /**
     * 复制一个新的对象
     *
     */
    @Deprecated
    public static <T> T copyObject(T object) {
        T newObject = new Gson().fromJson(new Gson().toJson(object), new TypeToken<T>() {
        }.getType());
        return newObject;
    }
}
