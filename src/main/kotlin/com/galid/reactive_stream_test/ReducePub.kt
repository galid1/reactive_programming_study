package com.galid.reactive_stream_test

import java.util.concurrent.Flow.Publisher
import java.util.concurrent.Flow.Subscriber
import java.util.function.BiFunction

class ReducePub <T>(
    val originPub: Publisher<T>,
    val initVal: T,
    val f: BiFunction<T, T, T>
): Publisher<T> {
    override fun subscribe(subscriber: Subscriber<in T>?) {
        originPub.subscribe(object: DelegateSubscriber<T>(subscriber as Subscriber<T>) {
            var result = initVal

            override fun onComplete() {
                subscriber?.onNext(result)
                subscriber?.onComplete()
            }

            override fun onNext(item: T?) {
                result = f.apply(result, item!!)
            }
        })
    }
}