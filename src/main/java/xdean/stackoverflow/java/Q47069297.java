package xdean.stackoverflow.java;

public class Q47069297 {
  public static void main(String[] args) {
    SuperClass sc = new SubClass();
    sc.print();
  }

  private static class SuperClass {
    private void print() {
      System.out.println("super");
    }
  }

  private static class SubClass extends SuperClass {
    @SuppressWarnings("unused")
    private void print() {
      System.out.println("sub");
    }
  }

}
