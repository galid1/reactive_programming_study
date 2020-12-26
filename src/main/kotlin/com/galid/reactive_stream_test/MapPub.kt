package com.galid.reactive_stream_test

import java.util.concurrent.Flow.*
import java.util.function.Function

class MapPub<T>(
    val originPub: Publisher<T>,
    val f: Function<T, T>
): Publisher<T> {
    override fun subscribe(subscriber: Subscriber<in T>) {
        originPub.subscribe(object: DelegateSubscriber<T>(subscriber as Subscriber<T>) {
            override fun onNext(item: T?) {
                subscriber.onNext(f.apply(item!!))
            }
        })
    }
}

