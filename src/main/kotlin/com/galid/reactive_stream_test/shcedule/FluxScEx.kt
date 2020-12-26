package com.galid.reactive_stream_test.shcedule

import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.time.Duration
import java.util.concurrent.TimeUnit

class FluxScEx {

}

fun main() {
    Flux.interval(Duration.ofMillis(500))
        .take(10)
        .subscribe{println(it)}

    TimeUnit.SECONDS.sleep(10)
}