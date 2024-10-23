package com.easing.commons.android.ui.form.item;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.helper.callback.SucceedCallBackListener;
import com.easing.commons.android.preference.Preference;
import com.easing.commons.android.struct.StringList;
import com.easing.commons.android.ui.adapter.ViewAdapter;
import com.easing.commons.android.ui.control.layout.FlowLayout;
import com.easing.commons.android.ui.control.list.easy_list.EasyListView;
import com.easing.commons.android.ui.control.list.easy_list.EasyListViewAdapter;
import com.easing.commons.android.ui.control.list.easy_list.onItemClick;
import com.easing.commons.android.ui.dialog.BottomSlideDialog;
import com.easing.commons.android.ui.dialog.TipBox;
import com.easing.commons.android.ui.form.FormItem;
import com.easing.commons.android.ui.form.item.adapter.DataListAdapter;
import com.easing.commons.android.ui.listener.TextChangeListener;
import com.easing.commons.android.view.Views;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 *
 */
public class FormUrlLayout {

    private FormItem formItem;
    private Context mContext;
    public BottomSlideDialog bottomSlideDialog;
    private EditText sed; //搜索框
    private FlowLayout flowLayout;
    private EasyListView eas_list_view;

    private List<DataBean> stringList = new ArrayList<>(); //使用的
    private List<DataBean> dataList = new ArrayList<>(); //原有数据
    private List<String> hiLists = new ArrayList<>();
    private DataListAdapter adapter;
    private String tag, blurryName;
    private SucceedCallBackListener<String> listener;

    private String hiName; //最近使用的名称key

    public FormUrlLayout(Context context, FormItem formItem) {
        init(context, formItem);
    }

    public void setOnClick(SucceedCallBackListener<String> listener) {

        this.listener = listener;
    }

    /**
     * 处理 数据
     */
    private List<DataBean> handListData(String blurryName) {
        List<DataBean> list1 = new ArrayList<>();

        if (!TextUtils.isEmpty(blurryName)) {
            for (DataBean str : dataList) {
                if (str.getName().contains(blurryName))
                    list1.add(str);
            }
            return list1;
        } else {
            return dataList;
        }
    }

    private void init(Context context, FormItem formItem) {
        mContext = context;
        this.formItem = formItem;
        bottomSlideDialog = BottomSlideDialog.create((CommonActivity) context, R.layout.dialog_view_type_select_name);
        adapter = EasyListViewAdapter.create(DataListAdapter.class, R.layout.item_from_layout);
        hiName = formItem.reservedField + "_NameRecord";
        sed = bottomSlideDialog.findView(R.id.search_edit);
        flowLayout = bottomSlideDialog.findView(R.id.nameRecordLayout);
        eas_list_view = bottomSlideDialog.findView(R.id.eas_list_view);
        TextView text_name = bottomSlideDialog.findView(R.id.text_name);
        text_name.setText(formItem.description);
        bottomSlideDialog.onClick(R.id.personalc, new Views.OnClick() {
            @Override
            public void onClick() {
                sed.setText("");
                stringList = handListData( null);
                adapter.reset(stringList);
            }
        });


        sed.addTextChangedListener(new TextChangeListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                blurryName = s.toString().trim();
                stringList = handListData(blurryName);
                adapter.reset(stringList);
            }
        });

        bottomSlideDialog.onClick(R.id.cancel_ll, new Views.OnClick() { //取消
            @Override
            public void onClick() {
                bottomSlideDialog.close();
            }
        });

        bottomSlideDialog.onClick(R.id.ok_ll, new Views.OnClick() { //确定
            @Override
            public void onClick() {
                bottomSlideDialog.close();
                if (TextUtils.isEmpty(tag)) {
                    TipBox.tip("当前未选择名称");
                    return;
                }
                if (listener != null)
                    listener.succeedCallBack(tag);
                setNameHi(tag);
            }
        });

        //surveyPointNameRecord

        hiLists = Preference.get(hiName, StringList.class, new StringList()); //

        Map<String, Object> dataMap = formItem.dataMap;
        if (formItem.dataMap != null) {
            if (formItem.dataMap.containsKey("default_data")) {
                tag = (String) dataMap.get("default_data");
            }
        }

        List<String> records = Preference.get(formItem.reservedField, new TypeToken<List<String>>() {
        }.getType());
        if (records != null && records.size() > 0) {
            for (String oj : records) {
                DataBean dataBean = new DataBean();
                dataBean.setName(oj);
                dataBean.isSelect = oj.equals(tag); //默认选选中
                stringList.add(dataBean);
                dataList.add(dataBean);
            }
            adapter.reset(stringList);
        }

        if (hiLists != null && hiLists.size() > 0) {
            SurveyPointNameRecordAdapter nameAdapter = new SurveyPointNameRecordAdapter();
            nameAdapter.bindParent(flowLayout);
            nameAdapter.bindData(hiLists);
            flowLayout.setViewAdapter(nameAdapter);
        }


        eas_list_view.adapter(adapter);

        adapter.onItemClick = new onItemClick<DataBean>() {
            @Override
            public void onClick(View view, DataBean data, int position) {

                for (int i = 0; i < stringList.size(); i++) {
                    if (i == position) {
                        stringList.get(i).isSelect = true;
                    } else {
                        stringList.get(i).isSelect = false;
                    }
                }
                adapter.notifyDataSetChanged();
                tag = data.name;
            }

            @Override
            public void onLongClick(View view, DataBean data, int position) {

            }
        };
    }

    public class SurveyPointNameRecordAdapter extends ViewAdapter<String> {

        @Override
        public View createView(int pos) {
            String name = datas.get(pos);
            View itemView = Views.inflateAndAttach(context, R.layout.md002_item_point_name, parent, false);
            TextView nameText = itemView.findViewById(R.id.nameText);
            nameText.setText(name);
            Views.onClick(nameText, () -> {
                tag = name;
                if (listener != null)
                    listener.succeedCallBack(tag);
                setNameHi(tag);
                bottomSlideDialog.close();
            });
            return itemView;
        }
    }


    @Data
    public class DataBean {
        public boolean isSelect;
        private String name;

    }

    /**
     * 保存历史
     */
    public void setNameHi(String name) {
        StringList nameRecords = Preference.get(hiName, StringList.class, new StringList());
        if (nameRecords.size() == 6)
            nameRecords.remove(5);
        if (!nameRecords.contains(name))
            nameRecords.add(0, name);
        Preference.set(hiName, nameRecords);
    }
}
