package xdean.stackoverflow.java.reflection;

import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

import io.reactivex.Observable;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class Q44563354 {
  public static void main(String[] args) throws Exception {
    // test normal
    System.out.println(get(new Exception().getStackTrace()[0]));
    // test lambda
    Runnable r = () -> System.out.println(uncheck(() -> get(new Exception().getStackTrace()[0])));
    r.run();
    // test function
    testHere(1);
  }

  private static void testHere(int i) throws Exception {
    System.out.println(get(new Exception().getStackTrace()[0]));
  }

  private static Optional<Method> get(StackTraceElement e) throws NotFoundException, ClassNotFoundException {
    Class<?> clz = Class.forName(e.getClassName());
    int line = e.getLineNumber();
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(clz.getName());
    return Observable.fromArray(cc.getDeclaredMethods())
        .sorted(Comparator.comparing(m -> m.getMethodInfo().getLineNumber(0)))
        .filter(m -> m.getMethodInfo().getLineNumber(0) <= line)
        .map(Optional::of)
        .blockingLast(Optional.empty())
        .map(m -> uncheck(() -> clz.getDeclaredMethod(m.getName(),
            Arrays.stream(m.getParameterTypes()).map(c -> uncheck(() -> nameToClass(c.getName()))).toArray(Class[]::new))));
  }

  private static Class<?> nameToClass(String name) throws ClassNotFoundException {
    switch (name) {
    case "int":
      return int.class;
    case "short":
      return short.class;
    case "long":
      return long.class;
    case "double":
      return double.class;
    case "float":
      return float.class;
    case "boolean":
      return boolean.class;
    case "char":
      return char.class;
    case "byte":
      return byte.class;
    case "void":
      return void.class;
    }
    if (name.endsWith("[]")) {
      return Array.newInstance(nameToClass(name.substring(0, name.length() - 2)), 0).getClass();
    }
    return Class.forName(name);
  }
}
