package xdean.stackoverflow.rx;

import rx.Observable;

public class Q44096689 {
  public static void main(String[] args) {
  }

  Observable<Boolean> updateAllProfiles(Integer[] ids) {
    return Observable.from(ids)
        .flatMap(id -> updateProfile(id))
        .all(b -> b);
  }

  private Observable<Boolean> updateProfile(Integer id) {
    return Observable.just(Math.random() > 0.5);
  }
}
