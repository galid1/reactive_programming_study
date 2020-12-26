package com.galid.reactive_stream_test

import java.util.concurrent.Flow
import java.util.concurrent.Flow.*
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.streams.toList

class OriginPub<T>(
    val data: Iterable<T>
): Publisher<T> {
    override fun subscribe(subscriber: Subscriber<in T>?) {
        subscriber?.onSubscribe(OriginSubscription(data, subscriber))
    }
}

class OriginSubscription<T>(
    val data: Iterable<T>,
    val subscriber: Subscriber<in T>
): Subscription {
    override fun request(n: Long) {
        // 1. iterator 생성
        // 2. while에 hasNext까지
        // 3. hasNext -> False면 subscriber의 onComplete
        val iterator = data.iterator()
        while (iterator.hasNext()) {
            subscriber.onNext(iterator.next())
        }
        subscriber.onComplete()
    }

    override fun cancel() {
    }
}

fun main() {
    val originP: OriginPub<Int> = OriginPub(
        data = Stream.iterate(0, {it + 1}).limit(10).toList()
    )
//    val mapPub: MapPub<Int> = MapPub<Int>(originP, { it * 10 })
//    val sumPub: ReducePub = ReducePub(originP, 0, { a,b -> a+b })
    val reducePub: ReducePub<Int> = ReducePub(originP, 0, {a,b -> a+b})
    reducePub.subscribe(LogSub())

}
