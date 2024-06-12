package com.easing.commons.android.value.identity;

import com.easing.commons.android.format.Maths;
import com.easing.commons.android.helper.exception.BizException;

import java.util.Random;
import java.util.Vector;

//常用事件或消息的ID标识
public class Codes {

    private static final Vector<Integer> codes = new Vector();

    public static final int CODE_BACKGROUND_RUNNING = 10001;
    public static final int CODE_BACKGROUND_LOCATION = 10002;
    public static final int CODE_PULL_ALIVE_JOB = 10003;
    public static final int CODE_AI_IMAGE_SELECT = 10004; //ai识别图片选择
    public static final int CODE_AI_IMAGE_COPE = 10005;//ai识别图片剪切
    public static final int CODE_QR_IMAGE_COPE = 10006;//扫描二维码成功后的回调

    public static final int CODE_PICK_FILE = randomCode();
    public static final int CODE_PICK_IMAGE = randomCode();
    public static final int CODE_IMAGE_CAPTURE = randomCode();
    public static final int CODE_VIDEO_CAPTURE = randomCode();
    public static final int CODE_AUDIO_CAPTURE = randomCode();

    static {
        codes.add(CODE_BACKGROUND_RUNNING);
        codes.add(CODE_BACKGROUND_LOCATION);
        codes.add(CODE_PULL_ALIVE_JOB);
    }

    //随机生成一个code，1-9999保留给用户自己使用
    public static int randomCode() {
        int code = Maths.randomInt(10000, 60000);
        code = codes.contains(code) ? randomCode() : code;
        codes.add(code);
        return code;
    }

    //消费一个code，如果已被使用则报错
    public static void consume(Integer code) {
        if (codes.contains(code))
            throw BizException.of("code is already used");
        codes.add(code);
    }
}
