package xdean.stackoverflow.java;

import java.util.*;

public class Q47028607 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String[][] comms = { { "ACac", "CAca" }, { "ADad", "DAda" }, { "AEae", "EAea" }, { "AFaf", "FAfa" },
        { "AGag", "GAga" }, { "AHah", "HAha" }, { "AIai", "Iaia" }, { "AJaj", "JAja" }, { "AKak", "KAka" },
        { "ALal", "LAla" }, { "AMam", "MAma" }, { "ANan", "NAna" }, { "AOao", "OAoa" }, { "APap", "PApa" },
        { "AQaq", "QAqa" }, { "ARar", "RAra" }, { "ASas", "SAsa" }, { "ATat", "TAta" }, { "AUau", "UAua" },
        { "AVav", "VAva" }, { "AWaw", "WAwa" }, { "AXax", "XAxa" }, { "AYay", "YAya" }, { "AZaz", "ZAza" } };
    String master = "ABab";
    System.out.println("Amount of Nails: ");
    int N = scanner.nextInt();
    if (N < 2) {
      System.out.println("Sorry, You Must input a number Greater than or Equal to 2");
    } else if (N == 2) {
      System.out.println(master);
    } else {
      for (int c = 0; c < N - 2; c++) {
        System.out.println(master);
        master = master.replace("A", "$1");
        master = master.replace("a", "$2");
        master = master.replace("$1", comms[c][0]);
        master = master.replace("$2", comms[c][1]);
        System.out.println(master);
      }
    }
    scanner.close();
  }
}