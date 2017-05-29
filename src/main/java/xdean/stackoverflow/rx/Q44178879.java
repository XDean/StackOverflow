package xdean.stackoverflow.rx;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Q44178879 {
  public static void main(final String[] args) {
    ExecutorService pool = Executors.newCachedThreadPool();
    final Scheduler scheduler = Schedulers.from(pool);

    Observable.interval(1L, TimeUnit.SECONDS)
        .take(1)
        .subscribeOn(scheduler)
        .map(x -> x + "s")
        .doOnComplete(()->{
          scheduler.shutdown();
          pool.shutdown();
        })
        .subscribe(x -> {
          System.out.println(x);
        });
  }
}
