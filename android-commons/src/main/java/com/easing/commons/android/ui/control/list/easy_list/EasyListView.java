package com.easing.commons.android.ui.control.list.easy_list;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.ui.control.layout_manager.ScrollableGridLayoutManager;
import com.easing.commons.android.ui.control.layout_manager.ScrollableHorizontalLayoutManager;
import com.easing.commons.android.view.Views;

import libSwipRecyclerView.AdapterWrapper;
import libSwipRecyclerView.SwipeRecyclerView;
import libSwipRecyclerView.touch.OnItemMoveListener;
import libSwipRecyclerView.widget.DefaultLoadMoreView;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import lombok.Getter;

@SuppressWarnings("all")
public class EasyListView extends FrameLayout {

    SwipeRefreshLayout root;

    @Getter
    BaseListView coreListView;

    //无内容时的提示控件
    ImageView emptyView;

    //限制最大高度
    Float maxHeight;

    //下拉刷新回调
    Action onRefreshStart;

    //上拉加载回调
    Action onLoadMoreStart;

    //上个被监听的adapter
    RecyclerView.Adapter lastObservable = null;

    //监听数据变化
    final EasyListViewObserver dataObserver = new EasyListViewObserver() {
        @Override
        public void updateHeaderView() {
            EasyListViewAdapter adapter = adapter();
            if (adapter == null) {
                coreListView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                return;
            }
            boolean empty = adapter.datas.size() == 0;
            if (empty) {
                coreListView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                coreListView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
        }
    };

    //监听数据侧滑删除和拖拽移动
    final OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onItemDismiss(RecyclerView.ViewHolder viewHolder) {
            EasyListViewAdapter adapter = adapter();
            if (adapter == null) return;
            EasyListViewAdapter.ViewHolder holder = (EasyListViewAdapter.ViewHolder) viewHolder;
            int index = adapter.getDatas().indexOf(holder.bindingData);
            adapter.getDatas().remove(holder.bindingData);
            adapter.notifyDataSetChanged();
        }
    };

