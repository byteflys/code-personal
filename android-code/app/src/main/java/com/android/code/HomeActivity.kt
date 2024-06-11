package com.android.code

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.View.DRAG_FLAG_OPAQUE
import android.view.View.DragShadowBuilder
import androidx.appcompat.app.AppCompatActivity
import com.android.code.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addEvents(binding.image1)
        addEvents(binding.image2)
    }

    private fun addEvents(cell: View) {
        cell.setOnLongClickListener {
            val data = ClipData.newIntent("xxx", Intent())
            val shadow = DragShadowBuilder(cell)
            val flag = DRAG_FLAG_OPAQUE
            binding.root.startDragAndDrop(data, shadow, null, flag)
            binding.root.setOnDragListener { v, event ->
                if (event.y > binding.button.y) {
                    binding.root.cancelDragAndDrop()
                }
                if (event.action == DragEvent.ACTION_DRAG_ENDED) {
                    binding.root.cancelDragAndDrop()
                    onDrop()
                }
                return@setOnDragListener true
            }
            return@setOnLongClickListener true
        }
    }

    private fun onDrop() {
        println("onDrop")
    }
}