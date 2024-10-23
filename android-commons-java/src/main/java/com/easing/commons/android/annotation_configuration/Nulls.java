package com.easing.commons.android.annotation_configuration;

import com.easing.commons.android.format.Texts;

//由于注解中不允许使用null，用一个变量来代表空值
public class Nulls {

    public static final Class nullClass = Nulls.class;

    public static final Integer nullInt = Integer.MIN_VALUE;

    public static final Long nullLong = Long.MIN_VALUE;

    public static final Double nullDouble = Double.MIN_VALUE;

    public static final String nullString = Texts.random();

}
