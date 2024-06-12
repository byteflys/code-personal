package com.easing.commons.android.data;

//包装数据
public class Data<T> {

    //基本数据
    public T data;

    //额外参数
    public Object[] args;

    public static Data create() {
        return new Data();
    }

    public static <T> Data<T> create(T data) {
        Data<T> d = new Data();
        d.data = data;
        return d;
    }

    public Data<T> set(T data) {
        this.data = data;
        return this;
    }

    public <T> T get() {
        return (T) data;
    }

    //设置额外参数
    public Object args(Object... args) {
        this.args = args;
        return this;
    }

    //获取额外参数
    public <S> S arg(int index) {
        return (S) args[index];
    }
}
