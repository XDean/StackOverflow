package xdean.stackoverflow.rx;

import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class Q47264933 {

  static final long BASE_TIME = System.currentTimeMillis();
  static final Scheduler MOCK_ANDROID_MAIN = Schedulers.from(Executors.newFixedThreadPool(1));
  static final Scheduler SEQUENTIAL = Schedulers.from(Executors.newFixedThreadPool(1));

  public static void main(String[] args) throws InterruptedException {
    IntStream.range(1, 10)
        .forEach(i -> sendRequest(i).subscribe());
    Thread.sleep(10000);
  }

  private static Observable<String> sendRequest(int request) {
    return Observable.just(request)
        .observeOn(SEQUENTIAL)
        .doOnNext(i -> System.out.println(i + " start at:" + (System.currentTimeMillis() - BASE_TIME)))
        .map(i -> mockLongTimeRequest(i))
        .doOnNext(i -> System.out.println(i + " done  at:" + (System.currentTimeMillis() - BASE_TIME)))
        .observeOn(MOCK_ANDROID_MAIN);
  }

  private static String mockLongTimeRequest(int i) throws InterruptedException {
    Thread.sleep((long) (1000 * Math.random()));
    return Integer.toString(i);
  }
}
