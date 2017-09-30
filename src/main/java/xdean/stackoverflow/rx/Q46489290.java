package xdean.stackoverflow.rx;

import io.reactivex.Observable;
import xdean.jex.extra.Pair;

public class Q46489290 {
  public <T, F> Observable<T> answer(Observable<T> values, Observable<F> trigger) {
    return Observable.combineLatest(values, trigger, Pair::of)
        .distinctUntilChanged(p -> p.getRight())
        .map(Pair::getLeft);
  }
}
