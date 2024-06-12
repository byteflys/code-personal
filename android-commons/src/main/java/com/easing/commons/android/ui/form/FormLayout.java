package com.easing.commons.android.ui.form;

import static com.easing.commons.android.time.Times.FORMAT_01;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.data.JSON;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.helper.callback.SucceedCallBackListener;
import com.easing.commons.android.helper.exception.BizException;
import com.easing.commons.android.manager.Device;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.struct.StringMap;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.ui.control.picker.DateTimePicker;
import com.easing.commons.android.ui.dialog.TipBox;
import com.easing.commons.android.ui.form.item.FormAutoCompleteInput;
import com.easing.commons.android.ui.form.item.FormInput;
import com.easing.commons.android.ui.form.item.FormLabel;
import com.easing.commons.android.ui.form.item.FormSelector;
import com.easing.commons.android.ui.form.item.FormUrlLayout;
import com.easing.commons.android.ui.form.item.UrlFormLabel;
import com.easing.commons.android.value.apk.SdkVersion;
import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.view.InputType;
import com.easing.commons.android.view.Views;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.SneakyThrows;

//带边框和动态加载功能的表格布局
@SuppressWarnings("all")
public class FormLayout extends GridLayout {

    FormInfo formInfo;

    CommonActivity ctx;

    final Map<String, FormItem> itemMap = new LinkedHashMap();
    final Map<FormItem, View> viewMap = new LinkedHashMap();
    public List<String> urlLists;
    int rowHeight = Dimens.toPx(40);
    int padding = Dimens.toPx(8);

    int titleSize = Dimens.toPx(16);
    int inputSize = Dimens.toPx(14);
    int multiLineRemarkSize = Dimens.toPx(13);

    public static final String Input = "1"; //单行输入框
    public static final String Label = "2"; //单行标题标签
    public static final String Selector = "3"; //下拉框
    public static final String LongitudePicker = "4"; //经度拾取器
    public static final String LatitudePicker = "5"; //纬度拾取器
    public static final String timePicker = "6"; //时间日期选择
    public static final String MultiLineInput = "26"; //多行输入框
    public static final String RemarkLabel = "27"; //多行备注标签
    public static final String AutoCompleteInput = "28"; //自动补全输入框
    public static final String AltitudeInput = "29"; //海拔输入框
    public static final String NoEdieInput = "30";//不可编辑的输入框
    public static final String UrlAutoCompleteInput = "31";//从网络请求数据的选择器

    private boolean isEnabled = true;

    public FormLayout(Context context) {
        this(context, null);
    }

