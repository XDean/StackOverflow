package xdean.stackoverflow.java;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Q47129466 {
  public static void main(String[] args) {
    int[][] mat = get(5, 5);
    System.out.println(Arrays.deepToString(mat));
    System.out.println(Arrays.toString(rowSum(mat)));
    System.out.println(Arrays.toString(columnSum(mat)));
  }

  public static int[][] get(int width, int height) {
    return IntStream.range(0, height)
        .mapToObj(c -> IntStream.range(0, width)
            .map(r -> (int) (9 * Math.random() + 1))
            .toArray())
        .toArray(int[][]::new);
  }

  public static int[] rowSum(int[][] matrix) {
    return Arrays.stream(matrix).mapToInt(row -> IntStream.of(row).sum()).toArray();
  }

  public static int[] columnSum(int[][] matrix) {
    return Arrays.stream(matrix).reduce((a, b) -> add(a, b)).orElse(new int[0]);
  }

  public static int[] add(int[] a, int[] b) {
    int[] sum = new int[Math.max(a.length, b.length)];
    for (int i = 0; i < sum.length; i++) {
      sum[i] = (i < a.length ? a[i] : 0) + (i < b.length ? b[i] : 0);
    }
    return sum;
  }
}
