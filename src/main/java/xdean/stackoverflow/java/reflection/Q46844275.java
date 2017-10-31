package xdean.stackoverflow.java.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import sun.misc.Cleaner;
import sun.misc.Unsafe;
import xdean.jex.util.lang.FinalizeSupport;
import xdean.jex.util.lang.UnsafeUtil;

public class Q46844275 {
  public static void main(String[] args) throws Exception {
    byReflection();
    byFinalizeSupport();
  }

  static Constructor<? extends ByteBuffer> constructor;
  static Field addressField;
  static Unsafe unsafe;
  static {
    try {
      Class<? extends ByteBuffer> directClass = ByteBuffer.allocateDirect(1).getClass();
      constructor = directClass.getDeclaredConstructor(long.class, int.class);
      constructor.setAccessible(true);
      addressField = Buffer.class.getDeclaredField("address");
      addressField.setAccessible(true);
      unsafe = UnsafeUtil.getUnsafe();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static ByteBuffer newDirectByteBuffer(long addr, int cap) throws Exception {
    // just mock jni newDirectByteBuffer, ignore the address
    addr = unsafe.allocateMemory(cap);
    return constructor.newInstance(addr, cap);
  }

  private static long getAddress(ByteBuffer buffer) throws Exception {
    return (long) addressField.get(buffer);
  }

  private static void byFinalizeSupport() throws Exception {
    ByteBuffer byteBuffer = newDirectByteBuffer(0x0000ffaf, 100);
    long addr = getAddress(byteBuffer);
    FinalizeSupport.finalize(byteBuffer, () -> {
      unsafe.freeMemory(addr);
      System.out.println("byFinalizeSupport clean");
    });
    byteBuffer = null;
    System.gc();
    Thread.sleep(10);
  }

  private static void byReflection() throws Exception {
    ByteBuffer byteBuffer = newDirectByteBuffer(0x0000ffaf, 100);
    long addr = getAddress(byteBuffer);
    Cleaner cleaner = Cleaner.create(byteBuffer, () -> {
      unsafe.freeMemory(addr);
      System.out.println("byReflection clean");
    });
    // in fact this three lines is not needed.
    Field cleanerField = Class.forName("java.nio.DirectByteBuffer").getDeclaredField("cleaner");
    cleanerField.setAccessible(true);
    cleanerField.set(byteBuffer, cleaner);
    byteBuffer = null;
    System.gc();
    Thread.sleep(10);
  }
}
