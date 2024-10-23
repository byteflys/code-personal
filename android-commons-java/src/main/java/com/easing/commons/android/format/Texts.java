package com.easing.commons.android.format;

import static com.easing.commons.android.format.PatternText.HTTP_STR;

import android.text.InputFilter;
import android.widget.EditText;

import com.easing.commons.android.code.Console;
import com.easing.commons.android.time.Times;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import lombok.SneakyThrows;

//文本工具
//有空优化排序一下
@SuppressWarnings("all")
public class Texts {

    private static final HanyuPinyinOutputFormat defaultFormat;

    static {
        defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    public static String random() {
        return random(false, false);
    }

    public static String unique() {
        return Texts.random() + "-" + Texts.random() + "-" + Times.millisOfNow();
    }

    public static String random(boolean withConcatChar, boolean toUpperCase) {
        String uuid = UUID.randomUUID().toString();
        if (!withConcatChar)
            uuid = uuid.replaceAll("-", "");
        if (toUpperCase)
            uuid = uuid.toUpperCase();
        return uuid;
    }

    public static String[] collectionToStringArray(Collection collection) {
        String[] array = new String[collection.size()];
        int index = 0;
        for (Object item : collection)
            array[index++] = (item == null) ? "NULL" : item.toString();
        return array;
    }

    public static String arrayToString(Object... array) {
        if (array == null) return "NULL";
        else if (array.length == 0) return "[]";

        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(array[0] == null ? "NULL" : array[0].toString());
        for (int i = 1; i < array.length; i++) {
            buffer.append(", ");
            buffer.append(array[i] == null ? "NULL" : array[i].toString());
        }
        buffer.append("]");
        return buffer.toString();
    }

    public static String arrayToString2(Object[][] array) {
        if (array == null) return "NULL";
        else if (array.length == 0) return "[]";

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            buffer.append(arrayToString(array[i]));
            if (i != array.length - 1) buffer.append("\n");
        }
        return buffer.toString();
    }

    public static String arrayToStringWithSplit(String tag, Object... array) {
        if (array == null) return "NULL";
        else if (array.length == 0) return "[]";

        StringBuffer sb = new StringBuffer();
        sb.append(array[0] == null ? "NULL" : array[0].toString());
        for (int i = 1; i < array.length; i++) {
            sb.append(tag);
            sb.append(array[i] == null ? "NULL" : array[i].toString());
        }
        return sb.toString();
    }

    public static String listToString(List objs, String split) {
        if (objs == null || objs.size() == 0) return "";
        StringBuffer buffer = new StringBuffer();
        buffer.append(objs.get(0).toString());
        for (int i = 1; i < objs.size(); i++)
            buffer.append(split).append(objs.get(i));
        return buffer.toString();
    }

    public static String[][] sortArray2(String[][] datas) {
        ArrayList<ArrayList<String>> lists = new ArrayList();
        for (int i = 0; i < datas.length; i++)
            lists.add(new ArrayList(Arrays.asList(datas[i])));
        Collections.sort(lists, (l, r) -> {
            for (int j = 0; j < l.size(); j++)
                if (l.get(j).compareTo(r.get(j)) != 0) return l.get(j).compareTo(r.get(j));
            return 0;
        });

        String[][] newDatas = new String[datas.length][datas.length > 0 ? datas[0].length : 0];
        for (int i = 0; i < newDatas.length; i++)
            for (int j = 0; j < newDatas[i].length; j++)
                newDatas[i][j] = lists.get(i).get(j);

        return newDatas;
    }

    public static String getPinyin(Object o) {
        try {
            String pinyin = PinyinHelper.toHanYuPinyinString(o.toString(), defaultFormat, "", true);
            return pinyin.toLowerCase();
        } catch (Exception e) {
            Console.error(e);
            return "";
        }
    }

    //首字母简拼
    public static String getSimplePinyin(Object o) {
        try {
            String pinyin = "";
            String str = o.toString();
            for (int i = 0; i < str.length(); i++)
                pinyin = pinyin + PinyinHelper.toHanYuPinyinString(str.charAt(i) + "", defaultFormat, "", true).charAt(0);
            return pinyin.toLowerCase();
        } catch (Exception e) {
            Console.error(e);
            return "";
        }
    }

    public static int comparePinyin(Object l, Object r) {
        return getPinyin(l).compareTo(getPinyin(r));
    }

    //对比字符串，null放在最后显示
    public static int compare(String l, String r) {
        if (l == null && r == null)
            return 0;
        if (l == null)
            return 1;
        if (r == null)
            return -1;
        return l.compareTo(r);
    }

