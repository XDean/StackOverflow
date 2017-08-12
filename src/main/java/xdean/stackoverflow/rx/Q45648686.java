package xdean.stackoverflow.rx;

import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import rx.Observable;
import rx.schedulers.Schedulers;

public class Q45648686 {

  @Test
  public void testBackToMainThread() throws InterruptedException {
    processValue(1);
    processValue(2);
    processValue(3);
    processValue(4);
    processValue(5);
    System.out.println("done");
  }

  private LinkedBlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();

  private void processValue(int value) throws InterruptedException {
    Observable.just(value)
        .subscribeOn(Schedulers.io())
        .doOnNext(number -> processExecution())
        .observeOn(Schedulers.from(command -> tasks.add(command)))
        .subscribe(x ->
            System.out.println("Thread:" + Thread.currentThread().getName() + " value:" + x));
    tasks.take().run();
    tasks.take().run();
  }

  private void processExecution() {
    System.out.println("Execution in " + Thread.currentThread().getName());
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}