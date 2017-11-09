package xdean.stackoverflow.rx;

import io.reactivex.Flowable;
import xdean.jex.util.calc.CartesianProduct;

public class Q43831465 {
  public static void main(String[] args) {
    Flowable<Integer> o1 = Flowable.just(0, 1, 2);
    Flowable<Integer> o2 = Flowable.just(2, 4, 6);
    CartesianProduct.cartesianProduct(Flowable.just(o2, o1))
        .filter(array -> array[1] != 0)
        .map(array -> array[0] / array[1])
        .forEach(System.out::println);
  }
}
