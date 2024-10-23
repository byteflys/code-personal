package com.easing.commons.android.ui.control.attach_viewer;

import android.content.Context;
import android.util.AttributeSet;

import com.easing.commons.android.io.Files;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.struct.StringList;
import com.easing.commons.android.thread.MainThread;
import com.easing.commons.android.ui.control.list.easy_list.EasyListView;

import java.util.List;

@SuppressWarnings("all")
public class FileListView extends EasyListView {

    final AttachAdapter attachAdapter = new AttachAdapter();

    public FileListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initFileListView(context, attributeSet);
    }

    public AttachAdapter attachAdapter() {
        return attachAdapter;
    }

    //初始化子类
    private void initFileListView(Context context, AttributeSet attributeSet) {
        horizontalLayout();
        adapter(attachAdapter);
        attachAdapter.removable(true);
        attachAdapter.showName(false);
        attachAdapter.reset(Collections.emptyList());
    }

    //设置事件监听器
    public FileListView eventListener(AttachAdapter.EventListener listener) {
        attachAdapter.listener = listener;
        return this;
    }

    //添加附件
    public FileListView add(Attach attach) {
        attachAdapter.add(attach);
        return this;
    }


    //获取附件列表
    public List<Attach> attaches() {
        return attachAdapter.getDatas();
    }

    //获取附件位置列表
    public List<String> attachPaths() {
        List<Attach> attaches = attachAdapter.getDatas();
        List<String> paths = Collections.convert(attaches, attach -> attach.file);
        return paths;
    }

    //重置附件列表
    public void resetByFileList(StringList list) {
        List<Attach> attaches = Collections.convert(list, path -> {
            Attach attach = new Attach();
            attach.file = path;
            attach.name = Files.getFileName(path);
            return attach;
        });
        attachAdapter.reset(attaches);
    }

    //重置附件列表
    public void resetByAttachList(List<Attach> attachList) {
        attachAdapter.reset(attachList);
    }

    //刷新
    public void update() {
        MainThread.post(attachAdapter::notifyDataSetChanged);
    }

    //不允许编辑
    public void editable(boolean editable) {
        attachAdapter.removable(editable);
    }
}
