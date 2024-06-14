package com.easing.commons.android.ui.form;

import android.view.View;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//单元格信息
@SuppressWarnings("all")
public class FormItem {

    //控件类型
    public String type;

    //控件在数据库中的字段名
    public String field;

    //输入类型（输入格式限制，数字，小数，邮箱等）
    public String inputType;

    //控件默认值（比如标签控件默认标题，输入框默认内容，下拉框默认选中项等）
    public String value;


    //控件内容（比如下拉框的所有可选值，数组用JSON表示）
    public String content;

    //控件内容选项需要请求的接口地址 //
    public String reservedField;

    //控件描述（可选）
    public String description;

    //是否必填
    public Integer isRequired = 0;

    //控件位置：起始行
    public Integer startRow;

    //控件位置：起始列
    public Integer startColumn;

    //控件位置：跨几行
    public Integer rowSpan;

    //控件位置：跨几列
    public Integer columnSpan;

    //控件网络数据 一般使用时添加
    public Map<String, Object> dataMap;
    //生成的View
    public transient View itemView;
}

