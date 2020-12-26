package com.galid.reactive_stream_test.shcedule

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {
    val pub: Publisher<Int> = Publisher<Int> { s ->
        s.onSubscribe(object : Subscription {
            var no = 0
            val exec = Executors.newSingleThreadScheduledExecutor()
            var cancelled = false

            override fun request(n: Long) {
                exec.scheduleAtFixedRate(
                    {
                        if (cancelled)
                            exec.shutdown()
                        s.onNext(no++)
                    },
                    0,
                    500,
                    TimeUnit.MILLISECONDS
                )
            }

            override fun cancel() {
                cancelled = true
            }
        })
    }

    val takePub: Publisher<Int> = Publisher<Int> { sub ->
        pub.subscribe(object: Subscriber<Int> {
            var count = 0
            var subscription: Subscription? = null

            override fun onSubscribe(s: Subscription?) {
                sub.onSubscribe(s)
                subscription = s
            }

            override fun onNext(t: Int?) {
                if (++count == 10) {
                    subscription?.cancel()
                    onComplete()
                }
                sub.onNext(t)
            }

            override fun onError(t: Throwable?) {
                sub.onError(t)
            }

            override fun onComplete() {
                sub.onComplete()
            }

        })
    }

    takePub.subscribe(object: Subscriber<Int> {
        override fun onSubscribe(s: Subscription?) {
            println("${Thread.currentThread()} on subscribe")
            s?.request(Long.MAX_VALUE)
        }

        override fun onNext(t: Int?) {
            println("${Thread.currentThread()} on next $t")
        }

        override fun onError(t: Throwable?) {
            println("${Thread.currentThread()} on error")
        }

        override fun onComplete() {
            println("${Thread.currentThread()} on complete")
        }
    })
}