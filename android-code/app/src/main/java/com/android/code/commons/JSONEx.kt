package com.android.code.commons

import com.google.gson.Gson

object JSONEx {

    private val gson = Gson()

    fun gson() = Gson()

    fun Any?.toJson(): String {
        if (this == null) {
            return ""
        }
        return gson.toJson(this)
    }

    fun <T> String?.fromJson(clazz: Class<T>): T? {
        if (this == null) {
            return null
        }
        return gson.fromJson(this, clazz)
    }
}