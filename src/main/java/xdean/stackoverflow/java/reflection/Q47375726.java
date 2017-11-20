package xdean.stackoverflow.java.reflection;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.BiPredicate;

public class Q47375726 {
  public static void main(String[] args) throws Throwable {
    execute("genericFunction");
  }

  public static void execute(String filterName) throws Throwable {
    MethodType methodType = MethodType.methodType(boolean.class, Object.class, Object.class);
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    MethodHandle handle = lookup.findStatic(Q47375726.class, filterName, methodType);
    handle.invoke(null, null);
    BiPredicate<Object, Object> f = (BiPredicate<Object, Object>) LambdaMetafactory.metafactory(
        lookup, "test", MethodType.methodType(BiPredicate.class), methodType, handle, methodType)
        .getTarget()
        .invokeExact(null);

    resolve(null, null, f);
  }

  public static <S, T> void resolve(S src, T template, BiPredicate<S, T> p) {
    if (p.test(src, template)) {
      System.out.println("yes!");
    } else {
      System.out.println("no...");
    }
  }

  public static <S, T> boolean genericFunction(S x, T y) {
    System.out.println("test it!");
    return Math.random() > 0.5;
  }
}
