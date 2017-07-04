package xdean.stackoverflow.rx;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class Q44881419 {
  public static void main(String[] args) {
    Object[] os = new Object[1];
    Boolean[] bs = new Boolean[1];
    System.out.println(os.getClass().isAssignableFrom(bs.getClass()));
    System.out.println(bs.getClass().isAssignableFrom(os.getClass()));
    List<Observable<Boolean>> memberObservables = new ArrayList<>();
    Observable.zip(memberObservables, new Function<Object[], Boolean>() {
      @Override
      public Boolean apply(@NonNull Object[] booleen) throws Exception { // lint error
        return null;
      }
    });
  }
}
