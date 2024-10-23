package com.easing.commons.android.format;

import com.easing.commons.android.clazz.Types;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class Maths {

    public static String keepInt(Number num, int n) {
        String format = "0";
        for (int i = 1; i < n; i++)
            format = format + "0";
        return new DecimalFormat(format).format(num);
    }

    //舍去多余小数位，返回String
    public static String keepFloat(String num, int n) {
        String format = "";
        if (n == 0)
            format = "0";
        if (n > 0)
            format = "0.";
        for (int i = 0; i < n; i++)
            format = format + "0";
        BigDecimal bigDecimal = new BigDecimal(num);
        bigDecimal.setScale(n, RoundingMode.HALF_DOWN);
        return new DecimalFormat(format).format(bigDecimal);
    }

    //舍去多余小数位，返回String
    public static String keepFloat(Number num, int n) {
        String format = "";
        if (n == 0)
            format = "0";
        if (n > 0)
            format = "0.";
        for (int i = 0; i < n; i++)
            format = format + "0";
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(num));
        bigDecimal.setScale(n, RoundingMode.HALF_DOWN);
        return new DecimalFormat(format).format(bigDecimal);
    }

    //舍去多余小数位，返回Double
    public static double keepFloat2(Number num, int n) {
        return Types.doubleValue(Maths.keepFloat(num, n));
    }

    public static String formatInteger(int i, String format) {
        format = format == null ? "00" : format;
        return new DecimalFormat(format).format(i);
    }

    //随机生成一个小数
    //默认生成0.0-1.0之间的小数，如果想数值更小，比如0.00-0.10
    public static double randomDouble(Integer keptFloatBit) {
        if (keptFloatBit == null) keptFloatBit = 0;
        double random = Math.abs(new Random().nextDouble() / Math.pow(10, keptFloatBit));
        boolean positive = new Random().nextBoolean();
        return random * (positive ? 1 : -1);
    }

    //随机生成一个整数
    public static int randomInt() {
        return new Random().nextInt(Integer.MAX_VALUE);
    }

    //生成[0,count)间的整数
    public static int randomInt(int count) {
        return new Random().nextInt(count);
    }

    /**
     * 生成[start,end]间的整数
     **/
    public static int randomInt(int start, int end) {
        return start + new Random().nextInt(end - start + 1);
    }

    /**
     * 生成一个N位的整数，并格式化成字符串
     **/
    public static String randomIntWithFixBit(int n) {
        int num = new Random().nextInt((int) Math.pow(10, n));
        return keepInt(num, n);
    }

    //取绝对值
    public static int abs(int number) {
        return Math.abs(number);
    }

    //取绝对值
    public static float abs(float number) {
        return Math.abs(number);
    }

    //取绝对值
    public static double abs(double number) {
        return Math.abs(number);
    }

    //小数向上进位，视为整数
    public static int upperInt(double num) {
        return (int) Math.ceil(num);
    }

    //小数向下退位，视为整数
    public static int floorInt(double num) {
        return (int) Math.floor(num);
    }

    //Number转Integer
    public static Integer intValue(Number num) {
        return num.intValue();
    }

    //Number转Double
    public static Double doubleValue(Number num) {
        return num.doubleValue();
    }

    //Number转Long
    public static Long longValue(Number num) {
        return num.longValue();
    }

    //Number转Long
    public static Integer intValue(Object num) {
        return ((Number) num).intValue();
    }

    //Number转Long
    public static Long longValue(Object num) {
        return ((Number) num).longValue();
    }

    /**
     * 格式化字节数
     **/
    public static String formatBytes(long bytes) {
        float num;
        String unit;
        if (bytes >= 1024 * 1024 * 1024) {
            num = bytes / 1024f / 1024f / 1024f;
            unit = "GB";
        } else if (bytes >= 1024 * 1024) {
            num = bytes / 1024f / 1024f;
            unit = "MB";
        } else if (bytes >= 1024) {
            num = bytes / 1024f;
            unit = "KB";
        } else {
            num = bytes;
            unit = "B";
        }
        return new DecimalFormat("0.#").format(num) + unit;
    }

    public static String formatBytePrgoress(long finish, long total) {
        return Maths.formatBytes(finish) + "/" + Maths.formatBytes(total);
    }

    public static String hexToString(int value) {
        return Integer.toHexString(value).toUpperCase();
    }

    public static int hexStringToInt(String value) {
        return Integer.parseInt(value, 16);
    }

    public static boolean isInteger(Object o) {
        try {
            Integer.parseInt(o.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNumber(Object o) {
        try {
            Double.parseDouble(o.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //小数转百分比
    public static String floatToPercent(Number rate) {
        return floatToPercent(rate, 2);
    }

    //小数转百分比
    public static String floatToPercent(Number rate, int dotBits) {
        if (dotBits <= 0)
            return (int) (rate.doubleValue() * 100) + "%";
        TextBuilder builder = TextBuilder.create();
        for (int i = 0; i < dotBits; i++)
            builder.append("0");
        String dotFormat = builder.build();
        DecimalFormat formater = new DecimalFormat("0." + dotFormat);
        String formatString = formater.format(rate.doubleValue() * 100) + "%";
        return formatString;
    }

    //判断两个整数是否相等
    public static boolean equal(Integer a, Integer b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.intValue() == b.intValue();
    }

    //按基数取整
    //比如基数为5，那么value只能取0，5，10，15这样的整倍数值
    public static int roundByBase(double value, Number base, boolean down) {
        double model = value % base.intValue();
        if (model == 0) return (int) value;
        if (down)
            return (int) (value - model);
        return (int) (value - model + base.intValue());
    }

    /**
     * 保留2位小数
     *
     * @param val
     * @return
     */
    public static String rahToStr(double val) {
        if (!Double.isNaN(val) && val != Double.NEGATIVE_INFINITY && val != Double.POSITIVE_INFINITY) {
            BigDecimal bd = new BigDecimal(val);
            val = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(val);
        }
        return "0.00";
    }
}
