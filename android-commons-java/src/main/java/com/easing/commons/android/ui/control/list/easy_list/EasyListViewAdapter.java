package com.easing.commons.android.ui.control.list.easy_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import com.easing.commons.android.annotation_processor.Reflection;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.struct.ListConvertor;
import com.easing.commons.android.thread.MainThread;
import com.easing.commons.android.view.Views;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

@SuppressWarnings("all")
abstract public class EasyListViewAdapter<T, HOLDER extends EasyListViewHolder> extends RecyclerView.Adapter<EasyListViewAdapter.ViewHolder> {

    public static final int NORMAL_VIEW = 0;
    public static final int HEADER_VIEW = 1;
    public static final int FOOTER_VIEW = 2;

    protected EasyListView listView;

    protected CommonActivity ctx;

    @Getter
    protected final List<T> datas = new LinkedList();

    protected int itemLayout;

    //点击事件
    public onItemClick onItemClick;

    //头部和底部视图
    //注意，这两个视图和ListView的Header/Footer不是同一个概念
    //可以给Adapter设置Header，再给ListView设置Header
    protected View headerView;
    protected View footerView;

    public static <K extends EasyListViewAdapter> K create(Class<K> clazz, @LayoutRes int itemLayoutId) {
        K adapter = Reflection.newInstance(clazz);
        adapter.itemLayout = itemLayoutId;
        return adapter;
    }

    public EasyListViewAdapter itemLayout(@LayoutRes int itemLayoutId) {
        itemLayout = itemLayoutId;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) return new ViewHolder(headerView);
        if (viewType == 2) return new ViewHolder(footerView);
        //创建item布局
        View root = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        //通过反射自动创建holder
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Class<HOLDER> holderType = (Class) parameterizedType.getActualTypeArguments()[1];
        HOLDER holder = Reflection.newInstance(holderType);
        holder.root = root;
        Views.viewBinding(holder, root);
        //将holder包装成RecyclerView.ViewHolder
        ViewHolder viewHolder = new ViewHolder(root);
        viewHolder.holder = holder;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EasyListViewAdapter.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) != NORMAL_VIEW) return;
        HOLDER holder = (HOLDER) viewHolder.holder;
        T data = datas.get(headerView == null ? position : position - 1);
        viewHolder.bindingData = data;
        onViewBind(holder, data, position);
    }

    abstract public void onViewBind(HOLDER holder, T data, int position);

    @Override
    public int getItemCount() {
        if (datas == null) return 0;
        int count = datas.size();
        if (headerView != null) count++;
        if (footerView != null) count++;
        return count;
    }

    public T getItem(int position) {
        return datas.get(headerView == null ? position : ++position);
    }

    public int getItemIndex(T item) {
        int index = datas.indexOf(item);
        if (headerView != null) index++;
        return index;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerView != null && position == 0) return HEADER_VIEW;
        if (footerView != null && position == getItemCount() - 1) return FOOTER_VIEW;
        return NORMAL_VIEW;
    }

    //重置数据
    public void update() {
        if (listView != null)
            MainThread.post(() -> {
                notifyDataSetChanged();
                listView.root.setRefreshing(false);
            });
    }

    //重置数据
    public void reset(List<T> datas) {
        List<T> newList = Collections.listCopy(datas); //防止使用一个对象先复制一份

        if (listView != null)
            MainThread.post(() -> {

                this.datas.clear();
                notifyDataSetChanged();

                if (newList != null)
                    this.datas.addAll(newList);

                notifyDataSetChanged();
                listView.root.setRefreshing(false);
            });
    }

    //重置数据
    public void reset(T... datas) {
        List<T> newList = Collections.asList(datas); //防止使用一个对象先复制一份

        if (listView != null)
            MainThread.post(() -> {
                this.datas.clear();
                notifyDataSetChanged();
                if (newList != null)
                    this.datas.addAll(newList);
                notifyDataSetChanged();
                listView.root.setRefreshing(false);
            });
    }

    //添加数据
    public EasyListViewAdapter<T, HOLDER> add(T data) {
        this.datas.add(data);
        MainThread.post(() -> {
            int index = getItemIndex(data);
            notifyItemInserted(index);
        });
        return this;
    }

    //添加数据
    public EasyListViewAdapter<T, HOLDER> add(List<T> datas) {
        this.datas.addAll(datas);
        if (Texts.isListNull(datas))
            MainThread.post(() -> {
                T item = datas.get(0);
                int index = getItemIndex(item);
                notifyItemRangeInserted(index, datas.size());
            });
        return this;
    }

    //移除数据
    public EasyListViewAdapter<T, HOLDER> remove(T data) {
        int index = getItemIndex(data);
        if (index < 0)
            return this;
        this.datas.remove(data);
        MainThread.post(() -> {
            notifyItemRemoved(index);
        });
        return this;
    }

    //设置一个空白控件作为Header
    //可利用此功能为ListView顶部设置一段空白间距
    public void setBlankHeader(int heightDp) {
        View header = new View(ctx);
        header.setLayoutParams(new LinearLayout.LayoutParams(Views.MATCH_PARENT, Dimens.toPx(heightDp)));
        setHeaderView(header);
    }

    //设置一个空白控件作为Footer
    //可利用此功能为ListView底部设置一段空白间距
    public void setBlankFooter(int heightDp) {
        View header = new View(ctx);
        header.setLayoutParams(new LinearLayout.LayoutParams(Views.MATCH_PARENT, Dimens.toPx(heightDp)));
        setFooterView(header);
    }

    //设置Header
    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        notifyDataSetChanged();
    }

    //设置Footer
    public void setFooterView(View footerView) {
        this.footerView = footerView;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View root;
        public Object bindingData;
        public EasyListViewHolder holder;

        public ViewHolder(View root) {
            super(root);
            this.root = root;
        }
    }
}
