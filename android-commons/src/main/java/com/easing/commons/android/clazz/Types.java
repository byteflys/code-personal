package com.easing.commons.android.clazz;

@SuppressWarnings("all")
public class Types {

    //判断是不是基本类型
    public static boolean isBasicType(Object object) {
        if (object instanceof Integer) return true;
        if (object instanceof Short) return true;
        if (object instanceof Long) return true;
        if (object instanceof Float) return true;
        if (object instanceof Double) return true;
        if (object instanceof Byte) return true;
        if (object instanceof Boolean) return true;
        if (object instanceof String) return true;
        return false;
    }

    //任意类型转String
    public static String stringValue(Object object) {
        if (object == null)
            return null;
        return String.valueOf(object);
    }

    //任意类型转Integer
    public static Integer intValue(Object object) {
        if (object == null)
            return null;
        if (object instanceof String) {
            String value = object.toString();
            if (value.isEmpty()) return null;
            return Double.valueOf(value).intValue();
        }
        if (object instanceof Number) {
            Number value = (Number) object;
            return value.intValue();
        }
        return null;
    }

    //任意类型转Float
    public static Float floatValue(Object object) {
        if (object == null)
            return null;
        if (object instanceof String) {
            String value = object.toString();
            if (value.isEmpty()) return null;
            return Float.valueOf(value);
        }
        if (object instanceof Number) {
            Number value = (Number) object;
            return value.floatValue();
        }
        return null;
    }

    //任意类型转Double
    public static Double doubleValue(Object object) {
        if (object == null)
            return null;
        if (object instanceof String) {
            String value = object.toString();
            if (value.isEmpty()) return null;
            return Double.valueOf(value);
        }
        if (object instanceof Number) {
            Number value = (Number) object;
            return value.doubleValue();
        }
        return null;
    }

    //任意类型转Boolean
    public static Boolean boolValue(Object object) {
        if (object == null)
            return null;
        if (object instanceof String) {
            String value = object.toString();
            if (value.isEmpty()) return null;
            return Boolean.valueOf(value);
        }
        if (object instanceof Boolean)
            return (Boolean) object;
        return null;
    }

    //包装型整数转普通整数
    public static int numberToInt(Number number) {
        if (number == null)
            return 0;
        return number.intValue();
    }
}

