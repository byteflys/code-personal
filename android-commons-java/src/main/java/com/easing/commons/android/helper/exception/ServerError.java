package com.easing.commons.android.helper.exception;

public class ServerError extends BizException {

    public static ServerError of(Exception e) {
        ServerError serverError = new ServerError(e);
        return serverError;
    }

    public static ServerError of(String message) {
        ServerError serverError = new ServerError(message);
        return serverError;
    }

    protected ServerError(Exception e) {
        super(e);
    }

    protected ServerError(String message) {
        super(message);
    }
}
