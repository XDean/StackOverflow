package xdean.stackoverflow.rx;

import static io.reactivex.internal.subscriptions.SubscriptionHelper.validate;
import static io.reactivex.internal.util.BackpressureHelper.*;
import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.UnicastProcessor;
import io.reactivex.schedulers.Schedulers;
import xdean.jex.util.task.tryto.Try;

public class Q47264933 {
  static class AndroidSchedulers {
    private static final Scheduler MOCK_ANDROID_MAIN = Schedulers.from(Executors.newFixedThreadPool(1));

    public static Scheduler mainThread() {
      return MOCK_ANDROID_MAIN;
    }
  }

  private static final long BASE_TIME = System.currentTimeMillis();

  public static void main(String[] args) throws InterruptedException {
    Q47264933 q = new Q47264933();
    IntStream.range(1, 10).forEach(i -> q.sendRequest(i));
    q.reciveResponse().subscribe(e -> System.out.println("\tdo for: " + e));
    Thread.sleep(10000);
    q.rerun();
    Thread.sleep(10000);
  }

  private Scheduler sequential = Schedulers.from(Executors.newFixedThreadPool(1));
  private FlowableProcessor<Integer> requestQueue = UnicastProcessor.create();
  private OnErrorStopSubscriber<String> stopSubscriber;

  public void sendRequest(Integer request) {
    requestQueue.onNext(request);
  }

  public Flowable<String> reciveResponse() {
    return requestQueue
        .observeOn(sequential)
        .doOnNext(i -> System.out.println(i + " start at:" + (System.currentTimeMillis() - BASE_TIME)))
        .map(i -> Try.to(() -> mockLongTimeRequest(i)))
        .doOnNext(t -> t.onException(e -> System.err.println("error happen: " + e))
            .toOptional()
            .ifPresent(s -> System.out.println(s + " done  at:" + (System.currentTimeMillis() - BASE_TIME))))
        .<String> lift(subscriber -> stopSubscriber = new OnErrorStopSubscriber<>(subscriber))
        .observeOn(AndroidSchedulers.mainThread());
  }

  public void rerun() {
    if (stopSubscriber != null) {
      stopSubscriber.rerun();
    }
  }

  private String mockLongTimeRequest(int i) {
    uncheck(() -> Thread.sleep((long) (1000 * Math.random())));
    if (i == 5) {
      throw new RuntimeException();
    }
    return Integer.toString(i);
  }

  private static final class OnErrorStopSubscriber<T> extends AtomicLong implements Subscriber<Try<T>>, Subscription {
    private final Subscriber<? super T> actual;
    private Subscription s;
    private AtomicLong stopRequest = new AtomicLong(-1L);

    public OnErrorStopSubscriber(Subscriber<? super T> subscriber) {
      actual = subscriber;
    }

    public void rerun() {
      while (true) {
        long sr = stopRequest.get();
        if (sr != -1L) {
          if (stopRequest.compareAndSet(sr, -1L)) {
            request(sr);
            break;
          }
        } else {
          break;
        }
      }
    }

    public void stop() {
      if (!stopRequest.compareAndSet(-1, getAndSet(0))) {
        shouldNotEmit();
      }
    }

    @Override
    public void onSubscribe(Subscription s) {
      if (validate(this.s, s)) {
        this.s = s;
        actual.onSubscribe(this);
      }
    }

    @Override
    public void onNext(Try<T> t) {
      if (stopRequest.get() != -1L) {
        shouldNotEmit();
      }
      if (t.isSuccess()) {
        if (produced(this, 1) > 0) {
          s.request(1);
        }
        actual.onNext(t.get());
      } else {
        stop();
      }
    }

    @Override
    public void onError(Throwable t) {
      if (stopRequest.get() != -1L) {
        shouldNotEmit();
      }
      actual.onError(t);
    }

    @Override
    public void onComplete() {
      if (stopRequest.get() != -1L) {
        shouldNotEmit();
      }
      actual.onComplete();
    }

    @Override
    public void request(long n) {
      if (stopRequest.get() != -1L) {
        add(stopRequest, n);
      } else if (add(this, n) == 0) {
        s.request(1);
      }
    }

    @Override
    public void cancel() {
      s.cancel();
    }

    private void shouldNotEmit() {
      throw new IllegalStateException("Upstream should not emit anything when not request.");
    }
  }
}
