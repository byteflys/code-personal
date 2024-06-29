package com.android.app

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData

class LiveObject : LifecycleOwner {

    override val lifecycle = LifecycleRegistry(this)

    constructor() {
        lifecycle.currentState = Lifecycle.State.CREATED
    }

    fun start() {
        lifecycle.currentState = Lifecycle.State.RESUMED
    }

    fun dispose() {
        lifecycle.currentState = Lifecycle.State.DESTROYED
    }
}

class CallingObject {

    private val lifecycleObject = LiveObject()

    private val observable = MutableLiveData("")

    fun doSomethingWhenObjectIsAlive() {
        observable.observe(lifecycleObject) {
            println("event: $it")
        }
    }
}