package xdean.stackoverflow.java.Q46864834;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

public class TwoLockVisitor<A extends Lockable, B extends Lockable> {
  public static <A extends Lockable, B extends Lockable> TwoLockVisitor<A, B> create(A a, B b) {
    return new TwoLockVisitor<>(LockVisitor.create(a), LockVisitor.create(b));
  }

  LockVisitor<A> left;
  LockVisitor<B> right;

  public TwoLockVisitor(LockVisitor<A> lva, LockVisitor<B> lvb) {
    this.left = lva;
    this.right = lvb;
  }

  public TwoLockVisitor<A, B> lock() {
    left.lock();
    right.lock();
    return this;
  }

  public TwoLockVisitor<A, B> unlockBoth() {
    left.unlock();
    right.unlock();
    return this;
  }

  public TwoLockVisitor<A, B> doOnLeft(Consumer<A> func) {
    left.doOnValue(func);
    return this;
  }

  public TwoLockVisitor<A, B> doOnRight(Consumer<B> func) {
    right.doOnValue(func);
    return this;
  }

  public TwoLockVisitor<A, B> doOnBoth(BiConsumer<A, B> func) {
    left.doOnValue(a -> right.doOnValue(b -> func.accept(a, b)));
    return this;
  }

  public LockVisitor<A> toLeft() {
    right.close();
    return left;
  }

  public LockVisitor<B> toRight() {
    left.close();
    return right;
  }
}
