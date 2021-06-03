package xdean.stackoverflow.java.concurrency;

import xdean.jex.extra.Either;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// not complete
public class Q58808242 {
  public static void main(String[] args) throws Exception {
    ExecutorService pool = Executors.newFixedThreadPool(1);
    List<Future<Either<Integer, InterruptedException>>> futures = invokeAll(
      pool,
      Arrays.asList(
        task(0),
        task(1),
        task(2)
      ),
      3000, TimeUnit.MILLISECONDS
    );
    for (Future<Either<Integer, InterruptedException>> f : futures) {
      if (f.isCancelled()) {
        System.out.println("canceled");
      } else {
        Either<Integer, InterruptedException> res = f.get();
        res.exec(i -> System.out.println("done"), e -> e.printStackTrace());
      }
    }
    pool.shutdown();
  }

  private static Callable<Integer> task(int i) {
    return () -> {
      System.out.println("start " + i);
      Thread.sleep(2000);
      System.out.println("end " + i);
      return i;
    };
  }

  public static <T> List<Future<Either<T, InterruptedException>>> invokeAll(
    ExecutorService service,
    Collection<? extends Callable<T>> tasks,
    long timeout,
    TimeUnit unit) throws InterruptedException {
    return service.invokeAll(tasks.stream()
      .<Callable<Either<T, InterruptedException>>>map(t -> () -> {
        try {
          return Either.left(t.call());
        } catch (InterruptedException e) {
          return Either.right(e);
        }
      })
      .collect(Collectors.toList()), timeout, unit);
  }
}
