package com.galid.reactive_stream_test

import java.util.concurrent.Flow
import java.util.concurrent.Flow.*

class LogSub<T>: Subscriber<T> {
    override fun onSubscribe(subscription: Subscription?) {
        println("on Subscribe")
        subscription?.request(Long.MAX_VALUE)
    }

    override fun onNext(item: T?) {
        println("on Next $item")
    }

    override fun onError(throwable: Throwable?) {
        println("on error")
    }

    override fun onComplete() {
        println("on complete")
    }
}