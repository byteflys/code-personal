package com.easing.commons.android.http;

import com.easing.commons.android.annotation_processor.Reflection;
import com.easing.commons.android.data.Result;

public interface BodyParser<T> {

    Result<T> parse(String body);

    public static <K> Result<K> parse(String body, Class<? extends BodyParser<K>> parserClass) {
        BodyParser<K> parser = Reflection.newInstance(parserClass);
        Result<K> result = parser.parse(body);
        return result;
    }
}
