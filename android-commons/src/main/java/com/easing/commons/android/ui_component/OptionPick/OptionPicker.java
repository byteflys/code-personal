package com.easing.commons.android.ui_component.OptionPick;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.data.DataCenter;
import com.easing.commons.android.helper.tool.NameTranslator;

import java.util.ArrayList;
import java.util.List;

public class OptionPicker {

    public static void pick(String requestCode, String title, List fullOptionList, Object selectedOption, NameTranslator nameTranslator) {
        if (fullOptionList == null)
            fullOptionList = new ArrayList();
        DataCenter.set("requestCodeForOptionPick", requestCode);
        DataCenter.set("titleForOptionPick", title);
        DataCenter.set("fullOptionList", fullOptionList);
        DataCenter.set("selectedOption", selectedOption);
        DataCenter.set("nameTranslator", nameTranslator);
        CommonApplication.startActivity(OptionPickActivity.class);
    }
}

