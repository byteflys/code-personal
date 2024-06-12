package com.easing.commons.android.ui_component.OptionPick;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.data.DataCenter;
import com.easing.commons.android.helper.tool.NameTranslator;

import java.util.ArrayList;
import java.util.List;

public class MultiOptionPicker {

    public static void pick(String requestCode, String title, List fullOptionList, List selectedOptionList, NameTranslator nameTranslator) {
        if (fullOptionList == null)
            fullOptionList = new ArrayList();
        if (selectedOptionList == null)
            selectedOptionList = new ArrayList();
        DataCenter.set("requestCodeForMultiOptionPick", requestCode);
        DataCenter.set("titleForOptionPick", title);
        DataCenter.set("fullOptionList", fullOptionList);
        DataCenter.set("selectedOptionList", selectedOptionList);
        DataCenter.set("nameTranslator", nameTranslator);
        CommonApplication.startActivity(MultiOptionPickActivity.class);
    }
}

