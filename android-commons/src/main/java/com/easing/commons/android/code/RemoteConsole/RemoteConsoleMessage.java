package com.easing.commons.android.code.RemoteConsole;

import com.easing.commons.android.data.Jsonable;
import com.easing.commons.android.time.Times;

import java.util.LinkedList;

@SuppressWarnings("all")
public class RemoteConsoleMessage implements Jsonable {

    public static final transient int LEVEL_NONE = 0;
    public static final transient int LEVEL_INFO = 0x01 << 1;
    public static final transient int LEVEL_DEBUG = 0x01 << 2;
    public static final transient int LEVEL_WARN = 0x01 << 3;
    public static final transient int LEVEL_ERROR = 0x01 << 4;
    public static final transient int LEVEL_DETAIL = 0x01 << 5;
    public static final transient int LEVEL_ALL = LEVEL_INFO | LEVEL_DEBUG | LEVEL_WARN | LEVEL_ERROR | LEVEL_DETAIL;

    public static final transient int STYLE_NONE = 0;
    public static final transient int STYLE_BOLD = 0x01 << 0;
    public static final transient int STYLE_ITALIC = 0x01 << 1;
    public static final transient int STYLE_UNDER_LINE = 0x01 << 2;
    public static final transient int STYLE_THROUGH_LINE = 0x01 << 3;
    public static final transient int STYLE_COLORED = 0x01 << 4;

    public static final transient String COLOR_TAG = "666666";
    public static final transient String COLOR_INFO = "333333";
    public static final transient String COLOR_DEBUG = "0066FF";
    public static final transient String COLOR_WARN = "CC6600";
    public static final transient String COLOR_ERROR = "CC3300";
    public static final transient String COLOR_DETAIL = "009900";

    public String clientID = "";
    public String project = "";
    public String tag = "";
    public Integer level = LEVEL_NONE;
    public Long time = Times.millisOfNow();
    public LinkedList<Element> elements = new LinkedList();

    public static class Element {

        public String text = "";
        public Integer style = LEVEL_NONE;
        public String color = COLOR_INFO;

        //判断是否加粗
        public boolean bold() {
            return (style & STYLE_BOLD) > 0;
        }

        //判断是否斜体
        public boolean italic() {
            return (style & STYLE_ITALIC) > 0;
        }

        //判断是否带下划线
        public boolean underline() {
            return (style & STYLE_UNDER_LINE) > 0;
        }

        //判断是否带删除线
        public boolean throughLine() {
            return (style & STYLE_THROUGH_LINE) > 0;
        }

        //判断是否使用自定义颜色
        public boolean colored() {
            return (style & STYLE_COLORED) > 0;
        }
    }

    private Integer tempStyle = LEVEL_NONE;
    private String tempColor = COLOR_INFO;

    //创建一个新消息
    public static RemoteConsoleMessage create(String clientID, String project, String tag, Integer level) {
        RemoteConsoleMessage message = new RemoteConsoleMessage();
        message.clientID = clientID;
        message.project = project;
        message.tag = tag;
        message.level = level;
        return message;
    }

    //加粗
    public RemoteConsoleMessage bold(boolean bold) {
        if (bold)
            tempStyle |= STYLE_BOLD;
        else
            tempStyle &= ~STYLE_BOLD;
        return this;
    }

    //斜体
    public RemoteConsoleMessage italic(boolean italic) {
        if (italic)
            tempStyle |= STYLE_ITALIC;
        else
            tempStyle &= ~STYLE_ITALIC;
        return this;
    }

    //下划线
    public RemoteConsoleMessage underline(boolean underline) {
        if (underline)
            tempStyle |= STYLE_UNDER_LINE;
        else
            tempStyle &= ~STYLE_UNDER_LINE;
        return this;
    }

    //删除线
    public RemoteConsoleMessage throughLine(boolean throughLine) {
        if (throughLine)
            tempStyle |= STYLE_THROUGH_LINE;
        else
            tempStyle &= ~STYLE_THROUGH_LINE;
        return this;
    }

    //彩色
    public RemoteConsoleMessage colored(boolean colored) {
        if (colored)
            tempStyle |= STYLE_COLORED;
        else
            tempStyle &= ~STYLE_COLORED;
        return this;
    }

    //设置文字颜色
    public RemoteConsoleMessage color(String color) {
        tempColor = color;
        return this;
    }

    //添加一段文本
    public RemoteConsoleMessage text(Object text) {
        Element element = new Element();
        element.text = String.valueOf(text);
        element.style = tempStyle;
        element.color = element.colored() ? tempColor : getDefaultColor();
        elements.addLast(element);
        return this;
    }

    //获取格式化的时间
    public String getFormatedTime() {
        return Times.formatDate(time);
    }

    //获取等级对应的默认颜色
    public String getDefaultColor() {
        if (level == LEVEL_INFO)
            return COLOR_INFO;
        if (level == LEVEL_DEBUG)
            return COLOR_DEBUG;
        if (level == LEVEL_WARN)
            return COLOR_WARN;
        if (level == LEVEL_ERROR)
            return COLOR_ERROR;
        if (level == LEVEL_DETAIL)
            return COLOR_DETAIL;
        return COLOR_INFO;
    }

    //获取文字颜色
    public String color(Element element) {
        if (element.colored())
            return element.color;
        return getDefaultColor();
    }

    //发送消息
    synchronized public void send() {
        if (!RemoteConsole.enabled)
            return;
        if (RemoteConsole.impl == null)
            return;
        RemoteConsole.impl.send(this);
    }
}

