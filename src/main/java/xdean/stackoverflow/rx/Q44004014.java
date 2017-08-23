package xdean.stackoverflow.rx;

import rx.Observable;
import rx.schedulers.Schedulers;
import xdean.jex.util.lang.ExceptionUtil;

public class Q44004014 {
  public static void main(String[] args) throws InterruptedException {
    Observable.just("a", "b", "c")
        .zipWith(Observable.range(1, 100).observeOn(Schedulers.io()).doOnNext(System.err::println), (a, c) -> c)
        .doOnNext(i -> ExceptionUtil.uncheck(() -> Thread.sleep(500)))
        .forEach(System.out::println);
    Thread.sleep(1000);
  }
}
