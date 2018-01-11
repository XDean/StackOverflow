package xdean.stackoverflow.rx;

import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.stream.Stream;

import xdean.jex.util.reflect.ReflectUtil;

public class Q46765735 {

  public static void main(String[] args) {
    create(TargetDomain.class).printTarget();
  }

  public static <T> T create(Class<T> clz) {
    T target = uncheck(() -> clz.newInstance());
    Stream.of(ReflectUtil.getAllFields(clz, false)).forEach(f -> uncheck(() -> fill(target, f)));
    return target;
  }

  private static <T> void fill(T target, Field field) throws Exception {
    TargetAnnotation anno = field.getAnnotation(TargetAnnotation.class);
    if (anno == null) {
      return;
    }
    Class<?> type = field.getType();
    if (!Target.class.isAssignableFrom(type)) {
      return;
    }
    field.setAccessible(true);
    Target value = (Target) field.get(target);
    if (value == null) {
      value = (Target) type.newInstance();
    }
    value.setFirst(anno.first());
    value.setSecond(anno.second());
    field.set(target, value);
  }
}

@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ ElementType.FIELD })
@interface TargetAnnotation {
  String first();

  String second();
}

class Target {
  String first;
  String second;

  public Target(String first, String second) {
    this.first = first;
    this.second = second;
  }

  public void setFirst(String first) {
    this.first = first;
  }

  public void setSecond(String second) {
    this.second = second;
  }
}

class TargetDomain {

  @TargetAnnotation(first = "first", second = "second")
  private Target target = new Target("a", "b");

  public void printTarget() {
    System.out.println(target.first); // should be `first`
    System.out.println(target.second); // should be `second`
  }
}