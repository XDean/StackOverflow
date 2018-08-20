package xdean.stackoverflow.javapoet;

import java.io.IOException;
import java.lang.reflect.Type;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

public class Q51915662 {
  public static void main(String[] args) throws IOException {
    TypeVariableName t = TypeVariableName.get("T").withBounds(U.class);
    TypeSpec type = TypeSpec.classBuilder("B")
        .addTypeVariable(t)
        .addSuperinterface(ParameterizedTypeName.get(ClassName.get(A.class), t))
        .build();
    JavaFile.builder("", type).build().writeTo(System.out);
  }
}

class U {
}

class A<T> {
}