    //按拼音对比字符串，数字和字母放在最前，null放在最后显示
    public static int compareByPinyin(String l, String r) {
        if (l == null && r == null)
            return 0;
        if (l == null)
            return 1;
        if (r == null)
            return -1;
        boolean numberAndChar1 = isNumberAndChar(l);
        boolean numberAndChar2 = isNumberAndChar(r);
        if (!numberAndChar1 && !numberAndChar2) {
            String PL = getPinyin(l);
            String PR = getPinyin(r);
            return PL.compareTo(PR);
        }
        if (numberAndChar1 && numberAndChar2)
            return l.compareTo(r);
        if (numberAndChar1)
            return -1;
        if (numberAndChar2)
            return 1;
        return l.compareTo(r);
    }

    //判断字符串是否只包含数字和字母
    public static boolean isNumberAndChar(String text) {
        return text.matches("^[a-z0-9A-Z]+$");
    }

    public static String unicodeToString(String str) {
        StringBuilder sb = new StringBuilder();
        int index = str.indexOf("\\u");
        while (index != -1) {
            sb.append(unicodeToChar(str.substring(index, index + 6)));
            index = str.indexOf("\\u", index + 6);
        }
        return sb.toString();
    }

    public static char unicodeToChar(String str) {
        if (str.length() != 6) throw new RuntimeException("not a unicode string");
        if (!"\\u".equals(str.substring(0, 2))) throw new RuntimeException("not a unicode string");

        int high = Integer.parseInt(str.substring(2, 4), 16) << 8; // 高8位
        int low = Integer.parseInt(str.substring(4, 6), 16); // 低8位
        return (char) (high + low);
    }

    //在已有字符串基础上，连续拼接若干个相同的字符
    public static String append(String base, Object extra, int times) {
        for (int i = 0; i < times; i++)
            base = base + extra.toString();
        return base;
    }

    public static String trim(String str, char c) {
        if (isEmpty(str)) return "";
        char[] chars = str.toCharArray();
        int start = 0;
        int end = chars.length;
        while (chars[start] == c)
            start++;
        while (chars[end - 1] == c)
            end--;
        String substring = str.substring(start, end);
        return substring;
    }

    public static String trimLeft(String str, char c) {
        if (isEmpty(str)) return "";
        char[] chars = str.toCharArray();
        int start = 0;
        int end = chars.length;
        while (chars[start] == c)
            start++;
        String substring = str.substring(start, end);
        return substring;
    }

    public static String trimRight(String str, char c) {
        if (isEmpty(str)) return "";
        char[] chars = str.toCharArray();
        int start = 0;
        int end = chars.length;
        while (chars[end - 1] == c)
            end--;
        String substring = str.substring(start, end);
        return substring;
    }

