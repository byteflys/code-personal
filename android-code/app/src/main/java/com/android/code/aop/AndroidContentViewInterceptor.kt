package com.android.code.aop

import android.graphics.Color
import android.view.View
import com.flyjingfish.android_aop_annotation.ProceedJoinPoint
import com.flyjingfish.android_aop_annotation.base.BasePointCut

class AndroidContentViewInterceptor : BasePointCut<AndroidContentViewPointcut> {

    override fun invoke(
        joinPoint: ProceedJoinPoint,
        annotation: AndroidContentViewPointcut
    ): Any? {
        val target = joinPoint.target!!.javaClass.simpleName
        val pointcut = annotation.args[0]
        val contentView = joinPoint.args!![0] as View
        contentView.setBackgroundColor(Color.YELLOW)
        println("AOP AndroidContentViewInterceptor: $target $pointcut")
        println("AOP AndroidContentViewInterceptor: before target executed")
        val result = joinPoint.proceed()
        println("AOP AndroidContentViewInterceptor: after target executed")
        return result
    }
}