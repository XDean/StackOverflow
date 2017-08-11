package xdean.stackoverflow.rx;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import java.util.List;
import java.util.Random;
import java.util.function.IntPredicate;

import xdean.jex.extra.Pair;

public class Q45540738 {
  static Random random = new Random();

  public static void main(String[] args) throws InterruptedException {
    Single<List<Pair<Integer, Disposable>>> tasks = Observable.range(0, 50)
        .map(i -> Pair.of(i, download(i).subscribe()))
        .toList();
    Subject<IntPredicate> stopers = BehaviorSubject.createDefault(i -> true);
    Observable.combineLatest(tasks.toObservable(), stopers, (l, s) -> {
      l.removeIf(p -> {
        if (s.test(p.getLeft()) == false) {
          p.getRight().dispose();
        }
        return p.getRight().isDisposed();
      });
      return l;
    }).subscribe();
    Thread.sleep(2000);
    stopers.onNext(i -> i % 2 == 0);
    Thread.sleep(10000);
  }

  static Observable<Integer> download(int id) {
    return Observable.just(random.nextInt(1000) + 500)
        .observeOn(Schedulers.computation())
        .doOnNext(t -> Thread.sleep(t))
        .doOnDispose(() -> System.err.printf("%d task stoped on %s\n", id, Thread.currentThread()))
        .doOnNext(t -> System.out.printf("%d task done on %s\n", id, Thread.currentThread()));
  }
}
