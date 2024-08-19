package com.android.code.aop

import com.flyjingfish.android_aop_annotation.anno.AndroidAopReplaceClass
import com.flyjingfish.android_aop_annotation.anno.AndroidAopReplaceMethod

@AndroidAopReplaceClass("android.util.Log")
object AndroidLogInterceptor {

    @JvmStatic
    @AndroidAopReplaceMethod("int e(java.lang.String,java.lang.String)")
    fun e(tag: String, msg: String): Int {
        println("AOP AndroidLogInterceptor: $tag $msg")
        return 0
    }
}