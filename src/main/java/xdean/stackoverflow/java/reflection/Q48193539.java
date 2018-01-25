package xdean.stackoverflow.java.reflection;

import java.util.Arrays;

import xdean.jex.util.reflect.GenericUtil;

public class Q48193539 {
  public class A<T> implements B<Z> {
  }

  public interface B<T> extends C<Z> {
  }

  public interface C<T> {
  }

  public class Z {
  }

  public static void main(String[] args) {
    System.out.println(Arrays.toString(GenericUtil.getGenericTypes(A.class, B.class)));
    System.out.println(Arrays.toString(GenericUtil.getGenericTypes(A.class, C.class)));
    System.out.println(Arrays.toString(GenericUtil.getGenericTypes(B.class, C.class)));
    System.out.println(Arrays.toString(GenericUtil.getGenericTypes(A.class, Z.class)));
  }
}
