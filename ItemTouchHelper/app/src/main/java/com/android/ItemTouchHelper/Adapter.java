package com.android.ItemTouchHelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ItemTouchHelper.helper.ItemTouchHelper;
import com.android.ItemTouchHelper.util.Console;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "ItemTouchHelperDebug";

    public static final int ITEM_TYPE_RECYCLER_WIDTH = 1000;
    public static final int ITEM_TYPE_ACTION_WIDTH = 1001;
    public static final int ITEM_TYPE_ACTION_WIDTH_NO_SPRING = 1002;
    private List<Model> mDatas;
    private Context mContext;
    //    private ItemTouchHelperExtension mItemTouchHelperExtension;
    private ItemTouchHelper mItemTouchHelperExtension;

    public Adapter(Context context) {
        mDatas = new ArrayList<>();
        mContext = context;
    }

    public void setDatas(List<Model> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
    }

    public void updateData(List<Model> datas) {
        setDatas(datas);
        notifyDataSetChanged();
    }

//    public void setItemTouchHelperExtension(ItemTouchHelperExtension itemTouchHelperExtension) {
//        mItemTouchHelperExtension = itemTouchHelperExtension;
//    }

    public void setItemTouchHelperExtension(ItemTouchHelper itemTouchHelperExtension) {
        mItemTouchHelperExtension = itemTouchHelperExtension;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_ACTION_WIDTH) {
            View view = getLayoutInflater().inflate(R.layout.list_item_main, parent, false);
            return new ItemSwipeWithActionWidthViewHolder(view);
        }
        if (viewType == ITEM_TYPE_RECYCLER_WIDTH) {
            View view = getLayoutInflater().inflate(R.layout.list_item_with_single_delete, parent, false);
            return new RecyclerWidthVH(view);
        }
        View view = getLayoutInflater().inflate(R.layout.list_item_main, parent, false);
        return new ActionWidthVH(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ItemBaseViewHolder baseViewHolder = (ItemBaseViewHolder) holder;
        baseViewHolder.bind(mDatas.get(position));
        baseViewHolder.mViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Console.debug("ItemView.onClick");
                Console.debug("=>", "ItemView.onClick", "void");
            }
        });
        if (holder instanceof RecyclerWidthVH) {
            RecyclerWidthVH viewHolder = (RecyclerWidthVH) holder;
            viewHolder.mActionViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doDelete(holder.getAdapterPosition());
                }
            });
        } else if (holder instanceof ItemSwipeWithActionWidthViewHolder) {
            ItemSwipeWithActionWidthViewHolder viewHolder = (ItemSwipeWithActionWidthViewHolder) holder;
            viewHolder.mActionViewRefresh.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }

            );
            viewHolder.mActionViewDelete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doDelete(holder.getAdapterPosition());
                        }
                    }

            );
        }
    }

    private void doDelete(int adapterPosition) {
        mDatas.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }


    public void move(int from, int to) {
        Model prev = mDatas.remove(from);
        mDatas.add(to > from ? to - 1 : to, prev);
        notifyItemMoved(from, to);
    }

    @Override
    public int getItemViewType(int position) {
        int type = position % 3;
        if (type == 1) {
            return ITEM_TYPE_ACTION_WIDTH_NO_SPRING;
        }
        if (type == 2) {
            return ITEM_TYPE_RECYCLER_WIDTH;
        }
        return ITEM_TYPE_ACTION_WIDTH;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ItemBaseViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextTitle;
        public TextView mTextIndex;
        public  View mViewContent;
        public  View mActionContainer;

        public ItemBaseViewHolder(View itemView) {
            super(itemView);
            mTextTitle = (TextView) itemView.findViewById(R.id.text_list_main_title);
            mTextIndex = (TextView) itemView.findViewById(R.id.text_list_main_index);
            mViewContent = itemView.findViewById(R.id.view_list_main_content);
            mActionContainer = itemView.findViewById(R.id.view_list_repo_action_container);
        }

        public void bind(Model testModel) {
            mTextTitle.setText(testModel.title);
            mTextIndex.setText("#" + testModel.position);
//            itemView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent e) {
//                    Log.d(TAG, "ItemView.OnTouchListener.onTouch " + MotionEvents.name(e));
//                    if (MotionEventCompat.getActionMasked(e) == MotionEvent.ACTION_DOWN) {
//                        mItemTouchHelperExtension.startDrag(ItemBaseViewHolder.this);
//                    }
//                    return true;
//                }
//            });
        }
    }


    public class RecyclerWidthVH extends ItemBaseViewHolder {

        View mActionViewDelete;

        public RecyclerWidthVH(View itemView) {
            super(itemView);
            mActionViewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);
        }

    }

    public  class ItemSwipeWithActionWidthViewHolder extends ItemBaseViewHolder {

        View mActionViewDelete;
        View mActionViewRefresh;

        public ItemSwipeWithActionWidthViewHolder(View itemView) {
            super(itemView);
            mActionViewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);
            mActionViewRefresh = itemView.findViewById(R.id.view_list_repo_action_update);
        }

    }

   public class ActionWidthVH extends ItemSwipeWithActionWidthViewHolder {

        public ActionWidthVH(View itemView) {
            super(itemView);
        }
    }

}
