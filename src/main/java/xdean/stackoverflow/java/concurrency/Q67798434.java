package xdean.stackoverflow.java.concurrency;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

@Warmup(iterations = 2, batchSize = 100, time = 5)
@Measurement(iterations = 5, batchSize = 100, time = 5)
public class Q67798434 {
  public static void main(String[] args) throws RunnerException {
    Options options = new OptionsBuilder()
      .include(Q67798434.class.getName() + ".*")
      .forks(1)
      .build();
    new Runner(options).run();
  }

  @State(Scope.Benchmark)
  public static class BenchmarkState {
    final LockFreeQueue<Integer> queueWithCheck = new LockFreeQueue<>(true);
    final LockFreeQueue<Integer> queueWithoutCheck = new LockFreeQueue<>(false);
  }

  @Benchmark
  @Threads(1)
  public void withCheck(BenchmarkState s) {
    s.queueWithCheck.enq(1);
    s.queueWithCheck.deq();
  }

  @Benchmark
  @Threads(1)
  public void withoutCheck(BenchmarkState s) {
    s.queueWithoutCheck.enq(1);
    s.queueWithoutCheck.deq();
  }
}

class LockFreeQueue<T> {

  AtomicReference<Node<T>> head, tail;
  final boolean check;

  public LockFreeQueue(boolean check) {
    this.check = check;
    Node<T> node = new Node<>(null);
    head = new AtomicReference<>(node);
    tail = new AtomicReference<>(node);
  }

  public void enq(T value) {
    Node<T> node = new Node<>(value);
    while (true) {
      Node<T> last = tail.get();
      Node<T> next = last.next.get();
      if (!check || last == tail.get()) {
        if (next == null) {
          if (last.next.compareAndSet(next, node)) {
            tail.compareAndSet(last, node);
            return;
          }
        } else {
          tail.compareAndSet(last, next);
        }
      }
    }
  }

  public T deq() throws NoSuchElementException {
    while (true) {
      Node<T> first = head.get();
      Node<T> last = tail.get();
      Node<T> next = first.next.get();
      if (!check || first == head.get()) {
        if (first == last) {
          if (next == null) {
            throw new NoSuchElementException();
          }
          tail.compareAndSet(last, next);
        } else {
          T value = next.value;
          if (head.compareAndSet(first, next)) {
            return value;
          }
        }
      }
    }
  }
}

class Node<T> {

  public T value;
  public AtomicReference<Node<T>> next;

  public Node(T value) {
    this.value = value;
    next = new AtomicReference<Node<T>>(null);
  }
}