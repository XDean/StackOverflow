package xdean.stackoverflow.java.annotation.Q47541636;

public class Main {
  public static void main(String[] args) {
    SomeObject so = SomeObject.create();
    so.syncDo("1");
    so.asyncDo("2");
    so.syncDo("3");
    so.asyncDo("4");
  }
}

class SomeObject {
  public static SomeObject create() {
    return AsyncTaskHandler.handle(new SomeObject());
  }

  protected SomeObject() {
  }

  @AsyncTask
  public void asyncDo(String who) {
    System.out.println(who + "\tThread: " + Thread.currentThread());
  }

  public void syncDo(String who) {
    System.out.println(who + "\tThread: " + Thread.currentThread());
  }
}
