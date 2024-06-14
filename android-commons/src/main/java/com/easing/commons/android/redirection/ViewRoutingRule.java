package com.easing.commons.android.redirection;

import android.app.Activity;

public class ViewRoutingRule {

    //规则名称
    public String name;

    //跳转目标
    public Class<? extends Activity> target;

    //跳转前是否销毁当前界面
    public Boolean destroyCurrent;
}
