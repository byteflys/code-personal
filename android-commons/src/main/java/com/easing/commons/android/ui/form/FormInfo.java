package com.easing.commons.android.ui.form;

import com.easing.commons.android.data.JSON;

import java.util.ArrayList;
import java.util.List;

//表格信息
@SuppressWarnings("all")
public class FormInfo {

    //表格总行数
    public Integer rowCount;

    //表格总列数
    public Integer columnCount;

    //表格单元格列表
    public final List<FormItem> formItems = new ArrayList();

    //布局信息转化为JSON字符串
    public String toJson(){
        return JSON.stringify(this);
    }

    //从JSON字符串解析整个表格的布局信息
    public static FormInfo fromJson(String json) {
        return JSON.parse(json, FormInfo.class);
    }
}
