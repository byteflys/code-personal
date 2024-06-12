package com.easing.commons.android.data;

/**
 * 后台Result.code编码
 */
public enum CustomizeErrorCode {
    SUCCESS(true, 200, "成功"),
    PARAM_INVILAD(false, 400, "传入参数有误"),
    READABLE_INVILAD(false, 400, "传入参数解释有误"),
    PATHVARIABLE_INVILAD(false, 400, "路径参数必填"),
    AUTHORIZATION_INVILAD(false, 401, "token过期,请重新登陆"),
    REQUEST_NOT_FIND(false, 404, "请求地址不存在"),
    REQUEST_INVILAD(false, 405, "请求方式有误"),
    MEDIATYPE_ACCEPTABLE_INVILAD(false, 406, "传入accept有误"),
    REQUEST_CONFLICT(true, 409, "被请求的资源的当前状态之间存在冲突"),
    MEDIATYPE_SUPPORTED_INVILAD(false, 415, "传入content-type有误"),
    FAILURE(true, 500, "系统错误"),
    SYSTEM_ERROR(false, 500, "系统错误"),
    PARAM_ERROR(true, 4000, "参数错误"),
    PARAM_UNIQUENESS(true, 4001, "参数已存在"),
    PARAM_FORBIDDEN(true, 4002, "参数禁止"),
    LOGIN_FORBIDDEN(true, 4030, "账号或密码错误"),
    CONTROL_FORBIDDEN(true, 4031, "当前状态禁止此操作"),
    VERSION_CONFLICT(true, 4040, "版本冲突"),
    UPLOAD_FILE_FAILURE(true, 4090, "文件保存失败"),
    REQUEST_ERROR(true, 5000, "请求其他后台系统失败"),
    CUSTOMIZE_ERROR(true, 9999, "自定义错误");

    private Boolean success;
    private Integer code;
    private String message;

    private CustomizeErrorCode(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    CustomizeErrorCode() {

    }

    public Boolean getSuccess() {
        return this.success;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static CustomizeErrorCode getCustomizeErrorCode(Integer code) {
        CustomizeErrorCode[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            CustomizeErrorCode e = var1[var3];
            if (e.code.equals(code)) {
                return e;
            }
        }

        return FAILURE;
    }
}
