package com.easing.commons.android.annotation_processor;

import com.easing.commons.android.format.TextBuilder;

//线程函数调用栈
public class CallingStack {

    public static String getInvokingMethod() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StackTraceElement element = elements[3];
        String description = element.getClassName() + " " + element.getMethodName() + " " + element.getLineNumber();
        return description;
    }

    public static String getCallingStack() {
        TextBuilder builder = TextBuilder.create();
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (int i = 3; i < elements.length; i++)
            builder.append(elements[i]).endLine();
        String description = builder.build();
        return description;
    }

}
