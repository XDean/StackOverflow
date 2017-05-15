package xdean.stackoverflow.rx;

import java.util.ArrayList;
import java.util.List;

import lombok.Value;
import rx.Observable;
import xdean.jex.extra.Pair;

public class Q43736186 {
  public static void main(String[] args) {
    List<Parent> parents = new ArrayList<>();
    parents.add(new Parent(1, "Parent1"));
    parents.add(new Parent(2, "Parent2"));
    parents.add(new Parent(3, "Parent3"));
    parents.add(new Parent(4, "Parent4"));
    parents.add(new Parent(5, "Parent5"));
    Observable.from(parents)
        .concatMap(p -> getChilds(p.getId())
            .map(Response::getValue)
            .<Pair<Parent, List<Child>>> map(children -> Pair.of(p, children)))
        .toMap(Pair::getLeft, Pair::getRight);
  }

  static Observable<Response<List<Child>>> getChilds(int parentId) {
    return null;
  }

  @Value
  private static class Parent {
    int id;
    String name;
  }

  @Value
  private static class Child {
    int id;
    String name;
  }

  @Value
  private static class Response<T> {
    T value;
  }
}
