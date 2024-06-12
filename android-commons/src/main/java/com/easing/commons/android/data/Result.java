package com.easing.commons.android.data;

import java.util.LinkedHashMap;
import java.util.Map;

//封装运算结果和错误信息
public class Result<T> {

    public static final int SUCCESS = 0;
    public static final int FAIL = -1;

    public boolean success;
    public int code;
    public String message;
    public T data;
    public Object error;
    public Object errMsg;

    public transient Map extraArgs = new LinkedHashMap();

    public static Result success() {
        Result rst = new Result();
        rst.success = true;
        rst.code = SUCCESS;
        rst.message = "success";
        return rst;
    }

    public static Result fail() {
        Result rst = new Result();
        rst.success = false;
        rst.code = FAIL;
        rst.message = "fail";
        return rst;
    }

    public static Result success(int code, String msg, Object data) {
        Result rst = new Result();
        rst.success = true;
        rst.code = code;
        rst.message = msg;
        rst.data = data;
        return rst;
    }

    public static Result fail(int code, String msg, Object data) {
        Result rst = new Result();
        rst.success = false;
        rst.code = code;
        rst.message = msg;
        rst.data = data;
        return rst;
    }

    //由于类本身已经包含泛型T
    //必须在使用时指定T的具体类型，才能使用带其它泛型（K）的泛型方法
    public <K> K data() {
        return (K) data;
    }
}
