package com.android.code.ui.actionmode

import android.graphics.Rect
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View

// control display rect of menu
class ActionModeCallback2(
    private val delegate: ActionMode.Callback
) : ActionMode.Callback2() {

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        return delegate.onCreateActionMode(mode, menu)
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return delegate.onPrepareActionMode(mode, menu)
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        return delegate.onActionItemClicked(mode, item)
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        delegate.onDestroyActionMode(mode)
    }

    override fun onGetContentRect(mode: ActionMode, view: View, outRect: Rect) {
        outRect.set(0, 0, view.width, view.height)
    }
}