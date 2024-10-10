package com.bennyhuo.kotlin.coroutines

import com.bennyhuo.kotlin.coroutines.context.CoroutineName
import com.bennyhuo.kotlin.coroutines.core.BlockingCoroutine
import com.bennyhuo.kotlin.coroutines.core.BlockingQueueDispatcher
import com.bennyhuo.kotlin.coroutines.core.DeferredCoroutine
import com.bennyhuo.kotlin.coroutines.core.StandaloneCoroutine
import com.bennyhuo.kotlin.coroutines.dispatcher.DispatcherContext
import com.bennyhuo.kotlin.coroutines.dispatcher.Dispatchers
import com.bennyhuo.kotlin.coroutines.scope.CoroutineScope
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.createCoroutineUnintercepted
import kotlin.coroutines.intrinsics.intercepted

private var coroutineIndex = AtomicInteger(0)

fun CoroutineScope.launch(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit): Job {
    val completion = StandaloneCoroutine(newCoroutineContext(context))
    val c1 = block.createCoroutineUnintercepted(completion, completion)
    val c2 = c1.intercepted()
    c2.resume(Unit)
    return completion
}

fun <T> CoroutineScope.async(context: CoroutineContext = Dispatchers.Default, block: suspend CoroutineScope.() -> T): Deferred<T> {
    val completion = DeferredCoroutine<T>(newCoroutineContext(context))
    block.startCoroutine(completion, completion)
    return completion
}

fun CoroutineScope.newCoroutineContext(context: CoroutineContext): CoroutineContext {
    val combined = scopeContext + context + CoroutineName("@coroutine#${coroutineIndex.getAndIncrement()}")
    return if (combined !== Dispatchers.Default && combined[ContinuationInterceptor] == null) combined + Dispatchers.Default else combined
}

fun <T> runBlocking(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> T): T {
    val eventQueue = BlockingQueueDispatcher()
    //ignore interceptor passed from outside.
    val newContext = context + DispatcherContext(eventQueue)
    val completion = BlockingCoroutine<T>(newContext, eventQueue)
    block.startCoroutine(completion, completion)
    return completion.joinBlocking()
}

