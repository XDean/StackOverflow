package xdean.stackoverflow.java;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Q65031068 {
  public static void main(String[] args) {
    Map<String, Integer> map = new HashMap<>();
    map.put("A", 1);
    map.put("B", 2);
    map.put("C", 3);
    System.out.println(map);
    PrintStream out = System.out;
    System.setOut(new PrintStream(out) {
      @Override
      public void println(Object o) {
        if (o instanceof Map) {
          super.println(((Map<?, ?>) o).entrySet().stream()
            .map(e -> String.format("%s %s", e.getKey(), e.getValue()))
            .collect(Collectors.joining("\n")));
        } else {
          super.println(o);
        }
      }
    });
    System.out.println(map);
  }
}
