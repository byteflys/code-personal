package com.easing.commons.android.ui_component.module_button;

import com.easing.commons.android.module.Module;

//模块信息
@SuppressWarnings("all")
public abstract class ModuleInfo {

    //模块类名
    public String clazz;

    //模块名称
    public String name;

    //模块图标
    public int icon;

    //模块id
    public String moduleId;

    //子模块
    public ModuleInfo[] subModuleInfos;

    //找到对应模块
    public Module module() {
        return Module.get(clazz);
    }
}

