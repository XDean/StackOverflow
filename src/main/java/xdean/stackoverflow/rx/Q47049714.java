package xdean.stackoverflow.rx;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.subjects.PublishSubject;

public class Q47049714 {
  public static class CheckInSuccess implements CheckInResult {
  }

  public static interface CheckInResult {
  }

  public class DeviceCheckInUseCase {
    public Observable<CheckInResult> checkIn() {
      return checkinSubject;
    }
  }

  PublishSubject<CheckInResult> checkinSubject;
  DeviceCheckInUseCase deviceCheckInUseCase;
  TestSubscriber<CheckInResult> subscriber;

  @Before
  public void setUp() {
    checkinSubject = PublishSubject.create();
    subscriber = new TestSubscriber<>();
    deviceCheckInUseCase = new DeviceCheckInUseCase();
  }

  @Test
  public void testCheckInSucceeded() {

    final ArrayList<CheckInResult> succeeded = new ArrayList<>();
    final ArrayList<Throwable> failed = new ArrayList<>();

    this.deviceCheckInUseCase.checkIn()
        .doOnNext(succeeded::add)
        .doOnError(failed::add)
        .subscribe(subscriber);

    this.subscriber.assertValueCount(0);
    assertEquals(0, succeeded.size());
    assertEquals(0, failed.size());

    this.checkinSubject.onNext(new CheckInSuccess());

    this.subscriber.assertValueCount(1);
    assertEquals(1, succeeded.size());
    assertEquals(0, failed.size());
  }
}
