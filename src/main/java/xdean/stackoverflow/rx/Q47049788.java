package xdean.stackoverflow.rx;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class Q47049788 {
  public static void main(String[] args) {
    BehaviorSubject<String> s1 = BehaviorSubject.create();
    BehaviorSubject<String> s2 = BehaviorSubject.create();
    Observable.combineLatest(s1.scan(new ArrayList<>(), (l, s) -> {
      l.add(s);
      return l;
    }), s2, (list, s) -> Observable.fromIterable(list).map(a -> a + s))
        .concatMap(o -> o)
        .subscribe(System.out::println);
    s1.onNext("a");
    s1.onNext("b");
    s1.onNext("c");
    s2.onNext("1");
  }
}
