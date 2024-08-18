package com.android.code.aop

import com.flyjingfish.android_aop_annotation.ProceedJoinPoint
import com.flyjingfish.android_aop_annotation.anno.AndroidAopMatchClassMethod
import com.flyjingfish.android_aop_annotation.base.MatchClassMethod
import com.flyjingfish.android_aop_annotation.enums.MatchType

@AndroidAopMatchClassMethod(
    targetClassName = "android.app.Activity",
    methodName = ["void onCreate(android.os.Bundle)"],
    type = MatchType.LEAF_EXTENDS
)
class AndroidActivityCreateInterceptor : MatchClassMethod {

    override fun invoke(
        joinPoint: ProceedJoinPoint,
        methodName: String
    ): Any? {
        val target = joinPoint.target!!.javaClass.simpleName
        println("AOP AndroidActivityCreateInterceptor: $target onCreate")
        return joinPoint.proceed()
    }
}