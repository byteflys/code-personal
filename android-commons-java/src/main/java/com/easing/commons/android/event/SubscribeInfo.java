package com.easing.commons.android.event;

import java.lang.reflect.Method;

public class SubscribeInfo {

    Object invokeObject;
    Method invokeMethod;

    String[] eventTypes;
    Class[] paramTypes;

    ThreadMode threadMode;
}
