package xdean.stackoverflow.java.reflection;

import java.lang.reflect.Type;

import xdean.jex.util.reflect.GenericUtil;

public class Q46360416 {

  public static void main(String[] args) {
    EpicCoolInterface<Integer> a = new EpicCoolInterface<Integer>() {
    };
    System.out.println(a.getParameterizedTypeClass());
    EpicCoolInterface<EpicCoolInterface<Integer>> b = new EpicCoolInterface<EpicCoolInterface<Integer>>() {
    };
    System.out.println(b.getParameterizedTypeClass());
    EpicCoolInterface<EpicCoolInterface<?>> c = new EpicCoolInterface<EpicCoolInterface<?>>() {
    };
    System.out.println(c.getParameterizedTypeClass());
  }

  public static interface EpicCoolInterface<T> {
    // Return Type rather than Class, because T not always be a class.
    // You can do type check and return Class<T> with force typecast.
    default Type getParameterizedTypeClass() {
      return GenericUtil.getGenericTypes(getClass(), EpicCoolInterface.class)[0];
    }
  }
}
