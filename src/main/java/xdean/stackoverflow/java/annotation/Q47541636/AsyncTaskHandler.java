package xdean.stackoverflow.java.annotation.Q47541636;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class AsyncTaskHandler {
  @SuppressWarnings("unchecked")
  public static <T> T handle(T origin) {
    // collect async methods
    List<Method> asyncMethods = Arrays.stream(origin.getClass().getMethods())
        .filter(m -> m.getAnnotation(AsyncTask.class) != null)
        .collect(Collectors.toList());
    // if no async method, return itself
    if (asyncMethods.isEmpty()) {
      return origin;
    }
    return (T) Enhancer.create(origin.getClass(), (MethodInterceptor) (obj, method, args, proxy) -> {
      // if asyn, wrapped in your async code, here I simply create a new thread
      if (asyncMethods.contains(method)) {
        new Thread(() -> {
          try {
            proxy.invoke(origin, args);
          } catch (Throwable e) {
            e.printStackTrace();
          }
        }).start();
        return null;
      }
      return proxy.invoke(origin, args);
    });
  }
}
