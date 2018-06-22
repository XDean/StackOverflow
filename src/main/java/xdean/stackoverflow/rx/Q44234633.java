package xdean.stackoverflow.rx;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class Q44234633 {
  public static void main(String[] args) throws InterruptedException {
    Observable.interval(50, TimeUnit.MILLISECONDS)
        .takeWhile(l -> l < 8)
        .observeOn(Schedulers.io())
        .filter(l -> isConditionTrue(l))
        .observeOn(Schedulers.computation())
        .firstElement()
        .doOnSuccess(System.out::println)
        .switchIfEmpty((MaybeSource<? extends Long>)o -> o.onSuccess(-1L))
        .isEmpty()
        .filter(empty -> empty)
        .doOnSuccess(b -> System.out.println("TimeOut"))
        .blockingGet();
  }

  private static boolean isConditionTrue(long time) {
    return time > ThreadLocalRandom.current().nextInt(5, 20 + 1);
  }
}
