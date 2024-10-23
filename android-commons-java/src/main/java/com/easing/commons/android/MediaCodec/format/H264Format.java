package com.easing.commons.android.MediaCodec.format;

public class H264Format {

    //从流数据中查找H264的header
    public static int findH264Header(byte[] buffer, int length) {
        //两个header之间的字节，即为一帧的数据
        //因此我们实际是跳过第一个header，查找第二个header的位置
        final int firsetOffset = 512;
        for (int index = firsetOffset; index < length; index++)
            if (isH264Header(buffer, index))
                return index;
        return 0;
    }

    //判断当前字节是不是H264的header
    public static boolean isH264Header(byte[] buffer, int index) {
        //00 00 00 01
        if (buffer[index] == 0 && buffer[index + 1] == 0 && buffer[index + 2] == 0 && buffer[3] == 1)
            return true;
        //00 00 01
        if (buffer[index] == 0 && buffer[index + 1] == 0 && buffer[index + 2] == 1)
            return true;
        return false;
    }
}
