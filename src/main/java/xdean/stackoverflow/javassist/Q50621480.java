package xdean.stackoverflow.javassist;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.IntegerMemberValue;

public class Q50621480 {
  class MyClass {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface MyAnnotation {
    int frequency();
  }

  public static void main(String[] args) throws Exception {
    Class<?> annotatedClass = addAnnotation(MyClass.class.getName(), MyAnnotation.class.getName(), 10);

    System.out.println(annotatedClass.getAnnotation(MyAnnotation.class));
  }

  private static Class<?> addAnnotation(String className, String annotationName, int frequency) throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass ctClass = pool.makeClass(className + "1");//because MyClass has been defined

    ClassFile classFile = ctClass.getClassFile();
    ConstPool constpool = classFile.getConstPool();

    AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
    Annotation annotation = new Annotation(annotationName, constpool);
    annotation.addMemberValue("frequency", new IntegerMemberValue(classFile.getConstPool(), frequency));
    annotationsAttribute.setAnnotation(annotation);

    ctClass.getClassFile().addAttribute(annotationsAttribute);
    return ctClass.toClass();
  }
}
