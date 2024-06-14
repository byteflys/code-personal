package com.easing.commons.android.http;

import com.easing.commons.android.annotation_processor.Reflection;
import com.easing.commons.android.data.Result;

import okhttp3.Response;

public interface ResponseParser<T> {

    Result<T> parse(Response response);

    public static <K> Result<K> parse(Response response, Class<? extends ResponseParser<K>> parserClass) {
        ResponseParser<K> parser = Reflection.newInstance(parserClass);
        Result<K> result = parser.parse(response);
        return result;
    }
}
