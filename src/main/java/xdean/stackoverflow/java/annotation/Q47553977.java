package xdean.stackoverflow.java.annotation;

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;

import xdean.annotation.processor.toolkit.AssertException;
import xdean.annotation.processor.toolkit.XAbstractProcessor;

@SupportedAnnotationTypes("*")
public class Q47553977 extends XAbstractProcessor {
  public static void main(String[] args) {
    Compiler.javac()
        .withProcessors(new Q47553977())
        .compile(JavaFileObjects.forResource(Q47553977.class.getResource("ForCompile.java")));
  }

  @Override
  public boolean processActual(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws AssertException {
    if (roundEnv.processingOver()) {
      return false;
    }
    TypeMirror tm = elements.getTypeElement("java.lang.String").asType();
    TypeMirror comparable = types.erasure(elements.getTypeElement("java.lang.Comparable").asType());
    boolean isComparable = types.isAssignable(tm, comparable);
    System.out.println(isComparable);
    return false;
  }
}
