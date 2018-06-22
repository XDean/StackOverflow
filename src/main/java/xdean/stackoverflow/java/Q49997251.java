package xdean.stackoverflow.java;

import java.lang.reflect.Array;

public class Q49997251 {
  public static void main(String[] args) {
    System.out.println(getArray("1").getClass());
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] getArray(T... value) {
    return value;
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] getArray(T value) {
    Object array = Array.newInstance(value.getClass(), 1);
    Array.set(array, 0, value);
    return (T[]) array;
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] getArray(Class<T> clz, Object value) {
    Object array = Array.newInstance(clz, 1);
    Array.set(array, 0, value);
    return (T[]) array;
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] getArray(Class<T> clz, Object... values) {
    Object array = Array.newInstance(clz, values.length);
    System.arraycopy(values, 0, clz, 0, values.length);
    return (T[]) array;
  }
}
