package com.easing.commons.android.format;

@SuppressWarnings("all")
public class Replacer {

    String text = "";

    public static Replacer from(String text) {
        Replacer replacer = new Replacer();
        replacer.text = text;
        return replacer;
    }

    public Replacer replace(String regex, String replacement) {
        if (regex != null && replacement != null)
            text = text.replaceAll(regex, replacement);
        return this;
    }

    public String result() {
        return text;
    }
}

