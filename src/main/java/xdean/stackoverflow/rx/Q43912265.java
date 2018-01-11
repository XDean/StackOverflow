package xdean.stackoverflow.rx;

import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.util.Optional;
import java.util.Random;

import rx.Observable;
import rx.schedulers.Schedulers;

public class Q43912265 {

  static Random random = new Random();

  public static void main(String[] args) {
    Observable.range(1, 1000)
        .map(String::valueOf)
        .flatMap(id -> Observable.just(id)
            .observeOn(Schedulers.io())
            .map(Q43912265::runRequest))
        .filter(ur -> ur.getUser().isPresent())
        .doOnNext(Q43912265::insert)
        .subscribe();
  }

  static UserReport runRequest(String id) {
    System.out.printf("request %s on %s\n", id, Thread.currentThread());
    uncheck(() -> Thread.sleep(random.nextInt(1000)));
    System.out.printf("done %s on %s\n", id, Thread.currentThread());
    return new UserReport(id, Optional.ofNullable(random.nextDouble() > 0.7 ? null : new User(random.nextInt())));
  }

  static void insert(UserReport ur) {
    System.err.printf("insert %s on %s\n", ur, Thread.currentThread());
  }
}

class UserReport {
  String id;
  Optional<User> user;

  public UserReport(String id, Optional<User> user) {
    this.id = id;
    this.user = user;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Optional<User> getUser() {
    return user;
  }

  public void setUser(Optional<User> user) {
    this.user = user;
  }
}

class User {
  int id;

  public User(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}