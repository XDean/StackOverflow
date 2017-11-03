package xdean.stackoverflow.java.Q46864834;

public class Resource implements Lockable {
  String name;
  boolean lock;

  public Resource(String name) {
    this.name = name;
  }

  @Override
  public boolean isLocked() {
    return lock;
  }

  @Override
  public void lock() {
    lock = true;
    System.out.println("lock: " + this);
  }

  @Override
  public void unlock() {
    lock = false;
    System.out.println("unlock: " + this);
  }

  @Override
  public void close() {
    unlock();
  }

  @Override
  public String toString() {
    return "Resource(" + name + ")";
  }
}