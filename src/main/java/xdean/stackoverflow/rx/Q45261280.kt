package xdean.stackoverflow.rx

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
	Observable.interval(0, 334, TimeUnit.MILLISECONDS)
			.take(9)
			.doOnComplete { Thread.sleep(3000) }
			.doOnNext { println(it) }
			.doOnComplete { println("completed") }
			.blockingSubscribe()
}