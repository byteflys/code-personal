package com.easing.commons.android.bin;

import com.github.snksoft.crc.CRC;

@SuppressWarnings("all")
public class CRC16 {

    //通过CRC16-MODBUS算法生成字节校验码
    public static long modbus(byte[] base) {
        long crc = CRC.calculateCRC(new CRC.Parameters(16, 0x8005, 0xFFFF, true, true, 0x0000), base);
        return crc;
    }

    //通过CRC16-IBM算法生成字节校验码
    public static long ibm(byte[] base) {
        long crc = CRC.calculateCRC(new CRC.Parameters(16, 0x8005, 0x0000, true, true, 0x0000), base);
        return crc;
    }

    //CRC校验码高低位互换
    public static long reverse(long crc) {
        long reversed = ((crc & 0xFF) << 8) + (crc >> 8);
        return reversed;
    }

    //CRC校验码转十六进制字符串
    public static String toHex(long crc) {
        byte b1 = (byte) (crc >> 8);
        byte b2 = (byte) crc;
        String hex = Bytes.byteToHex(b1) + Bytes.byteToHex(b2);
        return hex;
    }

    //通过CRC16-MODBUS算法校验CRC是否正确
    public static boolean checkByModbus(byte[] baseAndCrc) {
        int length = baseAndCrc.length;
        byte[] base = new byte[length - 2];
        System.arraycopy(baseAndCrc, 0, base, 0, length - 2);
        long expectedCRC = modbus(base);
        long actualCRC = ((baseAndCrc[length - 2] & 0xFFL) << 8) + (baseAndCrc[length - 1] & 0xFFL);
        String s1 = Long.toHexString(baseAndCrc[length - 2] & 0xFFL );
        String s2 = Long.toHexString(baseAndCrc[length - 1] & 0xFFL);
        boolean pass = actualCRC == expectedCRC;
        return pass;
    }

    //通过CRC16-MODBUS-REVERSED算法校验CRC是否正确
    public static boolean checkByModbusReversed(byte[] baseAndCrc) {
        int length = baseAndCrc.length;
        byte[] base = new byte[length - 2];
        System.arraycopy(baseAndCrc, 0, base, 0, length - 2);
        long expectedCRC = modbus(base);
        expectedCRC = reverse(expectedCRC);
        long actualCRC = ((baseAndCrc[length - 2] & 0xFFL) << 8) + (baseAndCrc[length - 1] & 0xFFL);
        String s1 = Long.toHexString(baseAndCrc[length - 2] & 0xFFL );
        String s2 = Long.toHexString(baseAndCrc[length - 1] & 0xFFL);
        boolean pass = actualCRC == expectedCRC;
        return pass;
    }
}

