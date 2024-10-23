package com.easing.commons.android.value.bean;


public class DelayedControlData<T> {
    private T valueData;
    private String showText;
    private Boolean isSelect;

    public DelayedControlData(T valueData, String showText) {
        this.valueData = valueData;
        this.showText = showText;
    }

    public Boolean isSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }

    public T getValueData() {
        return valueData;
    }

    public void setValueData(T valueData) {
        this.valueData = valueData;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }

    public DelayedControlData(T valueData, String showText, Boolean isSelect) {
        this.valueData = valueData;
        this.showText = showText;
        this.isSelect = isSelect;
    }

    public DelayedControlData() {

    }
}
