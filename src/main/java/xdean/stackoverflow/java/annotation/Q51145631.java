package xdean.stackoverflow.java.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Q51145631 {

  public static void main(String[] args) {
    Enginee.load(Enginee1.class);
    Enginee.load(Enginee2.class);
  }

  public interface IRule {
    void run();
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Rule {
    Class<? extends IRule>[] rule();
  }

  public static class Rule1 implements IRule {
    @Override
    public void run() {
      System.out.println("rule1");
    }
  }

  public static class Rule2 implements IRule {
    @Override
    public void run() {
      System.out.println("rule2");
    }
  }

  public static class Rule3 implements IRule {
    @Override
    public void run() {
      System.out.println("rule3");
    }
  }

  public static class Enginee {
    public static void load(Class<?> engineeClass) {
      Rule rule = engineeClass.getAnnotation(Rule.class);
      if (rule != null) {
        for (Class<? extends IRule> clz : rule.rule()) {
          try {
            IRule r = clz.newInstance();
            r.run();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  @Rule(rule = { Rule1.class, Rule2.class })
  public static class Enginee1 {
    static {

    }
  }

  @Rule(rule = { Rule2.class, Rule3.class })
  public static class Enginee2 {
  }
}
