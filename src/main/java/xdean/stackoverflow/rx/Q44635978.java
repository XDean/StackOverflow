package xdean.stackoverflow.rx;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public class Q44635978 {

  ConnectionManager connectionManager = new ConnectionManager();
  BehaviorSubject<Boolean> mSubject = BehaviorSubject.create();
  int TIMEOUT = 1000;

  public static void main(String[] args) {
    Q44635978 q = new Q44635978();
    Disposable s = q.connect()
        .doOnDispose(() -> q.disconnect())
        .subscribe();
    s.dispose();
    q.disconnect();
  }

  static class ConnectionManager {
    boolean connected;

    public boolean isConnected() {
      return connected;
    }

    public void connect(int timeout) {
      connected = true;
      System.out.println("Q44635978.ConnectionManager.connect()");
    }

    public void disconnect() {
      connected = false;
      System.out.println("Q44635978.ConnectionManager.disconnect()");
    }
  }

  public BehaviorSubject<Boolean> connect() {
    System.out.println("Q44635978.connect()");
    if (!connectionManager.isConnected()) {
      connectionManager.connect(TIMEOUT);
    }

    return mSubject;
  }

  public void disconnect() {
    System.out.println("Q44635978.disconnect()");
    if (connectionManager.isConnected() && !mSubject.hasObservers()) {
      connectionManager.disconnect();
    }
  }
}
