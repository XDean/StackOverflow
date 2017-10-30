package xdean.stackoverflow.rx;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;

public class Q47000589 {
  public static void main(String[] args) {
    Property<Integer> p = new Property<>(1);
    Disposable d = p.listen().subscribe(i -> System.out.println("Listen: " + i));
    p.set(2);
    p.set(3);
    d.dispose();
    p.set(4);
    p.set(5);
  }

  public static class Property<T> {
    T value;
    List<ObservableEmitter<? super T>> emitters = new LinkedList<>();

    public Property(T value) {
      this.value = value;
    }

    T get() {
      return value;
    }

    void set(T t) {
      this.value = t;
      emitters.forEach(e -> e.onNext(t));
    }

    Observable<T> listen() {
      return Observable.create(e -> {
        emitters.add(e);
        e.onNext(value);
        e.setCancellable(() -> emitters.remove(e));
      });
    }
  }
}
