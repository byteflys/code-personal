package com.easing.commons.android.data;

//以数组的形式包装多个数据
@SuppressWarnings("all")
public class ArrayArguments {

    public Object[] args;

    //设置参数
    public static ArrayArguments wrap(Object... args) {
        ArrayArguments arguments = new ArrayArguments();
        arguments.args = args;
        return arguments;
    }

    //获取参数
    public <T> T arg(int index) {
        return (T) args[index];
    }
}