    public EasyListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        //获取xml属性
        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.EasyListView, 0, 0);
        maxHeight = array.getDimension(R.styleable.EasyListView_maxHeight, -1);
        if (maxHeight == -1) maxHeight = null;
        //加载布局
        root = Views.inflate(context, R.layout.layout_easy_list);
        coreListView = root.findViewById(R.id.list);
        emptyView = root.findViewById(R.id.image_empty);
        emptyView.setVisibility(View.GONE);
        addView(root, Views.MATCH_PARENT, Views.MATCH_PARENT);
        //默认使用纵向布局
        verticalLayout();
        //默认禁用刷新功能
        root.setEnabled(false);
    }

    //设置空数据提示
    public EasyListView emptyView(@DrawableRes int drawableId) {
        emptyView.setImageResource(drawableId);
        emptyView.setImageTintList(null);
        return this;
    }

    //开启下拉刷新功能
    public EasyListView enableRefresh() {
        //监听
        root.setOnRefreshListener(() -> {
            root.setRefreshing(true);
            if (onRefreshStart != null)
                onRefreshStart.runAndPostException();
            else
                root.setRefreshing(false);
        });
        //开启
        root.setEnabled(true);
        return this;
    }

    //下拉刷新监听
    public EasyListView onRefreshStart(Action onRefreshStart) {
        this.onRefreshStart = onRefreshStart;
        return this;
    }

    //开始下拉刷新
    public EasyListView startRefresh() {
        root.setRefreshing(true);
        return this;
    }

    //停止下拉刷新
    public EasyListView finishRefresh() {
        root.setRefreshing(false);
        return this;
    }

    private SwipeRecyclerView.LoadMoreView cLoa;

    //开启上拉加载更多功能
    public EasyListView enableLoadMore(SwipeRecyclerView.LoadMoreView loadMoreView) {
        //监听
        coreListView.setLoadMoreListener(() -> {
            if (onLoadMoreStart != null)
                onLoadMoreStart.runAndPostException();
            else
                finishLoadMore(false);
        });
        //开启

        if (cLoa != null)
            coreListView.removeFooterView((View) cLoa);

        if (loadMoreView != null) {
            cLoa = loadMoreView;
        } else {
            cLoa = new DefaultLoadMoreView(getContext());
        }
        coreListView.addFooterView((View) cLoa);
        coreListView.setLoadMoreView(cLoa);
        coreListView.setAutoLoadMore(true);
        coreListView.loadMoreFinish(false, true);
        return this;
    }

    //下拉刷新监听
    public EasyListView onLoadMoreStart(Action onLoadMoreStart) {
        this.onLoadMoreStart = onLoadMoreStart;
        return this;
    }

    //停止加载更多动画
    public EasyListView finishLoadMore(boolean hasMore) {
        coreListView.loadMoreFinish(false, hasMore);
        return this;
    }

    /**
     * 设置空数据
     */
    public EasyListView noDataLoadMore() {
        coreListView.loadMoreFinish(true, false);
        return this;
    }

    //开启侧滑删除功能
    public EasyListView enableSwipeDeletion() {
        coreListView.setItemViewSwipeEnabled(true);
        coreListView.setOnItemMoveListener(onItemMoveListener);
        return this;
    }

    //设置布局
    public EasyListView verticalLayout() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        coreListView.setLayoutManager(manager);
        return this;
    }

    //设置布局
    public EasyListView horizontalLayout() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);
        coreListView.setLayoutManager(manager);
        return this;
    }

    //设置布局
    public EasyListView gridLayout(int column) {
        LinearLayoutManager manager = new GridLayoutManager(getContext(), column);
        coreListView.setLayoutManager(manager);
        return this;
    }

    //设置布局
    public ScrollableHorizontalLayoutManager scrollableHorizontalLayout() {
        ScrollableHorizontalLayoutManager manager = new ScrollableHorizontalLayoutManager(getContext());
        manager.scrollable(true);
        coreListView.setLayoutManager(manager);
        return manager;
    }

    //设置布局
    public ScrollableGridLayoutManager scrollableGridLayout(int column) {
        ScrollableGridLayoutManager manager = new ScrollableGridLayoutManager(getContext(), column);
        manager.scrollable(true);
        coreListView.setLayoutManager(manager);
        return manager;
    }

    //设置布局
    public EasyListView layout(RecyclerView.LayoutManager layoutManager) {
        coreListView.setLayoutManager(layoutManager);
        return this;
    }

    //获取适配器
    public EasyListViewAdapter adapter() {
        AdapterWrapper adapterWrapper = (AdapterWrapper) coreListView.getAdapter();
        if (adapterWrapper == null) return null;
        return (EasyListViewAdapter) adapterWrapper.getOriginAdapter();
    }

    //设置适配器
    public EasyListView adapter(EasyListViewAdapter adapter) {
        if (lastObservable != null) {
            lastObservable.unregisterAdapterDataObserver(dataObserver);
            lastObservable = null;
        }
        coreListView.setAdapter(adapter);
        adapter.listView = this;
        adapter.ctx = (CommonActivity) getContext();
        coreListView.getAdapter().registerAdapterDataObserver(dataObserver);
        lastObservable = coreListView.getAdapter();
        dataObserver.onChanged();
        return this;
    }

    //设置空的适配器
    public EasyListView emptyAdapter() {
        coreListView.removeAllHeaderViews();
        coreListView.removeAllFooterViews();
        if (lastObservable != null) {
            lastObservable.unregisterAdapterDataObserver(dataObserver);
            lastObservable = null;
        }
        EmptyListViewAdapter adapter = new EmptyListViewAdapter();
        coreListView.setAdapter(adapter);
        adapter.listView = this;
        adapter.ctx = (CommonActivity) getContext();
        coreListView.getAdapter().registerAdapterDataObserver(dataObserver);
        lastObservable = coreListView.getAdapter();
        dataObserver.onChanged();
        return this;
    }


    public void addHeaderView(View view) {
        coreListView.addHeaderView(view);
    }

    public void removeHeaderView(View view) {
        coreListView.removeHeaderView(view);
    }


    public void addFooterView(View view) {
        coreListView.addFooterView(view);
    }

    public void removeFooterView(View view) {
        coreListView.removeFooterView(view);
    }

    //设置是否拦截触摸事件
    public EasyListView interceptable(boolean interceptable) {
        coreListView.setInterceptable(interceptable);
        return this;
    }

    //重新测量，限制最大高度
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (maxHeight != null && specSize > maxHeight)
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight.intValue(), MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //设置最大高度
    public void maxHeight(int maxHeightDp) {
        maxHeight = (float) Dimens.toPx(maxHeightDp);
        requestLayout();
    }

    public BaseListView getListView() {
        return coreListView;
    }
}
