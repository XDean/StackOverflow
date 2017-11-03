package xdean.stackoverflow.java.Q46864834;

import java.util.Arrays;

public class Main {

  public static void main(String[] args) {
    // sample();
     sampleTwo();
//    sampleWithError();
  }

  public static void sample() {
    Resource resourceA = new Resource("A");
    LockVisitor.create(resourceA)
        .lock()// lock A
        .doOnValue(Main::doSomething)
        .doOnValue(Main::doSomething)
        .close();// unlock A
  }

  public static void sampleWithError() {
    Resource resourceA = new Resource("A");
    LockVisitor.create(resourceA)
        .lock()// lock A
        .doOnError(e -> System.out.println("the error:" + e))// error not happened, so print nothing
        .doOnValue(Main::error)// do for A
        .doOnValue(Main::doSomething)
        .doOnError(e -> System.out.println("the error:" + e))// error happened, print
        .close();// unlock A
  }

  public static void sampleTwo() {
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

  private static void doSomething(Resource... rs) {
    System.out.println("do with: " + Arrays.toString(rs));
  }

  private static void error(Resource... rs) {
    System.out.println("error: " + Arrays.toString(rs));
    throw new RuntimeException("error message: " + Arrays.toString(rs));
  }
}
