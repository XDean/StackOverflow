package xdean.stackoverflow.rx;

import java.util.concurrent.TimeUnit;

import rx.Observable;

public class Q43859499 {
  public static void main(String[] args) {
    Observable.unsafeCreate(s -> {
      System.out.println("subscribing");
      s.onNext("123");
      s.onError(new RuntimeException("always fails"));
    }).retryWhen(attempts -> {
      System.out.println("attemp");
      return attempts.doOnNext(System.out::println)
          .zipWith(Observable.range(1, 3), (n, i) -> i).flatMap(i -> {
            System.out.println("delay retry by " + i + " second(s)");
            return Observable.timer(i, TimeUnit.SECONDS);
          });
    }).toBlocking().forEach(System.out::println);
  }
}
