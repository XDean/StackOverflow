package xdean.stackoverflow.rx;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class Q47000608 {
  public static void main(String[] args) {
    BehaviorSubject<Integer> bs = BehaviorSubject.createDefault(1);
    Observable<Integer> o = bs.replay(1).autoConnect().distinctUntilChanged();
    o.subscribe(i -> System.out.println("s1 accept " + i));
    bs.onNext(2);
    o.subscribe(i -> System.out.println("s2 accept " + i));
    o.subscribe(i -> System.out.println("s3 accept " + i));
    bs.onNext(3);
    o.subscribe(i -> System.out.println("s4 accept " + i));
    bs.onNext(4);
  }
}
