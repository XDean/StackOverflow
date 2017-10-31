package xdean.stackoverflow.java.reflection;

import java.lang.reflect.Field;

public class Q47027440 {

  public static void main(String[] args) throws Exception {
    MyClass myClass = new MyClass();
    Field field = MyClass.class.getField("str");
    set(field, myClass, "TestValue");
    System.out.println(myClass.str.actual);
  }

  public static void set(Field field, Object obj, Object value)
      throws InstantiationException, IllegalAccessException {
    if (field.getType() == MyString.class && value instanceof String) {
      MyString ms = MyString.class.newInstance();
      ms.actual = (String) value;
      value = ms;
    }
    field.set(obj, value);
  }

  public static class MyString {
    String actual;
  }

  public static class MyClass {
    public MyString str;
  }
}
