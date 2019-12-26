package xdean.stackoverflow.java;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Q51960890 {
  public static void main(String[] args) throws Exception {
//    ObjectOutputStream s = new ObjectOutputStream(new FileOutputStream("temp"));
//    s.writeObject(new Bean(1, "a"));
//    s.close();

    ObjectInputStream s = new ObjectInputStream(new FileInputStream("temp"));
    System.out.println(s.readObject());
  }
}

@Data
@AllArgsConstructor
class Bean implements Serializable {
  static final long serialVersionUID = 1;
  int id;
  String string;
  String aaa = "abc";
}

class BadThing {
  int id;
}