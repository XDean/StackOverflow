package xdean.stackoverflow.rx;

import rx.Observable;
import xdean.jex.util.calc.MathUtil;

public class Q43831465 {
  public static void main(String[] args) {
    Observable<Integer> o1 = Observable.just(0, 1, 2);
    Observable<Integer> o2 = Observable.just(2, 4, 6);
    MathUtil.cartesianProduct(Observable.just(o2, o1))
        .filter(array -> array[1] != 0)
        .map(array -> array[0] / array[1])
        .forEach(System.out::println);
  }
}
