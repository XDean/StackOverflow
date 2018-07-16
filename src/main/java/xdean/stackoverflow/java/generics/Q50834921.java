package xdean.stackoverflow.java.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.BehaviorSubject;

public class Q50834921 {

  public static void main(String[] args) {
    List<? extends Dummy> list = new ArrayList<>();
    BehaviorSubject<List<? extends Dummy>> subject = BehaviorSubject.createDefault(list);

    // #1 -> ok
    BehaviorSubject.createDefault(list)
        .compose(ListFilter.create(item -> true));

    // #2 -> error
    subject
        .compose(ListFilter.create(item -> true));

    // #3 -> ok
    subject
        .flatMap(d -> Observable.just(d))
        .compose(ListFilter.create(item -> true));
  }
}

class Dummy {
}

class ListFilter<T> implements ObservableTransformer<List<? extends T>, List<? extends T>> {

  public static <T> ListFilter<T> create(Predicate<T> predicate) {
    return new ListFilter<>(predicate);
  }

  private final Predicate<T> predicate;

  private ListFilter(Predicate<T> predicate) {
    this.predicate = predicate;
  }

  @Override
  public ObservableSource<List<? extends T>> apply(Observable<List<? extends T>> upstream) {
    return upstream
        .flatMapSingle(list -> Observable.fromIterable(list)
            .filter(predicate::test)
            .toList());
  }
}