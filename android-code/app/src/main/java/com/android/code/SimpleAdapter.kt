package com.android.code

import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class SimpleAdapter : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = TextView(parent.context)
        textView.text = "Hello"
        textView.gravity = Gravity.CENTER
        textView.layoutParams = MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return object : ViewHolder(textView) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount() = 1000
}