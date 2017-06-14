package xdean.stackoverflow.rx;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import xdean.jex.util.task.If;

public class Q44535765 {
  public static void main(String[] args) {
    Maybe<Element> first = get();
    first.toObservable()
        .compose(o -> chain(o))
        .doOnError(e -> System.out.println(e))
        .subscribe(
            e -> System.out.println(e),
            e -> System.out.println("fail"),
            () -> System.out.println("complete"));
  }

  static Maybe<Element> get() {
    return Maybe.just(
        () -> If.<Maybe<Element>> that(Math.random() > 0.1)
            .tobe(() -> get())
            .orbe(() -> If.<Maybe<Element>> that(Math.random() > 0.5)
                .tobe(() -> Maybe.empty())
                .orbe(() -> null)
                .result())
            .result());
  }

  static Observable<Element> chain(Observable<Element> s) {
    return s.concatMap(
        e -> Observable.just(e)
            .concatWith(e.next()
                .toObservable()
                .compose(o -> chain(o))));
  }

  interface Element {
    Maybe<Element> next();
  }
}
