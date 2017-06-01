package xdean.stackoverflow.rx;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import java.util.concurrent.TimeUnit;

public class Q44278474 {
  public static void main(String[] args) {
    PublishSubject<String> publishSubject = PublishSubject.create();
    publishSubject
        .filter(s -> !s.isEmpty())
        .debounce(1000, TimeUnit.MILLISECONDS)
        .flatMap(s -> getInt())
        .mergeWith(getMerge())
        .subscribe(integer -> {
          System.out.print(integer + " ");
        });
    publishSubject.onNext("a");
    publishSubject.onComplete();
  }

  private static Observable<Integer> getInt() {
    return Observable.range(1, 5);
  }

  private static Observable<Integer> getMerge() {
    return Observable.range(5, 5);
  }
}