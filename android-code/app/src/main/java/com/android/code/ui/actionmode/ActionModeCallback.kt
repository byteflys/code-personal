package com.android.code.ui.actionmode

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import com.android.code.R

class ActionModeCallback : ActionMode.Callback {

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        menu.clear()
        mode.menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.select -> println("select")
            R.id.cancel -> println("cancel")
        }
        mode.finish()
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode) {

    }
}