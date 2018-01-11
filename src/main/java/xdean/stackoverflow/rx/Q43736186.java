package xdean.stackoverflow.rx;

import java.util.ArrayList;
import java.util.List;

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

  static class Parent {
    final int id;
    final String name;

    public Parent(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public int getId() {
      return id;
    }
  }

  static class Child {
    final int id;
    final String name;

    public Child(int id, String name) {
      this.id = id;
      this.name = name;
    }

  }

  static class Response<T> {
    final T value;

    public Response(T value) {
      this.value = value;
    }

    public T getValue() {
      return value;
    }
  }
}
