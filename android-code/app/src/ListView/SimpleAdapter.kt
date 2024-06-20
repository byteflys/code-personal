package com.android.code

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class SimpleAdapter : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = TextView(parent.context)
        textView.text = "Hello"
        return object : ViewHolder(textView) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        
    }

    override fun getItemCount() = 100
}