package xdean.stackoverflow.rx;

import io.reactivex.Observable;

public class Q44605815 {
  @SuppressWarnings("null")
  public static void main(String[] args) {
    Observable<Observable<Integer>> o = null;
    o.flatMap(inner -> inner.take(1).map(i -> inner));
  }
}
