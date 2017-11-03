package xdean.stackoverflow.java.Q46864834;

import java.util.Arrays;

public class Main {

  public static void main(String[] args) {
    sampleWithError();
  }

  public static void sample() {
    Resource resourceA = new Resource("A");
    Resource resourceB = new Resource("B");
    LockVisitor.create(resourceA)
        .lock()// lock A
        .doOnValue(Main::doSomething)// do for A
        .with(resourceB)// join with B
        .lock()// lock A & B (A has been locked)
        .doOnBoth(Main::doSomething)// do for A and B
        .toRight()// only need B (unlock A)
        .doOnValue(Main::doSomething)// do for B
        .close();// unlock B
  }

  public static void sampleWithError() {
    Resource resourceA = new Resource("A");
    Resource resourceB = new Resource("B");
    LockVisitor.create(resourceA)
        .lock()// lock A
        .doOnValue(Main::error)// do for A
        .with(resourceB)// join with B
        .lock()// lock A & B (A has been locked)
        .doOnBoth(Main::doSomething)// do for A and B
        .toRight()// only need B (unlock A)
        .doOnValue(Main::doSomething)// do for B
        .close();// unlock B
  }

  private static void doSomething(Resource... rs) {
    System.out.println("do with: " + Arrays.toString(rs));
  }

  private static void error(Resource... rs) {
    System.out.println("error: " + Arrays.toString(rs));
    throw new RuntimeException("error message: " + Arrays.toString(rs));
  }
}
