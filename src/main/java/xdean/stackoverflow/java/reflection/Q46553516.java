package xdean.stackoverflow.java.reflection;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import xdean.jex.util.reflect.ReflectUtil;

public class Q46553516 {
  public static void main(String[] args) throws Exception {
    // the input method
    Method method = ClassB.class.getMethod("func");
    // get the annotation value
    Class<?> clz = method.getDeclaringClass();
    List<Anno> collect = Stream.concat(
        Stream.of(clz),
        Stream.concat(
            Stream.of(ReflectUtil.getAllSuperClasses(clz)),
            Stream.of(ReflectUtil.getAllInterfaces(clz))))
        .map(c -> {
          try {
            return c.getMethod(method.getName(), method.getParameterTypes());
          } catch (Exception e) {
            return null;
          }
        })
        .filter(m -> m != null)
        .map(m -> m.getAnnotation(Anno.class))
        .filter(a -> a != null)
        .collect(Collectors.toList());
    collect.forEach(System.out::println);
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @Documented
  @Inherited
  public @interface Anno {
    String value();
  }

  static interface Inter {
    @Anno("Inter")
    void func();
  }

  static class ClassA implements Inter {
    @Override
    @Anno("ClassA")
    public void func() {

    }
  }

  static class ClassB extends ClassA {
    @Override
    public void func() {

    }
  }
}
