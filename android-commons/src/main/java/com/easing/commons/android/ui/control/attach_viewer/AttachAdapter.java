package com.easing.commons.android.ui.control.attach_viewer;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.easing.commons.android.R;
import com.easing.commons.android.R2;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.manager.Uris;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.ui.control.MultiFilePreviewer.MultiFilePreviewer;
import com.easing.commons.android.ui.control.list.easy_list.EasyListViewHolder;
import com.easing.commons.android.ui.control.list.easy_list.EasyListViewAdapter;
import com.easing.commons.android.view.Views;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lombok.SneakyThrows;

@SuppressWarnings("all")
public class AttachAdapter extends EasyListViewAdapter<Attach, AttachAdapter.Holder> {

    boolean removable = true;
    boolean showName = true;

    EventListener listener;

    public AttachAdapter() {
        itemLayout = R.layout.item_attach_list;
    }

    @Override
    @SneakyThrows
    public void onViewBind(AttachAdapter.Holder holder, Attach data, int position) {
        Views.size(holder.attachViewer, Views.MATCH_PARENT, Dimens.toPx(ctx, 90));
        holder.attachViewer.load(data);
        Views.onClick(holder.attachViewer, () -> {
            int index = datas.indexOf(data);
            if (data.url != null && data.url.startsWith("http") && !data.type.equals("other")) {
                MultiFilePreviewer.previewUrl(datas, index);
            } else { //本地
                List<String> fileList = Collections.convert(datas, attach -> attach.file);

                MultiFilePreviewer.preview(fileList, index);
            }

        });

        Views.onLongClick(holder.attachViewer, () -> {
            if (listener != null)
                listener.onAiIamge(Uris.fromFile(data.file));
        });

        Views.onClick(holder.removeButton, () -> {
            remove(data);
        });

        //禁止移除操作
        if (removable)
            holder.removeButton.setVisibility(View.VISIBLE);
        else
            holder.removeButton.setVisibility(View.GONE);

        //显示文件名
        if (showName)
            holder.attachViewer.textView.setVisibility(View.VISIBLE);
        else
            holder.attachViewer.textView.setVisibility(View.GONE);
    }

    public static class Holder extends EasyListViewHolder {
        @BindView(R2.id.attach_viewer)
        AttachViewer attachViewer;
        @BindView(R2.id.bt_remove)
        ImageView removeButton;
    }

    public List<String> attachs() {
        List<String> paths = new ArrayList();
        for (Attach data : datas)
            paths.add(data.file);
        return paths;
    }

    public AttachAdapter add(Attach attach) {
        datas.add(attach);
        notifyDataSetChanged();
        if (listener != null)
            listener.onAttachAdd(attach);
        return this;
    }

    public AttachAdapter remove(Attach attach) {
        int index = datas.indexOf(attach);
        datas.remove(attach);
        notifyDataSetChanged();
        if (listener != null) {
            listener.onAttachRemove(attach);
            listener.onAttachRemove(attach, index);
        }
        return this;
    }

    public void removable(boolean removable) {
        this.removable = removable;
    }

    public void showName(boolean showName) {
        this.showName = showName;
    }

    public interface EventListener {

        default void onAttachAdd(Attach attach) {
        }

        default void onAiIamge(Uri uri) {
        }

        default void onAttachRemove(Attach attach) {
        }

        default void onAttachRemove(Attach attach, int index) {
        }
    }
}

