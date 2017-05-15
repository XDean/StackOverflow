package xdean.stackoverflow.rx;

import java.util.Optional;
import java.util.Random;

import lombok.SneakyThrows;
import lombok.Value;
import rx.Observable;
import rx.schedulers.Schedulers;

public class Q43912265 {

  static Random random = new Random();

  public static void main(String[] args) {
    Observable.range(1, 1000)
        .map(String::valueOf)
        .flatMap(id ->
            Observable.just(id)
                .observeOn(Schedulers.io())
                .map(Q43912265::runRequest))
        .filter(ur -> ur.getUser().isPresent())
        .doOnNext(Q43912265::insert)
        .subscribe();
  }

  @SneakyThrows(InterruptedException.class)
  static UserReport runRequest(String id) {
    System.out.printf("request %s on %s\n", id, Thread.currentThread());
    Thread.sleep(random.nextInt(1000));
    System.out.printf("done %s on %s\n", id, Thread.currentThread());
    return new UserReport(id, Optional.ofNullable(random.nextDouble() > 0.7 ? null : new User(random.nextInt())));
  }

  static void insert(UserReport ur) {
    System.err.printf("insert %s on %s\n", ur, Thread.currentThread());
  }
}

@Value
class UserReport {
  String id;
  Optional<User> user;
}

@Value
class User {
  int id;
}