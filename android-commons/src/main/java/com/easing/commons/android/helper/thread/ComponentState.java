package com.easing.commons.android.helper.thread;

//控制线程自动结束的标志位
public class ComponentState {

    public enum StateValue {
        ALIVE, DISPOSED
    }

    public StateValue value = StateValue.ALIVE;

    private ComponentState() {
    }

    public static ComponentState create() {
        return new ComponentState();
    }

    public boolean isAlive() {
        return value == StateValue.ALIVE;
    }
}
