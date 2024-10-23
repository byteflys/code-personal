package com.easing.commons.android.bin;

import com.easing.commons.android.format.Texts;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import lombok.SneakyThrows;

//本字节工具，如无特别说明，均为大端模式
//即字节的高位，放在byte[]或String的低index存储，和阅读习惯一致
//需要大小端转换时，会引入一个beMode变量，true为大端，false为小端
@SuppressWarnings("all")
public class Bytes {

    //显示字节对应的实际位数据
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    //显示字节对应的实际位数据
    public static String byteToBit(byte[] bytes) {
        String bitStr = "";
        for (int i = 0; i < bytes.length; i++)
            bitStr += byteToBit(bytes[i]);
        return bitStr;
    }

    //无符号整数 转 无符号二进制字符
    public static String intToBin(int num) {
        if (num < 0)
            throw new RuntimeException("num should not below 0");
        String binStr = Integer.toBinaryString(num);
        int length = binStr.length();
        if (length < 8)
            for (int i = 0; i < 8 - length; i++)
                binStr = "0" + binStr;
        return binStr;
    }

    //无符号整数 转 无符号十六进制字符
    public static String intToHex(int num) {
        if (num < 0)
            throw new RuntimeException("num should not be below 0");
        String hexStr = Integer.toHexString(num);
        if (hexStr.length() % 2 != 0)
            hexStr = "0" + hexStr;
        return hexStr.toUpperCase();
    }

    //无符号整数 转 无符号十六进制字符
    public static String longToHex(long num) {
        if (num < 0)
            throw new RuntimeException("num should not be below 0");
        String hexStr = Long.toHexString(num);
        if (hexStr.length() % 2 != 0)
            hexStr = "0" + hexStr;
        return hexStr.toUpperCase();
    }

    //无符号整数 转 无符号十六进制字符 固定位数
    public static String longToHex(long num, int byteCount) {
        String hex = Bytes.longToHex(num);
        int originLength = hex.length();
        for (int i = 0; i < byteCount * 2 - originLength; i++)
            hex = "0" + hex;
        return hex;
    }

    //无符号整数 转 无符号十六进制字符 固定位数
    public static String intToHex(int num, int byteCount) {
        String hex = Bytes.intToHex(num);
        int originLength = hex.length();
        for (int i = 0; i < byteCount * 2 - originLength; i++)
            hex = "0" + hex;
        return hex;
    }

    //有符号整数 转 无符号十六进制字符 固定位数
    public static String signedIntToHex(int num, int byteCount) {
        String hex = Integer.toHexString(num);
        int originLength = hex.length();
        for (int i = 0; i < byteCount * 2 - originLength; i++)
            hex = "0" + hex;
        return hex.toUpperCase();
    }

    //无符号整数 转 无符号字节
    public static byte intToByte(int num) {
        if (num < 0 || num > 255)
            throw new RuntimeException("num must between 0 and 255");
        return (byte) num;
    }

    //无符号二进制字符 转 无符号整数
    public static int binToInt(String binStr) {
        if (binStr.length() > 8)
            throw new RuntimeException("length of bin string should not be above 8");
        return Integer.valueOf(binStr, 2);
    }

    //无符号二进制字符 转 无符号字节
    public static byte binToByte(String binStr) {
        return intToByte(binToInt(binStr));
    }

    //无符号十六进制字符 转 无符号整数
    public static int hexToInt(String hex) {
        hex = hex.replaceAll("[ ]", "");
        return Integer.valueOf(hex, 16);
    }

    //无符号十六进制字符 转 无符号字节
    public static byte hexToByte(String hexStr) {
        return intToByte(hexToInt(hexStr));
    }

    //有符号字节 转 无符号整数
    public static int byteToInt(byte numByte) {
        return numByte & 0xff;
    }

    //有符号字节 转 无符号二进制字符
    public static String byteToBin(byte numByte) {
        return intToBin(byteToInt(numByte));
    }

