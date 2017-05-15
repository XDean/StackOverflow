package xdean.stackoverflow.rx;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import xdean.jex.extra.Pair;

public class Q43975663 {
  public static void main(String[] args) throws InterruptedException {
    PublishSubject<String> textSub = PublishSubject.create(); // emit user input text
    PublishSubject<String> taskSub = PublishSubject.create(); // emit when execution thread is free
    // core
    Observable
        // when new text input or execution thread change to free, emit an item
        .combineLatest(textSub.distinctUntilChanged(), taskSub, Pair::of)
        // if the text not change or task cycle not change, ignore it
        .scan((p1, p2) ->
            (p1.getLeft().equals(p2.getLeft()) || p1.getRight().equals(p2.getRight())) ?
                p1 : p2)
        .distinctUntilChanged()
        // map to user input text
        .map(Pair::getLeft)
        // scheduler to IO thread
        .observeOn(Schedulers.io())
        // do HTTP request
        .doOnNext(Q43975663::httpTask)
        // emit item to notify the execution thread is free
        .doOnNext(taskSub::onNext)
        .subscribe();
    // test
    taskSub.onNext("start");
    textSub.onNext("t");
    textSub.onNext("te");
    textSub.onNext("tex");
    textSub.onNext("text");
    Thread.sleep(5000);
    textSub.onNext("new");
    textSub.onNext("new");
    textSub.onNext("text");
    Thread.sleep(5000);
  }

  static void httpTask(String id) {
    System.out.printf("%s \tstart on \t%s\n", id, Thread.currentThread());
    try {
      Thread.sleep((long) (Math.random() * 1000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.printf("%s \tdone on \t%s\n", id, Thread.currentThread());
  }
}
