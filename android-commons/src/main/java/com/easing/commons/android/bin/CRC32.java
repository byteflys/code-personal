package com.easing.commons.android.bin;

public class CRC32 {

    //通过CRC32-MPEG2算法生成字节校验码
    public static String MPEG2(byte[] data) {
        int crc = 0xFFFFFFFF;
        for (int i = 0; i < data.length; i++) {
            crc ^= data[i] << 24;
            for (int j = 0; j < 8; ++j) {
                if ((crc & 0x80000000) != 0)
                    crc = (crc << 1) ^ 0x04C11DB7;
                else
                    crc <<= 1;
            }
        }
        String CRC32 = Bytes.signedIntToHex(crc, 4);
        return CRC32;
    }

}
