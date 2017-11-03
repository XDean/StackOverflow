package xdean.stackoverflow.java.Q46864834;

public interface Lockable extends AutoCloseable {
  void lock() throws Exception;

  boolean isLocked();

  void unlock() throws Exception;

  @Override
  default void close() throws Exception {
    unlock();
  }
}