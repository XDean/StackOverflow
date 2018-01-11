package xdean.stackoverflow.rx;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/*
 * i    rela  abs   c     abs   rela
 * 0    0     0     0     0     0
 * 1    100   100   -300  400   400
 * 2    200   300   -500  800   400
 * 3    600   900   -300  1400  600
 * 4    2000  2900  900   3400  2000
 */
public class Q47157200 {
  public static void main(String[] args) throws Exception {
    BehaviorSubject<Integer> s = BehaviorSubject.create();
    // Three = (The value, upstream comes mills, downstream emits mills)
    s.map(i -> new Three<>(i, System.currentTimeMillis(), System.currentTimeMillis()))
        .scan((a, b) -> {
          b.setC(a.getC() + Math.max(400L, b.getB() - a.getB()));
          return b;
        })
        .concatMap(i -> Observable.just(i.getA()).delay(Math.max(0, i.getC() - System.currentTimeMillis()),
            TimeUnit.MILLISECONDS))
        .subscribe(i -> System.out.println(i + "\t" + System.currentTimeMillis()));
    s.onNext(0);
    Thread.sleep(100);
    s.onNext(1);
    Thread.sleep(200);
    s.onNext(2);
    Thread.sleep(600);
    s.onNext(3);
    Thread.sleep(2000);
    s.onNext(4);
    Thread.sleep(200);
    s.onNext(5);
    Thread.sleep(800);
    s.onNext(6);
    Thread.sleep(1000);
  }

  public static class Three<A, B, C> {
    A a;
    B b;
    C c;

    public Three(A a, B b, C c) {
      this.a = a;
      this.b = b;
      this.c = c;
    }

    public A getA() {
      return a;
    }

    public B getB() {
      return b;
    }

    public C getC() {
      return c;
    }

    public void setA(A a) {
      this.a = a;
    }

    public void setB(B b) {
      this.b = b;
    }

    public void setC(C c) {
      this.c = c;
    }
  }

}
