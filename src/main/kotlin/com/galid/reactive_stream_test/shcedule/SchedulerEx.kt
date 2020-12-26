package com.galid.reactive_stream_test.shcedule

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.springframework.scheduling.concurrent.CustomizableThreadFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SchedulerEx {
}

fun main() {
    val pub: Publisher<Int> = Publisher<Int> { s ->
        s.onSubscribe(object : Subscription {
            override fun request(n: Long) {
                println("${Thread.currentThread()} on request")
                s.onNext(1)
                s.onNext(2)
                s.onNext(3)
                s.onComplete()
            }

            override fun cancel() {
            }
        })
    }

//    val subOnPub: Publisher<Int> = object: Publisher<Int> {
//        override fun subscribe(s: Subscriber<in Int>?) {
//            val exe: ExecutorService = Executors.newSingleThreadExecutor(object: CustomizableThreadFactory() {
//                override fun getThreadNamePrefix(): String {
//                    return "subOn - "
//                }
//
//            })
//            exe.execute{ pub.subscribe(s) }
//        }
//    }

    val pubOnPub: Publisher<Int> = object: Publisher<Int> {
        override fun subscribe(sub: Subscriber<in Int>?) {
            pub.subscribe(object: Subscriber<Int> {
                val exe: ExecutorService = Executors.newSingleThreadExecutor(object: CustomizableThreadFactory() {
                    override fun getThreadNamePrefix(): String {
                        return "pubOn - "
                    }

                })

                override fun onSubscribe(s: Subscription?) {
                    sub?.onSubscribe(s)
                }

                override fun onNext(t: Int?) {
                    exe.execute { sub?.onNext(t!!) }
                }

                override fun onError(t: Throwable?) {
                    exe.execute { sub?.onError(t) }
                    exe.shutdown()
                }

                override fun onComplete() {
                    exe.execute { sub?.onComplete() }
                    exe.shutdown()
                }
            })
        }
    }

    val sub: Subscriber<Int> = object: Subscriber<Int> {
        override fun onSubscribe(s: Subscription?) {
            println("${Thread.currentThread()} on Subscribe")
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
    }

    pubOnPub.subscribe(sub)
    println("${Thread.currentThread()} exit")
}