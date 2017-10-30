package xdean.stackoverflow.java.reflection;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import sun.misc.Cleaner;
import xdean.jex.util.lang.FinalizeSupport;

public class Q46844275 {
  public static void main(String[] args) throws Exception {
    byReflection();
    byFinalizeSupport();
  }

  private static void byFinalizeSupport() throws InterruptedException {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);
    FinalizeSupport.finalize(byteBuffer, () -> System.out.println("Clean"));
    byteBuffer = null;
    System.gc();
    Thread.sleep(10);
  }

  private static void byReflection()
      throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InterruptedException {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);
    Field cleanerField = Class.forName("java.nio.DirectByteBuffer").getDeclaredField("cleaner");
    cleanerField.setAccessible(true);
    Cleaner cleaner = (Cleaner) cleanerField.get(byteBuffer);
    Field thunkField = Cleaner.class.getDeclaredField("thunk");
    thunkField.setAccessible(true);
    Runnable originThunk = (Runnable) thunkField.get(cleaner);
    thunkField.set(cleaner, (Runnable) () -> {
      System.out.println("To clean");
      originThunk.run();
      System.out.println("Cleaned");
    });
    byteBuffer = null;
    System.gc();
    Thread.sleep(10);
  }
}