    //有符号字节 转 无符号十六进制字符
    public static String byteToHex(byte numByte) {
        String hexStr = intToHex(byteToInt(numByte));
        if (hexStr.length() == 1)
            hexStr = "0" + hexStr;
        return hexStr;
    }

    //无符号整数 转 无符号字节集
    public static byte[] intToByteArray(int num) {
        if (num < 0)
            throw new RuntimeException("num should not be below 0");
        if (num == 0)
            return new byte[]{0};
        int bits = 0;
        while (Math.pow(2, ++bits) <= num)
            continue;
        if (bits % 8 != 0)
            bits = bits + 8 - bits % 8;
        int length = bits / 8;
        byte[] bytes = new byte[length];
        for (int i = length - 1; i >= 0; i--)
            bytes[length - 1 - i] = (byte) (num >> (i * 8));
        return bytes;
    }

    //无符号二进制字符 转 无符号字节集
    public static byte[] binToByteArray(String binStrs) {
        if (binStrs.length() % 8 != 0)
            binStrs = Texts.append(binStrs, "0", 8 - binStrs.length() % 8);
        int size = binStrs.length() / 8;
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++)
            bytes[i] = Bytes.binToByte("" + binStrs.substring(2 * i, 2 * i + 8));
        return bytes;
    }

    //无符号十六进制字符 转 无符号字节集
    public static byte[] hexToByteArray(String hexStrs) {
        hexStrs = hexStrs.replaceAll("[ ]", "");
        if (hexStrs.length() % 2 != 0)
            hexStrs = 0 + hexStrs;
        int size = hexStrs.length() / 2;
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++)
            bytes[i] = Bytes.hexToByte("" + hexStrs.charAt(2 * i) + hexStrs.charAt(2 * i + 1));
        return bytes;
    }

    //无符号字节集 转 无符号二进制字符
    public static String byteArrayToBin(byte[] bytes) {
        String binStr = "";
        for (int i = 0; i < bytes.length; i++)
            binStr = binStr + byteToBin(bytes[i]) + " ";
        return binStr.trim().replaceAll("(( 00000000)+)$", "");
    }

    //无符号字节集 转 无符号十六进制字符
    public static String byteArrayToHex(byte[] bytes) {
        String hexStr = "";
        for (int i = 0; i < bytes.length; i++)
            hexStr = hexStr + byteToHex(bytes[i]) + " ";
        return hexStr.trim().replaceAll("(( 00)+)$", "");
    }

    //无符号字节集 转 无符号二进制字符
    public static String byteArrayToBin(byte[] bytes, String split) {
        String binStr = "";
        for (int i = bytes.length - 1; i >= 0; i--) {
            binStr = binStr + byteToBin(bytes[i]);
            if (i != 0 && split != null)
                binStr = binStr + split;
        }
        return binStr;
    }

    //无符号字节集 转 无符号十六进制字符
    public static String byteArrayToHex(byte[] bytes, String split) {
        String hexStr = "";
        for (int i = bytes.length - 1; i >= 0; i--) {
            hexStr = hexStr + byteToHex(bytes[i]);
            if (i != 0 && split != null)
                hexStr = hexStr + split;
        }
        return hexStr;
    }

    //按位左移
    public static long LMove(long value, int bit) {
        return value << bit;
    }

    //按位右移
    public static long RMove(long value, int bit) {
        return value >> bit;
    }

    //字节 高低位互换
    public static byte[] reverse(byte[] bytes) {
        byte[] reverse = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            reverse[i] = bytes[bytes.length - i - 1];
        return reverse;
    }

    //十六进制字符串 高低位互换
    public static String reverse(String hex) {
        hex = hex.toUpperCase().replaceAll("[ ]", "");
        char[] reverse = new char[hex.length()];
        int index = 0;
        for (int i = hex.length() - 2; i >= 0; i -= 2) {
            reverse[index] = hex.charAt(i);
            reverse[index + 1] = hex.charAt(i + 1);
            index += 2;
        }
        String reverseHex = "";
        for (int i = 0; i < reverse.length; i += 2)
            reverseHex = reverseHex + reverse[i] + reverse[i + 1] + " ";
        return reverseHex.trim();
    }

    //十六进制字符串 大端转小端 固定字节数
    public static String toLeMode(String beHex, int byteCount) {
        beHex = beHex.toUpperCase().replaceAll("[ ]", "");
        int lostByteCount = byteCount * 2 - beHex.length();
        for (int i = 0; i < lostByteCount; i += 2)
            beHex = "00" + beHex;
        return reverse(beHex);
    }

    //十六进制字符串 小端转大端 固定字节数
    public static String toBeMode(String leHex, int byteCount) {
        leHex = leHex.toUpperCase().replaceAll("[ ]", "");
        int lostByteCount = byteCount * 2 - leHex.length();
        for (int i = 0; i < lostByteCount; i += 2)
            leHex = leHex + "00";
        return reverse(leHex);
    }

    //补足小端空位
    public static String fillLeHex(String leHex, int byteCount) {
        for (int i = leHex.length() / 2; i < byteCount; i++)
            leHex = leHex + " 00";
        return leHex;
    }

    //补足大端空位
    public static String fillBeHex(String beHex, int byteCount) {
        for (int i = beHex.length() / 2; i < byteCount; i++)
            beHex = "00 " + beHex;
        return beHex;
    }

    //获取字节数
    public static int byteCount(String hex) {
        hex = hex.replaceAll("[ ]", "");
        return hex.length() / 2;
    }

    //格式化十六进制字符串
    public static String formatHex(String hex) {
        hex = hex.replaceAll("[ ]", "");
        if (hex.length() % 2 != 0)
            hex = "0" + hex;
        String formated = "";
        for (int i = 0; i < hex.length(); i += 2)
            formated = formated + hex.charAt(i) + hex.charAt(i + 1) + " ";
        return formated.trim().toUpperCase();
    }

    //截取指定字节，[start，end)
    public static String sub(String hex, int start, int end) {
        hex = formatHex(hex).replaceAll("[ ]", "");
        String sub = "";
        for (int i = start; i < end; i++)
            sub = sub + hex.charAt(2 * i) + hex.charAt(2 * i + 1);
        return formatHex(sub);
    }

    //截取指定字节中存储的数值，[start，end)
    public static int subInt(String hex, int start, int end, boolean reverse) {
        String sub = sub(hex, start, end);
        if (reverse)
            sub = reverse(sub);
        return hexToInt(sub);
    }

    //查找target字节片段在buffer字节集中的位置
    public static int indexOf(byte[] buffer, int startIndex, int... target) {
        int count = target.length;
        for (int i = startIndex; i < buffer.length - count; i++) {
            boolean match = true;
            for (int j = 0; j < count; j++)
                if (buffer[i + j] != target[j]) match = false;
            if (match) return i;
        }
        return -1;
    }

    //截取字节数组的指定位置，[start，end)
    public static byte[] sub(byte[] buffer, int start, int end) {
        return Arrays.copyOfRange(buffer, start, end);
    }

    //截取字节数组的前若干个字节
    public static byte[] subFront(byte[] buffer, int length) {
        return Arrays.copyOf(buffer, length);
    }

    //合并若干个字节数组
    @SneakyThrows
    public static byte[] concat(byte[]... byteArrays) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (byte[] byteArray : byteArrays)
            bos.write(byteArray);
        byte[] concatBytes = bos.toByteArray();
        bos.close();
        return concatBytes;
    }

    //字节拷贝
    public static byte[] copy(byte[] src, int offset, int length) {
        byte[] dst = new byte[length];
        System.arraycopy(src, offset, dst, 0, length);
        return dst;
    }

    //判断字节是否相等
    public static boolean equal(byte b, int i) {
        if (i < 0 || i > 0xFF)
            throw new RuntimeException("invalid byte");
        boolean ret = b == (byte) i;
        return ret;
    }
}

