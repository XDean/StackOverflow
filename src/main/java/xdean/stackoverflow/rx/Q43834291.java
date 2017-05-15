package xdean.stackoverflow.rx;

import java.util.List;

import rx.Observable;

public class Q43834291 {
  public static void main(String[] args) {
    Observable<Integer> obs = Observable.just(1, 2, 3, 4, 5);
    List<Integer> count = obs.groupBy(i -> i % 2)
        .sorted((g1, g2) -> g1.getKey() - g2.getKey())
        .concatMap(go -> go.count())
        .toList()
        .toBlocking()
        .first();
    obs.map(i -> i * count.get(i % 2))
        .forEach(System.out::println);
  }
}
