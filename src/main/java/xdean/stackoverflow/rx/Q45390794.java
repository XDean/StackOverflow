package xdean.stackoverflow.rx;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class Q45390794 {

  public static void main(String[] args) {
    List<User> users = new ArrayList<>();
    List<MinorUser> mUsers = new ArrayList<>();

    mUsers.forEach(mu -> users.add(new User(mu.a, mu.b)));

    Observable.from(mUsers)
        .map(mu -> new User(mu.a, mu.b))
        .forEach(users::add);
  }

  static class User {
    int a;
    String b;

    public User(int a, String b) {
      this.a = a;
      this.b = b;
    }
  }

  static class MinorUser {
    int a;
    String b;
  }
}
