package com.code.kotlin

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut

@Aspect
class AspectRule {

    @Pointcut("execution(* com.code.kotlin.Good.doSomething(..))")
    fun anyFunction() {
    }

    @Before("anyFunction()")
    fun beforeFunction(joinPoint: JoinPoint) {
        println("before function executed: $joinPoint")
    }

    @After("anyFunction()")
    fun afterFunction(joinPoint: JoinPoint) {
        println("after function executed: $joinPoint")
    }
}