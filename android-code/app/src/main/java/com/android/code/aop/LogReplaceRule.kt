package com.android.code.aop

import android.widget.Toast
import com.android.code.Global
import com.flyjingfish.android_aop_annotation.anno.AndroidAopReplaceClass
import com.flyjingfish.android_aop_annotation.anno.AndroidAopReplaceMethod

@AndroidAopReplaceClass("android.util.Log")
object LogReplaceRule {

    @JvmStatic
    @AndroidAopReplaceMethod("int e(java.lang.String,java.lang.String)")
    fun e(tag: String, msg: String): Int {
        Toast.makeText(Global.application, "$tag  $msg", Toast.LENGTH_LONG).show()
        return 0
    }
}

