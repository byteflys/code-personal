package com.easing.commons.android.format;

public class PatternText {
    //密码校验规则()
    public static final String EDIT_PASSWORD = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\\\W_!@#$%^&*`~()-+=.]+$)(?![a-z0-9]+$)(?![a-z\\\\W_!@#$%^&*`~()-+=.]+$)(?![0-9\\\\W_!@#$%^&*`~()-+=.]+$)(?![a-zA-Z0-9]+$)(?![a-zA-Z\\\\W_!@#$%^&*`~()-+=.]+$)(?![a-z0-9\\\\W_!@#$%^&*`~()-+=.]+$)(?![0-9A-Z\\\\W_!@#$%^&*`~()-+=.]+$)[a-zA-Z0-9\\\\W_!@#$%^&*`~()-+=.]{6,32}$";
    //http
    public static final String HTTP_STR = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";

}
