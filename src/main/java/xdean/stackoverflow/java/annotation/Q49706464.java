package xdean.stackoverflow.java.annotation;

import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import org.junit.After;

import com.google.auto.service.AutoService;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;

import xdean.annotation.processor.toolkit.AssertException;
import xdean.annotation.processor.toolkit.XAbstractProcessor;
import xdean.annotation.processor.toolkit.annotation.SupportedAnnotation;

@AutoService(Processor.class)
@SupportedAnnotation(After.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class Q49706464 extends XAbstractProcessor {

  @Override
  public boolean processActual(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws AssertException {
    roundEnv.getElementsAnnotatedWith(After.class)
        .forEach(e -> error().log("@After is banned.", e));
    return false;
  }

  public static void main(String[] args) {
    Compilation c = Compiler.javac()
        .withProcessors(new Q49706464())
        .compile(JavaFileObjects.forResource(Q49706464.class.getResource("ForQ49706464.java")));
    CompilationSubject.assertThat(c).hadErrorContaining("@After is banned.");
    c.errors().forEach(System.err::println);
  }
}
