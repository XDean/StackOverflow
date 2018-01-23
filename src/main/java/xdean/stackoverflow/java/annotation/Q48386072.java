package xdean.stackoverflow.java.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.Arrays;

import com.google.common.collect.ImmutableMap;

import xdean.jex.util.reflect.AnnotationUtil;
import xdean.jex.util.reflect.ReflectUtil;

public class Q48386072 {

    public static void main(String[] args) {
      Class<?> clz = User.class;
      Arrays.stream(clz.getFields()).map(ReflectUtil::getRootField).forEach(f -> processShortcut(f));
      thirdParty(clz);
    }

    public static void thirdParty(Class<?> clz) {
      // this is mock your 3rd party entry
      Arrays.stream(clz.getFields()).forEach(f -> System.out.println(f + "\t" + f.getAnnotation(Type.class)));
    }

  public static void processShortcut(Field f) {
    Shortcut s = f.getAnnotation(Shortcut.class);
    if (s != null) {
      AnnotationUtil.removeAnnotation(f, Shortcut.class);
      Parameter p = AnnotationUtil.createAnnotationFromMap(Parameter.class, ImmutableMap.of("name", "parameterClass", "value", s.value().getName()));
      Type t = AnnotationUtil.createAnnotationFromMap(Type.class,
          ImmutableMap.of("type", "com.company.ConstantClass", "parameters", new Parameter[] { p }));
      AnnotationUtil.addAnnotation(f, t);
    }
    }

    public static class User {
      @Shortcut(User.class)
      public Object parameter;
    }

  @Retention(RetentionPolicy.RUNTIME)
  public @interface Shortcut {
    Class<?> value();
  }


  @Retention(RetentionPolicy.RUNTIME)
  public @interface Type {
    String type();

    Parameter[] parameters();
  }

  public @interface Parameter {
    String name();

    String value();
  }
}
