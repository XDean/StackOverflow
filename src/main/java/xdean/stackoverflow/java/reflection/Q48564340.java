package xdean.stackoverflow.java.reflection;

import java.lang.reflect.Method;

public class Q48564340 {

  public static void main(String[] args) throws Exception {
    Method fooA = Enum.A.getClass().getMethod("foo");
    Method barA = Enum.A.getClass().getMethod("bar");
    System.out.println(fooA.getDeclaringClass() == Inter.class);// false
    System.out.println(barA.getDeclaringClass() == Inter.class);// true
  }

  interface Inter {
    default void foo() {
      System.out.println("Q48564340.Inter.foo()");
    }

    default void bar() {
      System.out.println("Q48564340.Inter.bar()");
    }
  }

  enum Enum implements Inter {
    A {
      @Override
      public void foo() {
        System.out.println("Q48564340.Enum.A.{...}.foo()");
      }
    }
  }
}
