package com.easing.commons.android.http;

import okhttp3.MediaType;

public class HttpMediaTypes {

    public static final MediaType URL_ENCODED_FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    public static final MediaType MULTI_PART_FORM = MediaType.parse("multipart/form-data; charset=utf-8");
    public static final MediaType RAW_TEXT = MediaType.parse("text/plain; charset=utf-8");
    public static final MediaType RAW_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType BINARY = MediaType.parse("application/octet-stream");
}
