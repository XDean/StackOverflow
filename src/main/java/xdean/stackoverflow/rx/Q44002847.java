package xdean.stackoverflow.rx;

import rx.Observable;

public class Q44002847 {
  public static void main(String[] args) {
    Observable
        .just(1, 2, 3)
        .concatWith(Observable.error(new Throwable()))
        .retryWhen(notificationHandler -> Observable.never())
        .doOnError(e -> System.out.println(e))
        .doOnCompleted(() -> System.out.println("complete"))
        .forEach(s -> System.out.println(s));
  }
}
