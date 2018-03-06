package xdean.stackoverflow.rx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Q49112023 {
  public static void main(String[] args) throws Exception {
    Flowable<String> ob = create("a", "b", "c", "d", "e");
    Disposable d = ob.subscribeOn(Schedulers.computation())
        .subscribe(i -> System.out.println(System.currentTimeMillis() + "\t" + i));
    Thread.sleep(2500);
    d.dispose();
    ob.subscribeOn(Schedulers.computation())
        .blockingSubscribe(i -> System.err.println(System.currentTimeMillis() + "\t" + i));
  }

  @SafeVarargs
  public static <T> Flowable<T> create(T... ts) {
    List<T> list = new ArrayList<>(Arrays.asList(ts));
    return Flowable.generate(() -> list, (l, e) -> {
      if (l.isEmpty()) {
        e.onComplete();
      } else {
        e.onNext(l.remove(0));
        if (!l.isEmpty()) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException ex) {
            // you can use other way to delay it
          }
        }
      }
    });
  }
}
