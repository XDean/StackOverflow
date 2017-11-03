package xdean.stackoverflow.java.Q46864834;

import io.reactivex.functions.Consumer;

public class LockVisitor<T extends Lockable> implements AutoCloseable {
  public static <T extends Lockable> LockVisitor<T> create(T lockable) {
    return new LockVisitor<>(lockable);
  }

  T value;
  Exception error;

  public LockVisitor(T value) {
    this.value = value;
  }

  public LockVisitor<T> lock() {
    if (!value.isLocked()) {
      doWithException(Lockable::lock);
    }
    return this;
  }

  public LockVisitor<T> unlock() {
    if (value.isLocked()) {
      doWithException(Lockable::unlock);
    }
    return this;
  }

  public LockVisitor<T> doOnValue(Consumer<T> func) {
    if (value.isLocked()) {
      return doWithException(func);
    } else {
      return lock().doWithException(func).unlock();
    }
  }

  public LockVisitor<T> doOnError(java.util.function.Consumer<Exception> func) {
    if (error != null) {
      func.accept(error);
    }
    return this;
  }

  public <B extends Lockable> TwoLockVisitor<T, B> with(LockVisitor<B> other) {
    return new TwoLockVisitor<>(this, other);
  }

  public <B extends Lockable> TwoLockVisitor<T, B> with(B other) {
    return new TwoLockVisitor<>(this, create(other));
  }

  private LockVisitor<T> doWithException(Consumer<T> action) {
    if (error != null) {
      System.out.println("errored");
      return this;
    }
    try {
      action.accept(value);
    } catch (Exception e) {
      error = e;
      try {
        value.unlock();
      } catch (Exception e1) {
        e.addSuppressed(e1);
      }
    }
    return this;
  }

  @Override
  public void close() {
    unlock();
  }
}