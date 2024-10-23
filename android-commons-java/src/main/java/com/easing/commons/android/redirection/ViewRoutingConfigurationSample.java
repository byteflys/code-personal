package com.easing.commons.android.redirection;

import android.app.Activity;

//这只是一个参考类
//请在工程主模块自行添加一个同名类，参考此模式配置路由规则
public class ViewRoutingConfigurationSample {

    @ViewRouting(target = Activity.class, destroyCurrent = true)
    public Object onSplashFinish;

    @ViewRouting(target = Activity.class, destroyCurrent = true)
    public Object onLoginSuccess;

    @ViewRouting(target = Activity.class)
    public Object openVideoPlayUi;
}

