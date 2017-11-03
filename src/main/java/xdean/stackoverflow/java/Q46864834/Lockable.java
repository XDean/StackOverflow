package xdean.stackoverflow.java.Q46864834;

public interface Lockable extends AutoCloseable {

  void lock() throws Exception;

  void unlock() throws Exception;

  boolean isLocked();

  @Override
  default void close() throws Exception {
    unlock();
  }
}