    public FormLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = (CommonActivity) context;
    }

    @Override
    protected void onDetachedFromWindow() {
        viewMap.clear();
        super.onDetachedFromWindow();
    }

    /**
     * 是否可以编辑
     */
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    //初始化全部子控件
    public void initLayout(FormInfo formInfo) {
        this.formInfo = formInfo;
        viewMap.clear();
        removeAllViews();
        setColumnCount(formInfo.columnCount);
        for (FormItem item : formInfo.formItems) {
            View view = addItem(item);
            if (!Texts.isEmpty(item.field))
                itemMap.put(item.field, item);
            viewMap.put(item, view);
        }
    }

    //初始化全部子控件
    public void initLayoutFromJson(String json) {
        FormInfo formInfo = JSON.parse(json, FormInfo.class);
        initLayout(formInfo);
    }

    //填充内容
    public void fillContent(Map<String, ?> map) {
        if (map == null)
            return;
        for (String field : map.keySet()) {
            Object value = map.get(field);
            FormItem item = itemMap.get(field);
            if (value == null || item == null) continue;
            View view = viewMap.get(item);
            setBindingData(item, view, value);
        }
    }

    //添加一个Item
    public View addItem(FormItem item) {
        View view = createView(item);
        item.itemView = view;
        LayoutParams layoutParams = new LayoutParams();
        layoutParams.width = 0;
        layoutParams.height = rowHeight * item.rowSpan;
        layoutParams.rowSpec = GridLayout.spec(item.startRow - 1, item.rowSpan, item.rowSpan);
        layoutParams.columnSpec = GridLayout.spec(item.startColumn - 1, item.columnSpan, item.columnSpan);
        view.setEnabled(isEnabled);
        addView(view, layoutParams);
        return view;
    }

    //根据Item配置创建View
    private final View createView(FormItem item) {

        //文本标签
        if (Texts.equal(item.type, Label)) {
            FormLabel formText = new FormLabel(ctx);
            formText.setTextColor(Colors.BLACK_80);
            formText.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
            formText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            formText.setGravity(Gravity.CENTER);
            formText.setPadding(padding, padding, padding, padding);
            Views.text(formText, item.value);
            return formText;
        }

        //文本编辑框
        if (Texts.equal(item.type, Input)) {
            FormInput formEdit = new FormInput(ctx);
            formEdit.setSingleLine();
            formEdit.setGravity(Gravity.CENTER);
            formEdit.setTextColor(Colors.BLACK_80);
            formEdit.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputSize);
            if (Device.sdkVersionCode() >= SdkVersion.ANDROID_10) {
                formEdit.setTextCursorDrawable(R.drawable.edit_cursor_m03);
                formEdit.setTextIsSelectable(false);
            }
            formEdit.setBackgroundResource(R.drawable.edit_m11);
            formEdit.setPadding(padding, padding, padding, padding);
            formEdit.setElevation(Dimens.toPx(1));
            Views.text(formEdit, item.value);
            formEdit.setEnabled(isEnabled); //是否可编辑
            //数量控件只能输入数字
            if (item.field.equals("count"))
                Views.inputType(formEdit, InputType.NUMBER);
            return formEdit;
        }

        //下拉框
        if (Texts.equal(item.type, Selector)) {
            FormSelector formSelector = new FormSelector(ctx);
            formSelector.setGravity(Gravity.CENTER);
            formSelector.setTextColor(Colors.BLACK_80);
            formSelector.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputSize);
            formSelector.setBackgroundResource(R.drawable.edit_m11);
            formSelector.setPadding(padding, padding, padding, padding);
            formSelector.options(parseOptions(item.content));
            formSelector.select(item.value);
            return formSelector;
        }

        //坐标拾取框
        if (Texts.equal(item.type, LongitudePicker)) {
            FormLabel formText = new FormLabel(ctx);
            formText.setTextColor(Colors.BLACK_80);
            formText.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputSize);
            formText.setGravity(Gravity.CENTER);
            formText.setPadding(padding, padding, padding, padding);
            formText.setBackgroundResource(R.drawable.edit_m11);
            formText.setElevation(Dimens.toPx(1));
            if (isEnabled)
                Views.onClick(formText, () -> {

                    ctx.requestCodeForLocationPick = Texts.random();
                    ctx.pickLocation(ctx.requestCodeForLocationPick);

                });
            return formText;
        }//

        //时间日期选择
        if (Texts.equal(item.type, timePicker)) { //时间日期选择
            FormLabel dataText = new FormLabel(ctx);
            dataText.setTextColor(Colors.BLACK_80);
            dataText.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputSize);
            dataText.setGravity(Gravity.CENTER);
            dataText.setPadding(padding, padding, padding, padding);
            Calendar calendar = Times.getCalendar(); //当前时间
            String format = Times.formatCalendar(calendar, Times.FORMAT_01); //设置当前时间
            dataText.setText(format);
            dataText.setBackgroundResource(R.drawable.edit_m11);
            dataText.setElevation(Dimens.toPx(1));
            if (isEnabled) {
                Views.onClick(dataText, () -> {
                    DateTimePicker.pick(ctx, new DateTimePicker.Callback() {
                        @Override
                        public void onDateSelect(Date date) {
                            String dataTime = Times.formatDate(date, FORMAT_01);
                            dataText.setText(dataTime);
                        }
                    });
                });
            }
            return dataText;
        }

        //坐标拾取框
        if (Texts.equal(item.type, LatitudePicker)) {
            FormLabel formText = new FormLabel(ctx);
            formText.setTextColor(Colors.BLACK_80);
            formText.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputSize);
            formText.setGravity(Gravity.CENTER);
            formText.setPadding(padding, padding, padding, padding);
            formText.setBackgroundResource(R.drawable.edit_m11);
            formText.setElevation(Dimens.toPx(1));
            if (isEnabled)
                Views.onClick(formText, () -> {
                    ctx.requestCodeForLocationPick = Texts.random();
                    ctx.pickLocation(ctx.requestCodeForLocationPick);
                });
            return formText;
        }

        //多行输入框
        if (Texts.equal(item.type, MultiLineInput)) {
            FormInput formEdit = new FormInput(ctx);
            formEdit.setSingleLine(false);
            formEdit.setGravity(Gravity.LEFT | Gravity.TOP);
            formEdit.setTextColor(Colors.BLACK_80);
            formEdit.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputSize);
            if (Device.sdkVersionCode() >= SdkVersion.ANDROID_10) {
                formEdit.setTextCursorDrawable(R.drawable.edit_cursor_m03);
                formEdit.setTextIsSelectable(false);
            }
            formEdit.setBackgroundResource(R.drawable.edit_m11);
            formEdit.setPadding(padding, padding, padding, padding);
            formEdit.setElevation(Dimens.toPx(1));
            formEdit.setEnabled(isEnabled);
            Views.text(formEdit, item.value);
            return formEdit;
        }

        //多行备注标签
        if (Texts.equal(item.type, RemarkLabel)) {
            FormLabel formText = new FormLabel(ctx);
            formText.setTextColor(Colors.BLACK_80);
            formText.setTextSize(TypedValue.COMPLEX_UNIT_PX, multiLineRemarkSize);
            formText.setTypeface(Typeface.DEFAULT_BOLD);
            formText.setLineSpacing(Dimens.toPx(1), 1);
            formText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            formText.setPadding(padding, padding, padding, padding);
            formText.setEnabled(isEnabled);
            Views.text(formText, item.value);
            return formText;
        }

        //自动补全输入框
        if (Texts.equal(item.type, AutoCompleteInput)) {
            FormAutoCompleteInput autoCompleteInput = new FormAutoCompleteInput(ctx);
            autoCompleteInput.setSingleLine(true);
            autoCompleteInput.setGravity(Gravity.CENTER);
            autoCompleteInput.setTextColor(Colors.BLACK_80);
            autoCompleteInput.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputSize);
            if (Device.sdkVersionCode() >= SdkVersion.ANDROID_10)
                autoCompleteInput.setTextCursorDrawable(R.drawable.edit_cursor_m03);
            autoCompleteInput.setBackgroundResource(R.drawable.edit_m11);
            autoCompleteInput.setPadding(padding, padding, padding, padding);
            autoCompleteInput.setElevation(Dimens.toPx(1));
            autoCompleteInput.threshold(1);
            String[] options = parseOptions(item.content);
            autoCompleteInput.reset(options);
            autoCompleteInput.setEnabled(isEnabled);
            Views.text(autoCompleteInput, item.value);
            return autoCompleteInput;
        }

        //海拔输入框
        if (Texts.equal(item.type, AltitudeInput)) {
            FormInput formEdit = new FormInput(ctx);
            formEdit.setSingleLine();
            formEdit.setGravity(Gravity.CENTER);
            formEdit.setTextColor(Colors.BLACK_80);
            formEdit.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputSize);
            if (Device.sdkVersionCode() >= SdkVersion.ANDROID_10) {
                formEdit.setTextCursorDrawable(R.drawable.edit_cursor_m03);
                formEdit.setTextIsSelectable(false);
            }
            formEdit.setBackgroundResource(R.drawable.edit_m11);
            formEdit.setPadding(padding, padding, padding, padding);
            formEdit.setElevation(Dimens.toPx(1));
            formEdit.setMaxEms(5);
            Views.text(formEdit, item.value);
            Views.inputType(formEdit, InputType.NUMBER);
            return formEdit;
        }


        //不可编辑的输入框
        if (Texts.equal(item.type, NoEdieInput)) {
            FormLabel formText = new FormLabel(ctx);
            formText.setTextColor(Colors.BLACK_80);
            formText.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
            formText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            formText.setGravity(Gravity.CENTER);
            formText.setPadding(padding, padding, padding, padding);
            Views.text(formText, item.value);
            return formText;
        }

        //从网络请求数据的选择器
        if (Texts.equal(item.type, UrlAutoCompleteInput)) {
            UrlFormLabel formText = new UrlFormLabel(ctx);
            formText.setTextColor(Colors.BLACK_80);
            formText.setTextSize(TypedValue.COMPLEX_UNIT_PX, multiLineRemarkSize);
            formText.setTypeface(Typeface.DEFAULT_BOLD);
            formText.setGravity(Gravity.CENTER);
            formText.setLineSpacing(Dimens.toPx(1), 1);
            Views.text(formText, item.value);
            if (isEnabled)
                Views.onClick(formText, new Views.OnClick() {
                    @Override
                    public void onClick() {
                        item.dataMap = new LinkedHashMap<>();
                        item.dataMap.put("default_data", item.value); // 当前数据
                        ///item.dataMap = dataMap;
                        FormUrlLayout formLayout = new FormUrlLayout(getContext(), item);

                        formLayout.setOnClick(new SucceedCallBackListener<String>() {
                            @Override
                            public void succeedCallBack(@Nullable String o) { //成功后的数据
                                item.value = o;
                                Views.text(formText, o);
                            }
                        });

                        formLayout.bottomSlideDialog.show();
                    }
                });


            return formText;
        }

        throw BizException.of("unknown view type");
    }

    //获取数据并转为Map
    public Map<String, Object> formData() {
        Map<String, Object> dataMap = new LinkedHashMap();
        for (FormItem item : viewMap.keySet()) {
            if (Texts.isEmpty(item.field)) continue;
            View view = viewMap.get(item);
            Object data = getBindingData(item, view);
            //一个控件包含多个字段
            if (data instanceof Map) {
                Map map = (Map) data;
                dataMap.putAll(map);
            }
            dataMap.put(item.field, data);
        }
        return dataMap;
    }

    //获取数据并转为JsonObject
    @SneakyThrows
    public JSONObject formDataToJsonObject() {
        JSONObject jsonObject = JSON.newJsonObject();
        for (FormItem item : viewMap.keySet()) {
            if (Texts.isEmpty(item.field)) continue;
            View view = viewMap.get(item);
            Object data = getBindingData(item, view);
            //一个控件包含多个字段
            if (data instanceof Map) {
                Map<String, ?> map = (Map) data;
                for (String key : map.keySet())
                    jsonObject.put(key, map.get(key));
            }
            jsonObject.put(item.field, data);
        }
        return jsonObject;
    }

    //获取数据并转为JSON
    public String formDataToJson() {
        Map<String, Object> fromData = formData();
        return JSON.stringify(fromData);
    }

    //获取控件背后对应的数据
    protected final Object getBindingData(FormItem item, View view) {

        //文本标签
        if (Texts.equal(item.type, Label) || Texts.equal(item.type, RemarkLabel) || Texts.equal(item.type, NoEdieInput)) {
            TextView textView = (TextView) view;
            if (Views.isEmpty(textView))
                return null;
            return Views.text(textView);
        }

        //网络请求
        if (Texts.equal(item.type, UrlAutoCompleteInput)) {
            TextView textView = (TextView) view;
            if (Views.isEmpty(textView))
                return null;
            return Views.text(textView);
        }

        //文本编辑框
        if (Texts.equal(item.type, Input) || Texts.equal(item.type, MultiLineInput)) {
            FormInput textView = (FormInput) view;
            if (Views.isEmpty(textView))
                return null;
            return Views.text(textView);
        }

        //下拉框
        if (Texts.equal(item.type, Selector)) {
            FormSelector formSelector = (FormSelector) view;
            return formSelector.selectedOption();
        }

        //经度拾取器
        if (Texts.equal(item.type, LatitudePicker)) {
            TextView textView = (TextView) view;
            if (Views.isEmpty(textView))
                return null;
            return Views.text(textView);
        }

        //纬度拾取器
        if (Texts.equal(item.type, LongitudePicker)) {
            TextView textView = (TextView) view;
            if (Views.isEmpty(textView))
                return null;
            return Views.text(textView);
        }

        //自动补全输入框
        if (Texts.equal(item.type, AutoCompleteInput)) {
            TextView textView = (TextView) view;
            if (Views.isEmpty(textView))
                return null;
            return Views.text(textView);
        }

        //海拔拾取器 LongitudePicker
        if (Texts.equal(item.type, AltitudeInput)) {
            TextView textView = (TextView) view;
            if (Views.isEmpty(textView))
                return null;
            return Views.text(textView);
        }

        //时间选择 LongitudePicker
        if (Texts.equal(item.type, timePicker)) {
            TextView textView = (TextView) view;
            if (Views.isEmpty(textView))
                return null;
            return Views.text(textView);
        }

        return null;
    }

    //获取控件背后对应的数据
    protected final void setBindingData(FormItem item, View view, Object value) {

        //FormInput
        if (Texts.equal(item.type, Input) || Texts.equal(item.type, MultiLineInput)) {
            FormInput textView = (FormInput) view;
            Views.text(textView, Texts.toString(value));
        } else if (Texts.equal(item.type, Selector)) {//FormSelector
            FormSelector formSelector = (FormSelector) view;
            formSelector.select(Texts.toString(value));
        } else {
            TextView textView = (TextView) view;
            Views.text(textView, Texts.toString(value));
        }
    }

    //检查必填项
    public boolean nullCheck() {
        for (FormItem item : viewMap.keySet()) {
            if (Texts.equal(item.type, Label)) continue;
            if (Texts.equal(item.type, RemarkLabel)) continue;
            if (Texts.isEmpty(item.field)) continue;
            if (item.isRequired == null || item.isRequired.intValue() == 0) continue;
            View view = viewMap.get(item);
            if (!Views.isEmpty(view)) continue;
            view.requestFocus();
            TipBox.tipInCenter("请输入" + item.description);
            float y = view.getPivotY();
            View parent = (View) ((View) getParent()).getParent();
            parent.scrollTo(0, (int) y);
            return false;
        }
        return true;
    }

    //根据字段寻找控件
    public <T extends View> T findView(String field) {
        FormItem item = itemMap.get(field);
        View view = viewMap.get(item);
        return (T) view;
    }

    //解析下拉框数值
    //格式较为奇怪，是公司后台定义的，建议用JSON数组更方便
    protected String[] parseOptions(String content) {
        StringMap[] keyValuePairs = JSON.parse(content, StringMap[].class);
        List<String> options = Collections.emptyList();
        for (StringMap keyValuePair : keyValuePairs) {
            Set<String> keySet = keyValuePair.keySet();
            options.addAll(keySet);
        }
        return Collections.toArray(options, String[]::new);
    }


}
