package com.android.code.commons

import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class SimpleAdapter : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val button = Button(parent.context)
        button.text = "hello"
        button.gravity = Gravity.CENTER
        button.isAllCaps = false
        return object : ViewHolder(button) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount() = 1000
}