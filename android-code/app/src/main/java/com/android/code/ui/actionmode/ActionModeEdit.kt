package com.android.code.ui.actionmode

import android.content.Context
import android.util.AttributeSet
import android.view.ActionMode
import androidx.appcompat.R
import androidx.appcompat.widget.AppCompatEditText

class ActionModeEdit : AppCompatEditText {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.editTextStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        customSelectionActionModeCallback = ActionModeCallback()
        customInsertionActionModeCallback = ActionModeCallback()
    }

    override fun startActionMode(delegate: ActionMode.Callback, type: Int): ActionMode {
        val callback = ActionModeCallback2(delegate)
        return super.startActionMode(callback, ActionMode.TYPE_FLOATING)
    }
}