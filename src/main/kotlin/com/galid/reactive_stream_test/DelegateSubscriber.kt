package com.galid.reactive_stream_test

import java.util.concurrent.Flow
import java.util.concurrent.Flow.*

open class DelegateSubscriber<T>(
    val originSubscriber: Subscriber<T>
): Subscriber<T>{
    override fun onSubscribe(subscription: Subscription?) {
        originSubscriber.onSubscribe(subscription)
    }

    override fun onNext(item: T?) {
        originSubscriber.onNext(item)
    }

    override fun onError(throwable: Throwable?) {
        originSubscriber.onError(throwable)
    }

    override fun onComplete() {
        originSubscriber.onComplete()
    }
}