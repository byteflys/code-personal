package com.easing.commons.android.format;

import com.easing.commons.android.functional.Condition;

//文本拼接工具
public class TextBuilder {

    StringBuilder builder = new StringBuilder();

    private TextBuilder() {
    }

    public static TextBuilder create() {
        return new TextBuilder();
    }

    public TextBuilder append(String text) {
        builder.append(text);
        return this;
    }

    public TextBuilder append(Object text) {
        builder.append(text);
        return this;
    }

    public TextBuilder appendIf(Condition condition, Object text) {
        if (condition.decide())
            append(text);
        return this;
    }

    public TextBuilder appendIfElse(Condition condition, Object text1, Object text2) {
        if (condition.decide())
            append(text1);
        else
            append(text2);
        return this;
    }

    public TextBuilder space() {
        space(1);
        return this;
    }

    public TextBuilder space(int count) {
        for (int i = 0; i < count; i++)
            builder.append(" ");
        return this;
    }

    public TextBuilder endLine() {
        builder.append("\n");
        return this;
    }

    public String build() {
        String str = builder.toString();
        builder.setLength(0);
        return str;
    }
}