    //判断字符串是否为空
    public static boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }

    //判断List是否为空
    public static boolean isListNull(List list) {
        if (list != null && list.size() > 0)
            return true;
        return false;
    }

    //判断是不是http网址
    public static boolean isHttpUrl(String url) {
        Pattern pattern = Pattern.compile(HTTP_STR.trim());
        Matcher matcher = pattern.matcher(url.toLowerCase().trim());
        return matcher.matches();
    }

    /**
     * 通过正则判断
     * 目前正则数据异常暂时不做校验
     *
     * @param str   需要判断的字符串
     * @param regex 正则类容
     */
    public static boolean isPattern(String str, String regex) {
      /*  Pattern pattern = Pattern.compile(regex.trim());
        Matcher matcher = pattern.matcher(str.trim());
        return !matcher.matches();*/
        return false;
    }

    /**
     * 设置输入框的最大长度
     * 目前正则数据异常暂时不做校验
     *
     * @param str   需要判断的字符串
     * @param regex 正则类容
     */
    public static void setEditTextMaxLength(EditText view, int length) {
        if (view == null || length < 1)
            return;
        view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});

    }

    /**
     * 通过正则判断
     * 目前正则数据异常暂时不做校验
     *
     * @param str   需要判断的字符串
     * @param regex 正则类容
     */
    public static boolean isPatternAll(String str, String regex) {
        Pattern pattern = Pattern.compile(regex.trim());
        Matcher matcher = pattern.matcher(str.trim());
        return !matcher.matches();
    }

    //判断是不是ip
    public static boolean isIp(String ip) {
        //判断点号格式
        String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        if (!ip.matches(regex)) return false;

        //判断每段数字是否都位于0-255之间
        String[] ipArray = ip.split("\\.");
        for (int i = 0; i < ipArray.length; i++) {
            int number = Integer.parseInt(ipArray[i]);
            if (number > 255) return false;
        }

        return true;
    }

    //判断是不是域名
    public static boolean isDomain(String domain) {
        domain = domain.toLowerCase().trim();
        boolean b1 = domain.matches(".*[a-z]+.*"); //包含字母
        boolean b2 = domain.matches("[a-z0-9]+\\.[a-z0-9]+.*"); //包含点号
        boolean b3 = !domain.contains(".."); //只能包含单个点号
        return b1 && b2 && b3;
    }

    //判断是不是端口
    public static boolean isPort(String port) {
        try {
            int iPort = Integer.valueOf(port);
            return iPort > 0 && iPort <= 65535;
        } catch (Throwable e) {
            return false;
        }
    }

    //将异常的字符串转化为整数
    public static Integer abnormalTextToInt(String text, Integer defaultValue) {
        try {
            return Integer.parseInt(Pattern.compile("[^0-9]").matcher(text).replaceAll("").trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    //判断是否包含子字符串
    public static boolean containsIgnoreCase(String a, String b) {
        if (a == null && b == null)
            return true;
        if (a == null || b == null)
            return false;
        return a.toLowerCase().contains(b.toLowerCase());
    }

    //判断字符串是否相等
    public static boolean equal(String a, String b) {
        if (a == null && b == null)
            return true;
        if (a == null || b == null)
            return false;
        return a.equals(b);
    }

    //判断字符串是否相等
    public static boolean notEqual(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return !a.equals(b);
    }

    //无视大小写，判断字符串a是否以字符串b开头
    public static boolean startWithIgnoreCase(String a, String b) {
        return a.toLowerCase().startsWith(b.toLowerCase());
    }

    //对象转String，保留NULL
    public static String toString(Object object) {
        if (object == null) return null;
        return String.valueOf(object);
    }

    //对象转String，NULL转字符串
    public static String stringify(Object object) {
        if (object == null) return "NULL";
        return String.valueOf(object);
    }

    //解析字符串
    @SneakyThrows
    public static String decode(byte[] encodeBytes, String encode) {
        if (encode == null) encode = "UTF-8";
        return new String(encodeBytes, encode);
    }

    //判断字节是否是JSON字符串
    @SneakyThrows
    public static boolean isJsonString(byte[] bytes) {
        String str = new String(bytes, "UTF-8");
        str = Texts.trim(str, ' ');
        str = Texts.trim(str, '\n');
        str = Texts.trim(str, '\t');
        boolean b1 = str.endsWith("{") && str.endsWith("}");
        boolean b2 = str.endsWith("[") && str.endsWith("]");
        return b1 || b2;
    }

    //判断字节是否是XML字符串
    @SneakyThrows
    public static boolean isXmlString(byte[] bytes) {
        String str = new String(bytes, "UTF-8");
        str = Texts.trim(str, ' ');
        str = Texts.trim(str, '\n');
        str = Texts.trim(str, '\t');
        boolean b = str.endsWith("<") && str.endsWith(">");
        return b;
    }

    //将换行替换为空格
    public static String replaceEndlineWithWhitespace(String str) {
        if (str == null)
            return null;
        str = trim(str, ' ');
        str = str.replaceAll("\\s+", " ");
        return str;
    }

    //格式化字节大小，传入单位为B
    public static String formatByteLength(long speed) {
        if (speed >= 1024L * 1024L * 1024L) {
            double v = speed / 1024D / 1024D / 1024D;
            return Maths.keepFloat(v, 1) + "G";
        }
        if (speed >= 1024L * 1024L) {
            double v = speed / 1024D / 1024D;
            return Maths.keepFloat(v, 1) + "M";
        }
        if (speed >= 1024L) {
            double v = speed / 1024D;
            return Maths.keepFloat(v, 1) + "K";
        }
        return speed + "B";
    }

    //格式化网速，传入单位为B/S
    public static String formatNetworkSpeed(long speed) {
        if (speed >= 1024L * 1024L * 1024L) {
            double v = speed / 1024D / 1024D / 1024D;
            return Maths.keepFloat(v, 1) + "GB/S";
        }
        if (speed >= 1024L * 1024L) {
            double v = speed / 1024D / 1024D;
            return Maths.keepFloat(v, 1) + "MB/S";
        }
        if (speed >= 1024L) {
            double v = speed / 1024D;
            return Maths.keepFloat(v, 1) + "KB/S";
        }
        return speed + "B/S";
    }

    public static String stringForTime(double timeMs) {
        double totalSeconds = timeMs / 1000;

        int seconds = (int) totalSeconds % 60;
        int minutes = (int) (totalSeconds / 60) % 60;
        int hours = (int) totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }
}

