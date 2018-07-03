package xdean.stackoverflow.java.annotation;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class Q50978487 {
  public static void main(String[] arg) {
    YourClass actualInstance = new YourClass();
    YourClass instanceToUse = (YourClass) Enhancer.create(YourClass.class, (MethodInterceptor) (obj, method, args, proxy) -> {
      System.out.println("enter the method: " + method.getName());
      Object result = method.invoke(actualInstance, args);
      System.out.println("exit the method: " + method.getName());
      return result;
    });
    instanceToUse.process();
  }

  public static class YourClass {
    public Object process() {
      System.out.println("do something");
      return new Object();
    }
  }
}
