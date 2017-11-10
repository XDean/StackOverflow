package xdean.stackoverflow.rx

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    val map = Observable.interval(100, TimeUnit.MILLISECONDS)
            .map { newData() }
            .toInfiniteMap(Data::tag)
    Observable.interval(500, TimeUnit.MILLISECONDS)
            .take(10)
            .doOnNext { println(map.keys) }
            .blockingSubscribe()
}

fun <T, K> Observable<T>.toInfiniteMap(keySelector: (T) -> K): Map<K, Observable<T>> {
    val map = ConcurrentHashMap<K, Observable<T>>()
    this.subscribeOn(Schedulers.newThread())
            .doOnNext { println(it) }
            .groupBy(keySelector)
            .doOnNext { map.put(it.key!!, it) }
            .subscribe()
    return map
}

fun <T, K> Observable<T>.toInfiniteMapObservable(keySelector: (T) -> K):
        Observable<Map<K, Observable<T>>> {
    val map = ConcurrentHashMap<K, Observable<T>>()
    return this.subscribeOn(Schedulers.newThread())
            .doOnNext { println(it) }
            .groupBy(keySelector)
            .doOnNext { map.put(it.key!!, it) }
            .map { map }
}

fun newData() = Data((Math.random() * 10).toInt(), System.currentTimeMillis())

data class Data(val tag: Int, val time: Long)